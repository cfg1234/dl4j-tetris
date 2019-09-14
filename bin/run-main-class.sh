#! /bin/sh
cd `dirname $0`
cd ..
CLASSPATH=`bin/jar-path.sh`
for jar in `bin/lib-path.sh`/*
do
  CLASSPATH=$CLASSPATH:$jar
done
CLASS=$1
java -cp "$CLASSPATH" $CLASS "$@"
