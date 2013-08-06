package com.commercehub.jenkins.Reports;

import com.commercehub.jenkins.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Author: lzarou
 * Date: 7/17/13
 * Time: 7:03 PM
 */
public class JobHealthReport {
    @Option(name="-url",usage="Sets the root CI url")
    private String serverRoot;

    @Option(name="-job",usage="Sets the CI job")
    private String job;

    @Option(name="-buildLimit",usage="Sets the limit of builds to gather")
    private int buildLimit;

    static ResultsWriter rw = new ResultsWriter();
    ArrayList <JenkinsBuildBean> buildList = new ArrayList<JenkinsBuildBean>(0);

    public static void setFileWriter(String filename){rw.setFileName(filename);}

    public static void main(String[] args){
        new JobHealthReport().doMain(args);
    }

    public void doMain(String[] args){
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);

            if( args.length == 0 ){
                throw new CmdLineException(parser,"No argument is given");
            }

        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("java JobHealthReport [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();

            System.err.println("  Example: java JobHealthReport -url \"http://qatools02:8080\" -job \"Default Trunk - D - Batch - Run 1\" -buildLimit 2");

            return;
        }


        setFileWriter(System.getProperty("user.dir") + "/" + job.replace(" ", "_") + ".txt");
        gatherData();
        writeReport(serverRoot, job);

    }

    private void gatherData(){
        String encodedJob = encodeStringForUrl(job);
        String fullUrl = buildUrl(serverRoot, encodedJob);

        JenkinsJobApi jobApi = new JenkinsJobApi(fullUrl);
        int lastCompletedBuild = Integer.parseInt(jobApi.getLastCompletedBuild());
        int firstBuild = Integer.parseInt(jobApi.getFirstBuild());
        //buildLimit = 2;
        int startingBuild = lastCompletedBuild - buildLimit + 1;



        for (int currentBuild = startingBuild; currentBuild <= lastCompletedBuild; currentBuild++) {
            String currentBuildNumber = ((Integer) currentBuild).toString();
            JenkinsBuildBean build = new JenkinsBuildBean();
            TestNgResultsJobApi resultsApi = new TestNgResultsJobApi(fullUrl, currentBuildNumber );
            JenkinsBuildNumberApi jobNumApi = new JenkinsBuildNumberApi(fullUrl, currentBuildNumber);


            build.setBuildNumber(currentBuildNumber);
            build.setDuration(jobNumApi.getDuration());
            build.setFailedTestCount(resultsApi.getFailTestCount());
            build.setTotalTestCount(resultsApi.getTotalTestCount());
            build.setTime(rw.convertEpoch(jobNumApi.getTimeStamp()));

            buildList.add(build);

        }
    }



    private void writeReport(String serverRoot, String job){
        try{
            rw.writerJobInfo(serverRoot,job);

            rw.writerTableHeader();

            for(int j = buildList.size() - 1; j >= 0; j--){
                rw.writeReportRow(
                        buildList.get(j).getBuildNumber(),
                        buildList.get(j).getFailedTestCount(),
                        buildList.get(j).getTotalTestCount(),
                        buildList.get(j).getDuration(),
                        buildList.get(j).getTime());
            }
            rw.flushFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String encodeStringForUrl(String s){
        try {
            return URLEncoder.encode(s,"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildUrl(String serverRoot, String job){
        return serverRoot + "/job/" + job + "/";
    }
}
