
USE kaichao;

ADD JAR /tmp/guava-18.0.jar /tmp/jts-1.13.jar /tmp/esri-geometry-api-1.2.jar /tmp/spatial-sdk-hive-1.1.jar /tmp/pip-1.0.0.jar ;

CREATE TEMPORARY FUNCTION hive_props as 'cnic.pip.hive.HiveConfProps';

CREATE TEMPORARY FUNCTION ST_Point as 'cnic.pip.hive.ST_Point';
CREATE TEMPORARY FUNCTION ST_GeomFromText as 'cnic.pip.hive.ST_GeomFromText';
CREATE TEMPORARY FUNCTION ST_Contains AS 'cnic.pip.hive.ST_Contains';
CREATE TEMPORARY FUNCTION ST_AsText as 'cnic.pip.hive.ST_AsText';

SET mapreduce.task.timeout=0;

SET mapreduce.input.fileinputformat.split.maxsize=100000;

SET yarn.scheduler.minimum-allocation-mb=2384;
SET yarn.scheduler.maximum-allocation-mb=4768;
SET yarn.nodemanager.resource.memory-mb=57344;

SET mapreduce.map.memory.mb=2384;
SET mapreduce.reduce.memory.mb=4768;
SET mapreduce.map.java.opts=-Xmx1909m;
SET mapreduce.reduce.java.opts=-Xmx3818m;
