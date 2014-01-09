package com.zarry.jenkins;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: lzarou
 * Date: 6/22/13
 * Time: 1:45 PM
 */

public class JenkinsRootApi extends AbstractJenkinsApi implements Api {
    private Document dom;
    private String rootUrl;
    private String treeQuery;

    public JenkinsRootApi(String rootUrl) {
        this.rootUrl = rootUrl;
        this.dom = getDom(createUrl(constructUrlString()));
    }

    public JenkinsRootApi(String rootUrl, String treeQuery) {
        this.rootUrl = rootUrl + "/";
        this.treeQuery = treeQuery;
        this.dom = getDom(createUrl(constructTreeQuery()));
    }

    public String constructUrlString() {
        return rootUrl + APIXML;
    }

    public String constructTreeQuery() {
        return rootUrl + APIXML + TREE + treeQuery;
    }

    public ArrayList<String> getAllJobNames(){
        List<Element> nodes = dom.getRootElement().elements("job");
        ArrayList<String> jobs = new ArrayList<String>();

        for(Element node : nodes){
            jobs.add(node.elementText("name"));
        }
        return jobs;
    }

   /*  Custom method to return job and timestamp of all jobs
    *  Used with custom tree query -> "jobs[name,lastBuild[number,duration,timestamp]"
    *  XML looks something like this...
    *  <br/><br/>
    *  ...
    *  <job>
    *  <name>2013_ATS_Improvements - D - Batch - Run 1</name>
    *  <lastBuild>
    *  <duration>1393083</duration>
    *  <number>3</number>
    *  <result>UNSTABLE</result>
    *  <timestamp>1384187053000</timestamp>
    *  </lastBuild>
    *  </job>
    *  ...
    */
    public HashMap<String, String> getAllJobsAndLastTimeStamp(){
        List<Element> nodes = dom.getRootElement().elements("job");
        HashMap<String, String> jobsAndLastBuild = new HashMap<String, String>();

        for(Element node : nodes){
            if(node.element("lastBuild") == null){
                jobsAndLastBuild.put(
                        node.elementText("name"),
                        "0");
            }else{
                jobsAndLastBuild.put(
                        node.elementText("name"),
                        node.element("lastBuild").elementText("timestamp"));
            }

        }
        return jobsAndLastBuild;
    }

    private String getElementOffRoot(String element){
        return dom.getRootElement().elementText(element);
    }

}
