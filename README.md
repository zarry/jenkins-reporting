jenkins-reporting
=================

A simple java framework for writing reports against the Jenkins API

## The Goal

Eventually provide a pretty extendable framework for generating reports based on data gathered from the jenkins api.  

Is it there now?  Absolutely not.  I have really only just begun work on this project.  

The idea formed from a need in my office to gather data regarding the acceptance test phase of our deployment pipeline.  Which we run entirely through Jenkins and in house deployment/automation tools.  The type of data intially sought were job durations, test failure counts, wait times with a goal of indentifing bottlenecks and troubled jobs(read: branches).  I started writing such reports and quickly tired of parsing the dom to find what I was looking for. 

## Roadmap

Write Everything...

To be just a tiny bit more specific.

* ReportWriter needs to be (re)written.  
* More access to the API
* More pre-made/sample reports
* ???
* Profit        