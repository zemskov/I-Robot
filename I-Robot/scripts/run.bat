@ECHO OFF

mkdir C:\mongodb\data

start C:\mongodb\bin\mongod -dbpath=C:\mongodb\data



SETLOCAL ENABLEDELAYEDEXPANSION

SET ALL_JARS=

cd lib
FOR /R %%f IN (*.jar) DO (
   SET ALL_JARS=%%f;!ALL_JARS!
)
cd ..



start java -classpath "%ALL_JARS%"  -Xmx512m com.tolmachev.lucy.Main

