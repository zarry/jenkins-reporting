package com.zarry.jenkins.Reports;


import com.zarry.jenkins.JenkinsBuildBean;
import com.zarry.jenkins.JenkinsJobApi;
import com.zarry.jenkins.JenkinsRootApi;
import com.zarry.jenkins.ResultsWriter;
import org.kohsuke.args4j.Option;

import java.text.SimpleDateFormat;
import java.util.*;

public class DormantJobsPendingRemoval extends AbstractJenkinsReport {
    @Option(name="-url",usage="URL root to Jenkins CI Server")
    private String serverRoot;

    @Option(name ="-regex",
            usage = "Regex to match jobs on CI.  Using this will run the report against all jobs from CI that match given regex")
    private String regex;

    @Option(name ="-days",
            usage = "Consective days inactive that would trigger a job as being dormant or inactive.  ")
    private int days;

    static ResultsWriter rw = new ResultsWriter();
    private static final String JOB_HEADER = "Jobs Pending Removal";
    private static final String LAST_EXECUTED = "Last Executed";
    private static final String ALL_JOBS_TREE_QUERY = "jobs[name,lastBuild[number,duration,timestamp]]";
    private LinkedHashMap<String, Integer> columnHeaderAndWidth = new LinkedHashMap<String, Integer>();
    private HashMap<String, String> matchingJobsAndInfo = new HashMap<String, String>();

    public static void main(String[] args){
        new DormantJobsPendingRemoval().runReport(args);
    }

    @Override
    void setUp() {
        setColumnHeaderAndWidth();

    }

    private void setColumnHeaderAndWidth(){
        columnHeaderAndWidth.put(JOB_HEADER, 60);
        columnHeaderAndWidth.put(LAST_EXECUTED, 25);
    }

    @Override
    void executeReport() {
        gatherData(serverRoot);
        writeReport();
    }

    private void gatherData(String serverRoot){
        JenkinsRootApi rootApi = new JenkinsRootApi(serverRoot, ALL_JOBS_TREE_QUERY);
        HashMap<String, String> jobsAndLastBuild = rootApi.getAllJobsAndLastTimeStamp();

        for(String job : jobsAndLastBuild.keySet()){
            if(job.matches(regex) && isJobLastRunQualifyForRemoval(rw.convertEpoch(jobsAndLastBuild.get(job)))){
                matchingJobsAndInfo.put(job, jobsAndLastBuild.get(job));
            }
        }
    }

    private void writeReport(){
        try{
            rw.writeGenericHeader(columnHeaderAndWidth);
            for(String job : matchingJobsAndInfo.keySet()){
                writeRow(job,  matchingJobsAndInfo.get(job));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeRow(String job, String lastRunDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm aaa");
        LinkedHashMap<String,String> headerWithRowValue = new LinkedHashMap<String, String>();

        headerWithRowValue.put(JOB_HEADER, job);
        headerWithRowValue.put(LAST_EXECUTED, dateFormat.format(rw.convertEpoch(lastRunDate)));

        rw.writeReportRow(headerWithRowValue);
    }

    boolean isJobLastRunQualifyForRemoval(Date lastRunDate) {
        long DAY_IN_MS = 1000 * 3600 * 24;
        Date expireDate = new Date(System.currentTimeMillis() - (days * DAY_IN_MS));
        return lastRunDate.before(expireDate);
    }

    @Override
    String getReportName() {
        return "DormantJobsPendingRemoval";
    }

    @Override
    String getUsageMessage() {
        return "Example: java " + getReportName() + " -url \"http://qatools02:8080\" "
                + "-regex \".*Setup.*\"" + "-days 30";
    }

    @Override
    void tearDown() {
        //No-op
    }


}
