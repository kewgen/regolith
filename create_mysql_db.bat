set SUPERUSER=root
set SUPERPASSWORD=root

set DATABASE=regolith
set CHAR=utf8

set QUERY=drop database if exists %DATABASE%; create database %DATABASE% character set %CHAR%;

mysql --user=%SUPERUSER% --password=%SUPERPASSWORD% -e "%QUERY%"
