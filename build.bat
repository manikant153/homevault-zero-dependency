@echo off
if not exist out mkdir out

javac -d out src\homevault\*.java

if %errorlevel% neq 0 (
    echo Build failed.
    exit /b 1
)

echo Build successful.
echo Run application: java -cp out homevault.Main
echo Run tests: java -cp out homevault.TestRunner