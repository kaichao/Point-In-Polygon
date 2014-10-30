-- 100k points per degree
SET mapreduce.input.fileinputformat.split.maxsize=8000;

SET yarn.scheduler.minimum-allocation-mb=9536;
SET yarn.scheduler.maximum-allocation-mb=19072;
SET yarn.nodemanager.resource.memory-mb=57344;

SET mapreduce.map.memory.mb=9536;
SET mapreduce.reduce.memory.mb=19072;
SET mapreduce.map.java.opts=-Xmx7636m;
SET mapreduce.reduce.java.opts=-Xmx15272m;

DROP TABLE IF EXISTS tmp_precision.cnic_5w_100k;
CREATE TABLE tmp_precision.cnic_5w_100k AS
  SELECT x, y, code1 
  FROM
  (
    SELECT x, y, code1, hash(wkt) hash
    FROM county_map RIGHT OUTER JOIN points5w
    WHERE ST_Contains(ST_GeomFromText(wkt),ST_Point(x, y))
    DISTRIBUTE BY hash
  ) t1
  DISTRIBUTE BY code1
  SORT BY code1;

