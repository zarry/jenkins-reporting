package com.zarry.jenkins.Reports;

import com.zarry.jenkins.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.kohsuke.args4j.Option;

/**
 * Author: lzarou
 * Date: 7/17/13
 * Time: 7:03 PM
 */
public class JobHealthReport extends AbstractJenkinsReport{
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

    static ResultsWriter rw = new ResultsWriter();
    ArrayList <JenkinsBuildBean> buildList = new ArrayList<JenkinsBuildBean>(0);

    private static final String BUILD_COLUMN_HEADER = "Build";
    private static final String FAILED_TEST_HEADER = "Failed Test Count";
    private static final String TOTAL_TEST_HEADER = "Total Test Count";
    private static final String DURATION_HEADER = "Total Duration";
    private static final String DATE_HEADER = "Date";
    private LinkedHashMap<String, Integer> columnHeaderAndWidth = new LinkedHashMap<String, Integer>();


    public static void main(String[] args){
        new JobHealthReport().runReport(args);
    }
    
    @Override
	void setUp() {
    	setColumnHeaderAndWidth();        
	}
    
    private void setColumnHeaderAndWidth(){
        columnHeaderAndWidth.put(BUILD_COLUMN_HEADER, 0);
        columnHeaderAndWidth.put(FAILED_TEST_HEADER, 0);
        columnHeaderAndWidth.put(TOTAL_TEST_HEADER, 0);
        columnHeaderAndWidth.put(DURATION_HEADER, 0);
        columnHeaderAndWidth.put(DATE_HEADER, 16);
    }
    
    @Override
	void executeReport() {
    	for(String job : jobs){
            buildList = new ArrayList<JenkinsBuildBean>();
            rw.flushLineBreak();
            gatherData(serverRoot, job);
            writeReport(serverRoot, job);
        }
	}

    private void gatherData(String serverRoot, String job){
        String fullUrl = buildJobUrl(serverRoot, encodeStringForUrl(job));

        JenkinsJobApi jobApi = new JenkinsJobApi(fullUrl);
        int lastCompletedBuild = Integer.parseInt(jobApi.getLastCompletedBuild());
        int startingBuild = lastCompletedBuild - buildLimit + 1;

        for (int currentBuild = startingBuild; currentBuild <= lastCompletedBuild; currentBuild++) {
            String currentBuildNumber = ((Integer) currentBuild).toString();
            JenkinsBuildBean build = new JenkinsBuildBean();
            TestNgResultsJobApi resultsApi = new TestNgResultsJobApi(fullUrl, currentBuildNumber );
            JenkinsBuildApi jobNumApi = new JenkinsBuildApi(fullUrl, currentBuildNumber);

            build.setBuildNumber(currentBuildNumber);
            build.setDuration(jobNumApi.getDuration());
            build.setFailedTestCount(resultsApi.getFailTestCount());
            build.setTotalTestCount(resultsApi.getTotalTestCount());
            build.setTimestamp(rw.convertEpoch(jobNumApi.getTimeStamp()));

            buildList.add(build);
        }
    }
       
    private void writeReport(String serverRoot, String job){
        try{
            rw.writerJobInfo(serverRoot,job);
            rw.writeGenericHeader(columnHeaderAndWidth);

            Collections.reverse(buildList);

            for(JenkinsBuildBean build : buildList){
                writeRow(build);
            }

            rw.writeLineBreak("-");

            writeAverageRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeRow(JenkinsBuildBean build){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        LinkedHashMap<String,String> headerWithRowValue = new LinkedHashMap<String, String>();

        headerWithRowValue.put(BUILD_COLUMN_HEADER, build.getBuildNumber());
        headerWithRowValue.put(FAILED_TEST_HEADER, build.getFailedTestCount());
        headerWithRowValue.put(TOTAL_TEST_HEADER, build.getTotalTestCount());
        headerWithRowValue.put(DURATION_HEADER, rw.convertMilliSecToReadable(build.getDuration()));
        headerWithRowValue.put(DATE_HEADER, dateFormat.format(build.getTimestamp()));

        rw.writeReportRow(headerWithRowValue);
    }

    private void writeAverageRow(){
        LinkedHashMap<String,String> headerWithRowValue = new LinkedHashMap<String, String>();

        headerWithRowValue.put(BUILD_COLUMN_HEADER, "Avg");
        headerWithRowValue.put(FAILED_TEST_HEADER, getAverageFailedTest().toString());
        headerWithRowValue.put(TOTAL_TEST_HEADER, getAverageTotalTest().toString());
        headerWithRowValue.put(DURATION_HEADER, rw.convertMilliSecToReadable(getAverageDuration().toString()));
        headerWithRowValue.put(DATE_HEADER, "Avg");

        rw.writeReportRow(headerWithRowValue);
    }

    private Integer getAverageFailedTest(){
        Integer sum = 0;
        Integer total = 0;
        for (JenkinsBuildBean build : buildList){
            if("-" != build.getFailedTestCount()){
                sum += Integer.parseInt(build.getFailedTestCount());
                total++;
            }
        }
        return sum / total;
    }

    private Integer getAverageTotalTest(){
        Integer sum = 0;
        Integer total = 0;
        for (JenkinsBuildBean build : buildList){
            if("-" != build.getTotalTestCount()){
                sum += Integer.parseInt(build.getTotalTestCount());
                total++;
            }
        }
        return sum / total;
    }

    private Long getAverageDuration(){
        long sum = 0;
        for (JenkinsBuildBean build : buildList){
            sum += Integer.parseInt(build.getDuration());
        }
        return sum / buildList.size();
    }   

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
