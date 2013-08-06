

java -classpath "./out/production/jenkins-api;lib/*"  com.zarry.jenkins.Reports.JobHealthReport -url "http://qatools02:8080" -job "Default Trunk - D - Batch - Run 1" -buildLimit 10