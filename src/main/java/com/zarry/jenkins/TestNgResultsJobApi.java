package com.zarry.jenkins;

import org.dom4j.Document;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class TestNgResultsJobApi extends AbstractJenkinsApi implements Api {
    private String buildNumber;
    private String rootJobUrl;
    private static final String TEST_NG_RESULTS = "testngreports";
    private Document dom;

    public TestNgResultsJobApi(String rootJobUrl, String buildNumber) {
        this.buildNumber = buildNumber;
        this.rootJobUrl = rootJobUrl;
        this.dom = getDom(createUrl(constructUrlString()));
    }

    public String constructUrlString(){
        return  rootJobUrl + buildNumber + "/" + TEST_NG_RESULTS + "/" + APIXML;
    }

    public String getFailTestCount() {
        return accessDomRootElement("fail");
    }

    public String getTotalTestCount() {
        return accessDomRootElement("total");
    }

    private String accessDomRootElement(String elementText){
        if(dom == null){
            return "-";
        }else{
            return dom.getRootElement().elementText(elementText);
        }
    }

}
