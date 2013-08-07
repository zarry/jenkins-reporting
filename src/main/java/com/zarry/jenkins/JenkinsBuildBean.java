package com.zarry.jenkins;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Author: lzarou
 * Date: 7/17/13
 * Time: 10:08 PM
 */
public class JenkinsBuildBean implements Serializable {
    private String buildNumber;
    private String failedTestCount;
    private String totalTestCount;
    private String duration;
    private Date timestamp;
    private String result;
    private String shortDescription;
    private String upstreamBuild;
    private String upstreamProject;
    private String upstreamUrl;
    private HashMap<String, String> artifacts;
    private String description;
    private String estimatedDuration;
    private String fullDisplayName;
    private String id;
    private String keepLog;
    private String number;
    private String url;


    public JenkinsBuildBean() {
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getFailedTestCount() {
        return failedTestCount;
    }

    public void setFailedTestCount(String failedTestCount) {
        this.failedTestCount = failedTestCount;
    }

    public String getTotalTestCount() {
        return totalTestCount;
    }

    public void setTotalTestCount(String totalTestCount) {
        this.totalTestCount = totalTestCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUpstreamBuild() {
        return upstreamBuild;
    }

    public void setUpstreamBuild(String upstreamBuild) {
        this.upstreamBuild = upstreamBuild;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getUpstreamProject() {
        return upstreamProject;
    }

    public void setUpstreamProject(String upstreamProject) {
        this.upstreamProject = upstreamProject;
    }

    public String getUpstreamUrl() {
        return upstreamUrl;
    }

    public void setUpstreamUrl(String upstreamUrl) {
        this.upstreamUrl = upstreamUrl;
    }

    public HashMap<String, String> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(HashMap<String, String> artifacts) {
        this.artifacts = artifacts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public void setFullDisplayName(String fullDisplayName) {
        this.fullDisplayName = fullDisplayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeepLog() {
        return keepLog;
    }

    public void setKeepLog(String keepLog) {
        this.keepLog = keepLog;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
