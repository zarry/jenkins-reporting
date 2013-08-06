package com.commercehub.jenkins;

import java.io.Serializable;
import java.util.Date;

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
    private Date time;


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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
