@echo off
rem Задаем имена клиентов
set name1="1"
set name2="2"

rem Запуск rmiregistry
set classpath=out\production\common;out\production\server;release\server.jar;release\mainserver.jar;release\common.jar;
start cmd /k rmiregistry
timeout /t 2 /nobreak

rem Запуск Main Server
start cmd /c ant run.main.server
timeout /t 5 /nobreak

rem Запуск Battle server
start cmd /c ant run.battle.server
timeout /t 5 /nobreak

rem Запуск клиентов
start cmd /c ant -Dlogin.name=%name1% run.client1
start cmd /c ant -Dlogin.name=%name2% run.client2