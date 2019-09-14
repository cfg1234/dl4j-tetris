#! /bin/sh
cd `dirname $0`
cd ..
ART_ID=`grep artifact pom.xml | head -n 1`
ART_ID=${ART_ID##*<artifactId>}
ART_ID=${ART_ID%%</artifactId>*}
VERSION=`grep version pom.xml | head -n 1`
VERSION=${VERSION##*<version>}
VERSION=${VERSION%%</version>*}
echo `pwd`/target/$ART_ID-$VERSION.jar
