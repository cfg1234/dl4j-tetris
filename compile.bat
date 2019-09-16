@echo off
%~d0
cd %~dp0
call mvn clean package
setlocal enabledelayedexpansion
for /f "delims=" %%i in ('call bin\lib-path.bat') do (
set libpath=%%i
)
call mvn -DoutputDirectory=!libpath! dependency:copy-dependencies
pause