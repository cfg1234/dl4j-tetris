#! /bin/sh
cd `dirname $0`
mvn clean package
mvn -DoutputDirectory=`./bin/lib-path.sh` dependency:copy-dependencies
