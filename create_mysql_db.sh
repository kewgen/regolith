#!/bin/bash
SUPERUSER="root"
SUPERPASSWORD="root"

DATABASE="regolith"
CHAR="utf8"

QUERY="drop database if exists $DATABASE;create database $DATABASE character set $CHAR;"

mysql --user=$SUPERUSER --password=$SUPERPASSWORD -e "$QUERY"