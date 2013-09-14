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
    private HashMap<String, String> jobAvgDuration = new HashMap<String, String>();


    public static void main(String[] args){
        new JobDurationReport().runReport(args);
    }
    
    @Override
	void setUp() {
        setColumnHeaderAndWidth();

	}

    private void setColumnHeaderAndWidth(){
        columnHeaderAndWidth.put(JOB_HEADER, 62);
        columnHeaderAndWidth.put(DURATION_HEADER, 0);
    }


    @Override
	void executeReport() {
            gatherData(serverRoot);
            writeReport(jobAvgDuration);
	}

    private void gatherData(String serverRoot){
        HashMap<String, ArrayList<JenkinsBuildBean>> jobsAndBuilds = new HashMap<String, ArrayList<JenkinsBuildBean>>();
        JenkinsRootApi rootApi = new JenkinsRootApi(serverRoot, ALL_JOBS_TREE_QUERY);

        for(String matchingJob : getMatchingJobs(rootApi.getAllJobNames())){
            JenkinsJobApi jobApi = new JenkinsJobApi(buildJobUrl(serverRoot, matchingJob), ALL_BUILDS_TREE_QUERY);
            jobsAndBuilds.put(matchingJob, populateAllBuildsForJob(jobApi.getAllBuildsForJob()));
            jobAvgDuration.put(matchingJob, rw.getAverageDuration(populateAllBuildsForJob(jobApi.getAllBuildsForJob())).toString());
        }
    }

    private ArrayList<String> getMatchingJobs(ArrayList<String> jobs){
        ArrayList<String> matchingJobs = new ArrayList<String>();
        for(String job : jobs){
            if(job.matches(regex)){
                matchingJobs.add(job);
            }
        }
        return matchingJobs;
    }

    private ArrayList<JenkinsBuildBean> populateAllBuildsForJob(LinkedHashMap<String, HashMap<String, String>> builds){
        ArrayList<JenkinsBuildBean> allBuilds = new ArrayList<JenkinsBuildBean>();
        for(String key : builds.keySet()){
            HashMap<String, String> buildInfo = builds.get(key);
            JenkinsBuildBean build = new JenkinsBuildBean();
            build.setDuration(buildInfo.get("duration"));
            build.setNumber(buildInfo.get("number"));
            allBuilds.add(build);
        }
        return allBuilds;
    }

    private void writeReport(HashMap<String, String> jobAndDuration){
        try{
            rw.writeGenericHeader(columnHeaderAndWidth);

            for(String job : jobAndDuration.keySet()){
                writeRow(job, jobAndDuration.get(job));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeRow(String job, String duration){
        LinkedHashMap<String,String> headerWithRowValue = new LinkedHashMap<String, String>();
        headerWithRowValue.put(JOB_HEADER, job);
        headerWithRowValue.put(DURATION_HEADER, rw.convertMilliSecToReadable(duration));
        rw.writeReportRow(headerWithRowValue);
    }

	@Override
	String getReportName() {
		return "JobDurationReport";
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
