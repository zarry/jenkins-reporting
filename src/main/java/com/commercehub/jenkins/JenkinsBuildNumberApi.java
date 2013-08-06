package com.commercehub.jenkins;

import org.dom4j.Document;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class JenkinsBuildNumberApi extends JenkinsApi implements IApi{
    private Document dom;

    public JenkinsBuildNumberApi(String rootJobUrl, String buildNumber) {
     String jobNumberUrl = rootJobUrl + buildNumber + "/api/xml";

     this.dom = getDom(createUrl(jobNumberUrl));
    }

    public String getDuration() {
        return dom.getRootElement().elementText("duration");
    }

    public String getResult() {
        return dom.getRootElement().elementText("result");
    }

    public String getTimeStamp() {
        return dom.getRootElement().elementText("timestamp");
    }

}
