@echo off
if not exist out mkdir out
javac -d out src\homevault\*.java
if %errorlevel% neq 0 (
    echo Build failed.
    exit /b 1
)
echo Build successful.