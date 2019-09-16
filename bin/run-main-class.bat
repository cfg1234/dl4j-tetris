@echo off
%~d0
cd %~dp0
cd ..
setlocal enabledelayedexpansion
for /f "delims=" %%i in ('call bin\jar-path.bat') do (
set CLASSPATH=%%i
)
for /f "delims=" %%i in ('call bin\lib-path.bat') do (
set libpath=%%i
)
for /f "delims=" %%i in ('call dir /B !libpath!') do (
set CLASSPATH=!libpath!\%%i;!CLASSPATH!
)
set CLASS=%~1
call java -cp !CLASSPATH! !CLASS!
