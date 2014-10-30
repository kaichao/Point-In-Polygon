#!/bin/bash

#ppds=(10 100 1000 10000 100000)
ppds=(100000)
pnts=(10k 100k 1m)

for pnt in ${pnts[@]} 
do
  for ppd in ${ppds[@]} 
  do
    date
    echo 'ppd='${ppd}
    echo 'ppd='${ppd} > /tmp/pip.conf
    hdfs dfs -rm -skipTrash /tmp/pip.conf
    hdfs dfs -put /tmp/pip.conf /tmp
    export tb_cnic=tmp_precision.cnic_${pnt}_${ppd}
    export tb_pnt=points_${pnt}
    hive -e '
      SOURCE ../common/cnic_head.hql;

      SET mapreduce.input.fileinputformat.split.maxsize=100000;
SET yarn.scheduler.minimum-allocation-mb=9536;
SET yarn.scheduler.maximum-allocation-mb=19072;
SET yarn.nodemanager.resource.memory-mb=57344;

SET mapreduce.map.memory.mb=9536;
SET mapreduce.reduce.memory.mb=19072;
SET mapreduce.map.java.opts=-Xmx7636m;
SET mapreduce.reduce.java.opts=-Xmx15272m;

      USE pip;
      CREATE DATABASE IF NOT EXISTS tmp_precision;

      DROP TABLE IF EXISTS tmp_table;
      CREATE TABLE tmp_table AS
        SELECT x, y, code1 AS code, hash(wkt) AS hash
        FROM counties RIGHT OUTER JOIN ${env:tb_pnt} 
        WHERE ST_Contains(ST_GeomFromText(wkt),ST_Point(x, y))
        DISTRIBUTE BY hash
      ;
      SET mapreduce.input.fileinputformat.split.maxsize=64000000;
      DROP TABLE IF EXISTS ${env:tb_cnic};
      CREATE TABLE ${env:tb_cnic} AS
        SELECT x, y, code 
        FROM tmp_table
        DISTRIBUTE BY code
        SORT BY code
      ;
    '
  done
done
date

