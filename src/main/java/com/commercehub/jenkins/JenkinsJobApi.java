package com.commercehub.jenkins;

import org.dom4j.Document;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class JenkinsJobApi extends JenkinsApi {
    public Document dom;

    public JenkinsJobApi(String rootJobUrl) {
        String jobUrl = rootJobUrl + "api/xml";

        this.dom = getDom(createUrl(jobUrl));
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
