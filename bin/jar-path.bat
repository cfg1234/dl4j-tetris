@echo off
%~d0
cd %~dp0
cd ..
setlocal enabledelayedexpansion
for /f "delims=" %%i in ('findstr artifact pom.xml') do (
set aid=%%i
goto out
)
:out
set aid=!aid:^<artifactId^>=!
set aid=!aid:^</artifactId^>=!
set aid=!aid: =!
for /f "delims=" %%i in ('findstr version pom.xml') do (
set ver=%%i
goto out2
)
:out2
set ver=!ver:^<version^>=!
set ver=!ver:^</version^>=!
set ver=!ver: =!
echo target\!aid!-!ver!.jar
