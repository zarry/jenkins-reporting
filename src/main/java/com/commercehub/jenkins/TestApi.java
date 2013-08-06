package com.commercehub.jenkins;

import java.net.URLEncoder;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class TestApi {
    public static void main(String[] args) throws Exception {

        String serverRoot = "http://qatools02:8080";
        String job = "Default Trunk - D - Batch - Run 1";
        String encodedJob = URLEncoder.encode(job, "UTF-8").replace("+", "%20");
        String fullUrl = serverRoot + "/job/" + encodedJob + "/";

        JenkinsJobApi jobApi = new JenkinsJobApi(fullUrl);
        int lastCompletedBuild = Integer.parseInt(jobApi.getLastCompletedBuild());
        int firstBuild = Integer.parseInt(jobApi.getFirstBuild());
        int jobLimit = 2;
        int startingBuild = lastCompletedBuild - jobLimit + 1;


        ResultsWriter rw = new ResultsWriter();
        rw.writerJobInfo(serverRoot,job);
        rw.writerTableHeader();


        for (int currentBuild = startingBuild; currentBuild <= lastCompletedBuild; currentBuild++) {
            String currentBuildString = ((Integer) currentBuild).toString();

            TestNgResultsJobApi resultsApi = new TestNgResultsJobApi(fullUrl, currentBuildString );
            JenkinsBuildNumberApi jobNumApi = new JenkinsBuildNumberApi(fullUrl, currentBuildString);

            String failCount = resultsApi.getFailTestCount();
            String totalCount = resultsApi.getTotalTestCount();
            String duration = rw.convertMilliSecToReadable(jobNumApi.getDuration());
            String jobName = jobApi.getJobName();

            //rw.writeReportRow(currentBuildString, failCount, totalCount, duration);

            //System.out.println("Job: " + jobName + " Build: " + currentBuildString + " Failed Test Count: " + failCount + " Total Test Count: " + totalCount + " Duration: " + duration);

        }

    }
}
