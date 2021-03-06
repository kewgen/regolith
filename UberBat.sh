#!/bin/bash
echo "Starting rmiregistry"
export CLASSPATH=$PATH:out/production/common:out/production/server:release/server.jar:release/mainserver.jar:release/common.jar
echo $CLASSPATH
sleep 2
$JAVA_HOME/bin/rmiregistry &
sleep 2
echo "Starting main server"
ant run.main.server
sleep 5
echo "Starting main battle"
ant run.battle.server 
name1=1
name2=2
sleep 5
echo "Starting clients"
ant -Dlogin.name=$name1 run.client
ant -Dlogin.name=$name2 run.client