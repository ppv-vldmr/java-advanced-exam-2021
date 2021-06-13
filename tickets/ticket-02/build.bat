@echo off
set classpath=out
javac -d %classpath% src\Server.java src\Client.java src\StringServer.java
start "" "%java_home%\bin\rmiregistry"