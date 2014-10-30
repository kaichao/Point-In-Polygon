USE kaichao;

ADD JAR /tmp/esri-geometry-api-1.2.jar /tmp/spatial-sdk-hive-1.1.jar;

SET mapreduce.task.timeout=0;

SET mapreduce.input.fileinputformat.split.maxsize=8000;

SET yarn.scheduler.minimum-allocation-mb=2384;
SET yarn.scheduler.maximum-allocation-mb=4768;
SET yarn.nodemanager.resource.memory-mb=57344;

SET mapreduce.map.memory.mb=2384;
SET mapreduce.reduce.memory.mb=4768;
SET mapreduce.map.java.opts=-Xmx1909m;
SET mapreduce.reduce.java.opts=-Xmx3818m;

create temporary function ST_Point as 'com.esri.hadoop.hive.ST_Point';
create temporary function ST_GeomFromText as 'com.esri.hadoop.hive.ST_GeomFromText';
create temporary function ST_AsText as 'com.esri.hadoop.hive.ST_AsText';
create temporary function ST_Contains as 'com.esri.hadoop.hive.ST_Contains';

