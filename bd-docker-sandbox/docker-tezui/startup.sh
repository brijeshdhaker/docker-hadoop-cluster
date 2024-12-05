#!/bin/bash


# java -jar jetty-runner.jar \
#   --lib ~/src/tools/derby/ --lib ~/src/tools/atomikos  \
#   --jdbc org.apache.derby.jdbc.EmbeddedXADataSource "databaseName=testdb;createDatabase=create" "jdbc/mydatasource" \
#   my.war

java -jar /opt/tez/jetty-runner-${JETTY_VERSION}.jar --port 9999 /opt/tez/tez-ui-${TEZ_VERSION}.war


