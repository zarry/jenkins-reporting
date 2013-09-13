package com.zarry.jenkins.Reports;

import com.zarry.jenkins.*;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Option(name ="-regex",
            usage = "Regex to match jobs on CI.  Using this  will run the report against all jobs from CI that match given regex")
    private String regex;


    static ResultsWriter rw = new ResultsWriter();
    private static final String JOB_HEADER = "Job";
    private static final String DURATION_HEADER = "Avg Duration";
    private static final String ALL_JOBS_TREE_QUERY = "jobs[name]";
    private static final String ALL_BUILDS_TREE_QUERY = "builds[number,status,timestamp,id,duration,result]";
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
        ArrayList<String> matchingJobs = new ArrayList<String>();
        for(String job : jobs){
            if(job.matches(regex)){
                System.out.println("Match => " + job);
                matchingJobs.add(job);
            }
        }
        HashMap<String, ArrayList<JenkinsBuildBean>> jobsAndBuilds = new HashMap<String, ArrayList<JenkinsBuildBean>>();

        for(String matchingJob : matchingJobs){
            JenkinsJobApi jobApi = new JenkinsJobApi(buildJobUrl(serverRoot, matchingJobs.get(5)), ALL_BUILDS_TREE_QUERY);
            LinkedHashMap<String, HashMap<String, String>> builds = jobApi.getAllBuildsForJob();
            ArrayList<JenkinsBuildBean> allBuilds = new ArrayList<JenkinsBuildBean>();
            for(String key : builds.keySet()){
                HashMap<String, String> buildInfo = builds.get(key);
                JenkinsBuildBean build = new JenkinsBuildBean();
                //System.out.println("Number: " + buildInfo.get("number") + "Timestamp: " + buildInfo.get("timestamp") );
                build.setDuration(buildInfo.get("duration"));
                build.setNumber(buildInfo.get("number"));
                allBuilds.add(build);
            }
            jobsAndBuilds.put(matchingJob,allBuilds);
        }

        System.out.println("Testing => " + jobsAndBuilds.get("Default Trunk - A - VM Reserve").get(2).getNumber());

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
