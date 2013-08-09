package com.zarry.jenkins;

import org.dom4j.Document;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class JenkinsJobApi extends JenkinsApi implements Api {
    private Document dom;
    private String rootJobUrl;

    public JenkinsJobApi(String rootJobUrl) {
        this.rootJobUrl = rootJobUrl;
        this.dom = getDom(createUrl(constructUrlString()));
    }

    public String constructUrlString(){
        return rootJobUrl + APIXML;
    }

    public String getLastCompletedBuild() {
        return dom.getRootElement().element("lastCompletedBuild").elementText("number");
    }

    public String getFirstBuild() {
        return dom.getRootElement().element("firstBuild").elementText("number");
    }

    public String getJobName() {
        return dom.getRootElement().elementText("name");
    }

}
