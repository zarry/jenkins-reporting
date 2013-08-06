package com.zarry.jenkins;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.List;

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
        return getElementOffRoot("duration");
    }

    public String getResult() {
        return getElementOffRoot("result");
    }

    public String getTimeStamp() {
        return getElementOffRoot("timestamp");
    }

    public String getShortDescription(){
        return getCauseNode().elementText("shortDescription");
    }

    public String getUpstreamBuild(){
        return getCauseNode().elementText("upstreamBuild");
    }

    public String getUpstreamProject(){
        return getCauseNode().elementText("upstreamProject");
    }

    public String getUpstreamUrl(){
        return getCauseNode().elementText("upstreamUrl");
    }

    public HashMap<String,String> getArtifacts(){
        List<Element> nodes = dom.getRootElement().elements("artifact");
        HashMap<String,String> artifacts = new HashMap<String, String>();

        for(Element node : nodes){
            artifacts.put(node.elementText("fileName"), node.elementText("relativePath"));
        }
        return artifacts;
    }

    public String getDescription(){
        return getElementOffRoot("description");
    }

    public String getEstimatedDuration(){
        return getElementOffRoot("estimatedDuration");
    }

    public String getFullDisplayName(){
        return getElementOffRoot("fullDisplayName");
    }

    public String getId(){
        return getElementOffRoot("id");
    }

    public String getKeepLog(){
        return getElementOffRoot("keepLog");
    }

    public String getNumber(){
        return getElementOffRoot("number");
    }

    public String getUrl(){
        return getElementOffRoot("url");
    }

    private String getElementOffRoot(String element){
        return dom.getRootElement().elementText(element);
    }

    private Element getCauseNode(){
        return dom.getRootElement().element("action").element("cause");
    }

}
