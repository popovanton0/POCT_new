@echo off

Set folder=C:\ROST_tests

If Exist "%folder%\*.*" goto g
If Not Exist "%folder%\*.*" md C:\ROST_tests
:g ChCp 1251
echo ����� ........
cls
java sample.Main