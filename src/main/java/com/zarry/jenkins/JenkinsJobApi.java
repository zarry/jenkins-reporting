package com.zarry.jenkins;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class JenkinsJobApi extends AbstractJenkinsApi implements Api {
    private Document dom;
    private String rootJobUrl;
    private String treeQuery;

    public JenkinsJobApi(String rootJobUrl) {
        this.rootJobUrl = rootJobUrl;
        this.dom = getDom(createUrl(constructUrlString()));
    }

    public JenkinsJobApi(String rootJobUrl, String treeQuery) {
        this.rootJobUrl = rootJobUrl;
        this.treeQuery = treeQuery;
        this.dom = getDom(createUrl(constructTreeQuery()));
    }


    public String constructUrlString(){
        return rootJobUrl + APIXML;
    }

    public String constructTreeQuery() {
        return rootJobUrl + APIXML + TREE + treeQuery;
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

    public LinkedHashMap<String, HashMap<String, String>> getAllBuildsForJob(){
        List<Element> nodes = dom.getRootElement().elements("build");
        LinkedHashMap<String, HashMap<String,String>> buildsAndData = new LinkedHashMap<String, HashMap<String, String>>();

        for(Element node : nodes){
            HashMap<String, String> build = new HashMap<String, String>();

            build.put("duration", node.elementText("duration"));
            build.put("id", node.elementText("id"));
            build.put("number", node.elementText("number"));
            build.put("result", node.elementText("result"));
            build.put("timestamp", node.elementText("timestamp"));

            buildsAndData.put(node.elementText("number"), build);
        }
        return buildsAndData;
    }

}
