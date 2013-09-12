package com.zarry.jenkins.Reports;

import com.zarry.jenkins.*;
import org.kohsuke.args4j.Option;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Author: lzarou
 * Date: 7/17/13
 * Time: 7:03 PM
 */
public class JobDurationReport extends AbstractJenkinsReport{
    @Option(name="-url",usage="URL root to Jenkins CI Server")
    private String serverRoot;

    @Option(name="-buildLimit",usage="Number of builds to gather data for.")
    private int buildLimit;

    private LinkedHashSet<String> jobs = new LinkedHashSet<String>();
    @Option(name = "-job", metaVar = "\"job1,job2,jobN\"",
            usage = "CI Job(s) to generate report for. Comma delimited for multiple jobs")
    private void setJobs(final String job){
        String[] alljobs = job.split(",");
        for(String aJob : alljobs){
            jobs.add(aJob);
        }
    }

    @Option(name ="-match",
            usage = "Regex to match jobs on CI.  Using this  will run the report against all jobs from CI that match given regex")
    private String regex;


    static ResultsWriter rw = new ResultsWriter();
    private static final String JOB_HEADER = "Job";
    private static final String DURATION_HEADER = "Avg Duration";
    private static final String ALL_JOBS_TREE_QUERY = "jobs[name]";
    private LinkedHashMap<String, Integer> columnHeaderAndWidth = new LinkedHashMap<String, Integer>();


    public static void main(String[] args){
        new JobDurationReport().runReport(args);
    }
    
    @Override
	void setUp() {
	}
    
    @Override
	void executeReport() {

            gatherData(serverRoot);
	}

    private void gatherData(String serverRoot){
        JenkinsRootApi rootApi = new JenkinsRootApi(serverRoot, ALL_JOBS_TREE_QUERY);
        ArrayList<String> jobs = rootApi.getAllJobNames();
        for(String job : jobs){
            System.out.println("Job => " + job);
        }

    }
       
    private void writeReport(){}


	@Override
	String getReportName() {
		return "JobHealthReport";
	}

	@Override
	String getUsageMessage() {
		return "Example: java " + getReportName() + " -url \"http://qatools02:8080\" "
				+ "-job \"Default Trunk - D - Batch - Run 1\" -buildLimit 5";
	}

	@Override
	void tearDown() {
		//No-op
	}
}
