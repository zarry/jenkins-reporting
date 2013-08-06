package com.zarry.jenkins;

import org.dom4j.Document;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class TestNgResultsJobApi extends JenkinsApi {
    String failedTestCount;
    String totalTestCount;
    private Document dom;

    public TestNgResultsJobApi(String rootJobUrl, String buildNumber) {
        String testNgResults = "testngreports/api/xml";
        String testngUrl = rootJobUrl + buildNumber + "/" + testNgResults;

        setData(getDom(createUrl(testngUrl)));

    }

    private void setData(Document dom) {
        if (dom == null){
            setFailedTestCount("-");
            setTotalTestCount("-");
        }else{
            setFailedTestCount(dom.getRootElement().elementText("fail"));
            setTotalTestCount(dom.getRootElement().elementText("total"));
        }
    }

    public String getFailTestCount() {
        return this.failedTestCount;
    }

    public String getTotalTestCount() {
        return this.totalTestCount;
    }

    public void setFailedTestCount(String failedTestCount) {
        this.failedTestCount = failedTestCount;
    }

    public void setTotalTestCount(String totalTestCount) {
        this.totalTestCount = totalTestCount;
    }


}
