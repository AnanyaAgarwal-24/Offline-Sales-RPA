@echo off
setlocal
cd /d "%~dp0"
if not exist "target\classes" (
  echo Project is not compiled yet.
  echo Run: mvn clean compile
  exit /b 1
)
for /f "delims=" %%i in ('mvn -q dependency:build-classpath -Dmdep.outputFile=target\classpath.txt ^&^& type target\classpath.txt') do set CP=%%i
echo Starting Offline Sales RPA...
java -cp "target\classes;%CP%" com.wipro.rpa.Main "sample-data\SalesData.xls"
endlocal
