@echo off
call build.bat
if %errorlevel% neq 0 exit /b 1
java -cp out homevault.Main