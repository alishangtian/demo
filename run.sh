#!/bin/bash
git pull origin master
mvn clean package
OLDPID=$(ps aux|grep blogspider-1.0-SNAPSHOT|grep -v grep|awk '{print $2}')
echo "OLDPID:$OLDPID"
kill -9 "$OLDPID"
nohup java -jar ./target/blogspider-1.0-SNAPSHOT.jar &
NEWPID=$(ps aux|grep blogspider-1.0-SNAPSHOT|grep -v grep|awk '{print $2}')
echo "NEWPID:$NEWPID"