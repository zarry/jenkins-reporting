package com.zarry.jenkins.Reports;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author Brett Duclos
 */
public abstract class AbstractJenkinsReport {

	protected void runReport(String[] args){
		parseArguments(args);
		setUp();
		executeReport();
		tearDown();
	}
	
	protected String encodeStringForUrl(String s){
        try {
            return URLEncoder.encode(s,"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String buildJobUrl(String serverRoot, String job){
        return serverRoot + "/job/" + job + "/";
    }
    
    protected void parseArguments(String[] args){
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);

            if( args.length == 0 ){
                throw new CmdLineException(parser,"No argument is given");
            }
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("java " + getReportName() + " [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println(getUsageMessage());            
            return;
        }
    }
		
    abstract String getReportName();
    abstract String getUsageMessage();
	abstract void setUp();
	abstract void executeReport();
	abstract void tearDown();	
	
}
