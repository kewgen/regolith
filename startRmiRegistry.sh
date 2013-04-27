#!/bin/sh
export CLASSPATH=out\production\common;out\production\server;release\server.jar;release\mainserver.jar;release\common.jar;
echo "Starting rmiregistry"
rmiregistry 
sleep 5
echo "rmiregistry was started"

