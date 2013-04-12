@echo OFF
if "%1" EQU "" GOTO LABEL
cmd /c ant -Dlogin.name=%1 run.client
exit
:LABEL
@echo need to pass a login name