@echo off
SET DEVELOPMENT_HOME=../
echo Starting Application - IMDb
cd %DEVELOPMENT_HOME%\
call mvn spring-boot:run
pause;