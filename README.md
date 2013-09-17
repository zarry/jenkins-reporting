jenkins-reporting
=================

A simple java framework for writing reports against the Jenkins API

## The Goal

Eventually provide a pretty extendable framework for generating reports based on data gathered from the jenkins api.  

Is it there now?  Absolutely not.  I have really only just begun work on this project.  

The idea formed from a need in my office to gather data regarding the acceptance test phase of our deployment pipeline.  Which we run entirely through Jenkins and in house deployment/automation tools.  The type of data intially sought were job durations, test failure counts, wait times with a goal of indentifing bottlenecks and troubled jobs(read: branches).  I started writing such reports and quickly tired of parsing the dom to find what I was looking for. 

## Building with dependencies

* mvn install

## Roadmap

Write Everything...

To be just a tiny bit more specific.

* Documentation
* ReportWriter needs to be (re)written.  
* More access to the API
* More pre-made/sample reports
* ???
* Profit


## Running A Report

JobHealthReport.bat -url `<url>` -job `<jobName>` -buildLimit `<maxBuildLimit>`


```
> JobHealthReport.bat -url "http://jenkinsci:8080" -job "Default Trunk - Run 1" -buildLimit 10

```

JobDurationReport.bat -url `<url>` -regex `<regex>`


```
> JobDurationReport.bat -url "http://jenkinsci:8080" -regex ".*VM Reserve.*" 

```

BranchComparisonReport.bat -url `<url>` -baseJob `<jobName>` -testJob `<jobName>`

```
> BranchComparisonReport.bat -url "http://jenkinsci:8080" -baseJob "Default Trunk - Run 1" -testJob "AugustStaging - Run 1"

```

## Sample Report Output

### JobHealthReport

A report document duration, testng test totals and failure counts along with relevant build and date information.

```
Report Time:   2013/8/13  1:51 AM
Jenkins CI:    http://jenkinsci:8080
Job:           Default Trunk - Run 1

|  Build  |  Failed Test Count  |  Total Test Count  |  Total Duration  |      Date      |
+---------+---------------------+--------------------+------------------+----------------
    62              45                   2151              4 h 6 m          08/07/2013
    61              51                   2151              4 h 53 m         08/06/2013
    60              27                   2150              3 h 47 m         08/05/2013
    59              33                   2150              3 h 2 m          08/04/2013
    58              29                   2150              3 h 8 m          08/03/2013
    57              26                   2150              3 h 5 m          08/02/2013
    56              39                   2150              3 h 18 m         08/01/2013
    55              25                   2149              3 h 12 m         07/31/2013
    54               -                    -                2 h 2 m          07/31/2013
    53              23                   2149              3 h 7 m          07/30/2013
-----------------------------------------------------------------------------------------
    Avg             33                   2150              3 h 22 m            Avg
 ```        
 
### JobDurationReport

A report to match all jobs based on regex provided and calculate average duration for all builds for the given job, then total average duration for all jobs and builds which matched.

```

|                             Job                             |  Avg Duration  |
+-------------------------------------------------------------+----------------+
                Build - A - VM Reserve                              1 h 8 m     
                Performance_Build - A VM Reserve                    3 h 44 m    
                 Default Trunk - A - VM Reserve                     2 h 52 m    
                   Cache - A - VM Reserve                           3 h 5 m     
              Testcase_Stability - A - VM Reserve                   1 h 3 m     
                Maintenance - A - VM Reserve                        3 h 13 m    
             Locator-enhancements - A - VM Reserve                  0 h 12 m    
            WebDM_LineItemDataType - A - VM Reserve                 7 h 45 m    
             RemoveInvoicePage - A - VM Reserve                     0 h 0 m     
              Maintenance_Weekly - A - VM Reserve                   0 h 54 m    
                 Offshore Team - A - VM Reserve                     1 h 5 m     
            QA_Sandbox_Conversions - A - VM Reserve                 1 h 9 m     
---------------------------------------------------------------------------------
              All Jobs and Builds Average Duration                  1 h 23 m    


```

 
### BranchComparisonReport

The BranchComparisonReport provides a list of test classes that are failing within the 'testJob' job that are not 
failing within the 'baseJob' job. This would typically be used to compare a specific workstream to the default
workstream to only provide those tests that are not failing globally.

```

Tests failing in 'AugustStaging - Run 1' that are not failing in 'Default Trunk - Run 1':
        tests.specTest
        tests.confirmTest

```
