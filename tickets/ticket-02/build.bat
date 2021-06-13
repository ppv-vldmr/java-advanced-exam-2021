@echo off
set classpath=out
javac -d %classpath% src\Manager.java src\Client.java src\StringManager.java
start "" "%java_home%\bin\rmiregistry"