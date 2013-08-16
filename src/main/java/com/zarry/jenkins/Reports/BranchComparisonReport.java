/*
 * Copyright (c) 1999-2013 Commerce Technologies Inc. All rights reserved
 *
 * This software is the confidential and proprietary information of Commerce
 * Technologies, Inc. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Commerce Technologies, Inc.
 */
package com.zarry.jenkins.Reports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zarry.jenkins.JenkinsApi;
import com.zarry.jenkins.JenkinsJobApi;

import org.dom4j.Document;
import org.dom4j.Element;

import org.kohsuke.args4j.Option;

/**
 * @author Brett Duclos
 */
public class BranchComparisonReport extends AbstractJenkinsReport {
    @Option(name = "-url", usage = "Sets the root CI url")
    private String serverRoot;

    @Option(name = "-baseJob", usage = "Sets the Base CI job")
    private String baseJob;

    @Option(name = "-testJob", usage = "Sets the Test CI job")
    private String testJob;

    private ParsedFailedApi baseFailedApi;
    private ParsedFailedApi testFailedApi;

    private Set<String> baseFailures;

    public BranchComparisonReport() {
        baseFailures = new HashSet<String>();
    }

    public static void main(String[] args) {
        new BranchComparisonReport().runReport(args);
    }

    @Override
    public void setUp() {
        String baseUrl = buildJobUrl(serverRoot, encodeStringForUrl(baseJob));
        baseFailedApi = buildParsedFailedApi(baseUrl);

        String testUrl = buildJobUrl(serverRoot, encodeStringForUrl(testJob));
        testFailedApi = buildParsedFailedApi(testUrl);
    }

    private ParsedFailedApi buildParsedFailedApi(String url) {
        JenkinsJobApi jobApi = new JenkinsJobApi(url);
        int lastCompletedBranchBuild = Integer.parseInt(jobApi.getLastCompletedBuild());
        return new ParsedFailedApi(url, lastCompletedBranchBuild);
    }

    @Override
    public void executeReport() {
        baseFailures.addAll(baseFailedApi.getAllFailedTests());

        List<String> branchFailures = testFailedApi.getAllFailedTests();

        System.out.println("Tests failing in '" + testJob + "' that are not failing in '" + baseJob + "':");
        for (String failure : branchFailures) {
            if (!baseFailures.contains(failure)) {
                System.out.println("\t" + failure);
            }
        }
    }

    @Override
    public void tearDown() {
        // No-op
    }

    @Override
    String getReportName() {
        return "DefaultComparisonReport";
    }

    @Override
    String getUsageMessage() {
        return "Example: java " + getReportName() + " -url \"http://qatools02:8080\" "
            + "-baseJob \"Default Trunk - D - Batch - Run 1\" -testJob \"August20Staging - F - Batch - Run 3\"";
    }

    private class ParsedFailedApi extends JenkinsApi {
        private Document dom;
        private String rootJobUrl;
        private String buildNumber;

        private static final String VIEW_FILE_URL =
            "/artifact/lightest-report/parsedfailed.xml/*view*/";

        public ParsedFailedApi(String rootJobUrl, int buildNumber) {
            this.rootJobUrl = rootJobUrl;
            this.buildNumber = String.valueOf(buildNumber);
            dom = getDom(createUrl(constructUrlString()));
        }

        @Override
        public String constructUrlString() {
            return rootJobUrl + buildNumber + VIEW_FILE_URL;
        }

        @SuppressWarnings("unchecked")
        public List<String> getAllFailedTests() {
            List<String> failedTests = new ArrayList<String>();

            List<Element> testList = dom.getRootElement().elements("test");
            for (Element node : testList) {
                List<Element> defaultClassList = node.element("classes").elements();
                for (Element element : defaultClassList) {
                    failedTests.add(element.attributeValue("name"));
                }
            }

            return failedTests;
        }
    }
}
