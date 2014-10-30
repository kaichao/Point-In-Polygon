SET mapreduce.input.fileinputformat.split.maxsize=100000;

SET yarn.scheduler.minimum-allocation-mb=2384;
SET yarn.scheduler.maximum-allocation-mb=4768;
SET yarn.nodemanager.resource.memory-mb=57344;

SET mapreduce.map.memory.mb=2384;
SET mapreduce.reduce.memory.mb=4768;
SET mapreduce.map.java.opts=-Xmx1909m;
SET mapreduce.reduce.java.opts=-Xmx3818m;

CREATE TABLE tmp_001 AS
SELECT county_map.name1,count(*) AS cnt 
FROM county_map
  JOIN points5w
WHERE ST_Contains(ST_GeomFromText(county_map.wkt),ST_Point(points5w.x, points5w.y))
DISTRIBUTE BY county_map.wkt
GROUP BY county_map.name1
SORT BY cnt desc;

SET yarn.app.mapreduce.am.resource.mb=4768;
SET yarn.app.mapreduce.am.command-opts=-Xmx3818m;

SET yarn.scheduler.minimum-allocation-mb=2560;
SET yarn.scheduler.maximum-allocation-mb=5120;
SET yarn.nodemanager.resource.memory-mb=57344;
SET yarn.nodemanager.resource.cpu-vcores=22;
SET mapreduce.map.memory.mb=3072;
SET mapreduce.reduce.memory.mb=6144;
SET mapreduce.map.java.opts=-Xmx5120m;
SET mapreduce.reduce.java.opts=-Xmx10240m;

SET yarn.nodemanager.container-manager.thread-count=40;

SET yarn.scheduler.minimum-allocation-mb=2048;
SET yarn.scheduler.maximum-allocation-mb=3840;
SET yarn.nodemanager.resource.memory-mb=61440;
SET yarn.nodemanager.resource.cpu-vcores=22;
SET yarn.nodemanager.vmem-pmem-ratio=2.1;
SET mapreduce.map.memory.mb=2560;
SET mapreduce.reduce.memory.mb=5120;
SET mapreduce.map.java.opts=-Xmx3840m;
SET mapreduce.reduce.java.opts=-Xmx7680m;

