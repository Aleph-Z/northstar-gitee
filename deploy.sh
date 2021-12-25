#!/bin/bash
kill `pgrep -a java | grep northstar.jar | awk '{print $1}'`

cd ~/northstar
~/apache-maven-3.6.3/bin/mvn clean install -Dmaven.test.skip=true
\mv -f northstar-main/target/northstar-main*.jar ~/northstar.jar

cd ~
nohup java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xlog:gc+heap=debug:gc.log -Xmn1g -Xmx1g  -jar northstar.jar >ns.log &
