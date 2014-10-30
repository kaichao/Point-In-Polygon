#!/bin/bash

ppds=(10 100 1000 10000 100000)

for ppd in ${ppds[@]}
do
  date
  echo 'ppd='${ppd}
  echo 'ppd='${ppd} > /tmp/pip.conf
  hdfs dfs -rm -skipTrash /tmp/pip.conf
  hdfs dfs -put /tmp/pip.conf /tmp
  hive -S -e '
    SOURCE cnic_head.hql;
    USE pip;
    DROP TABLE IF EXISTS tmp_table;
    CREATE TABLE tmp_table AS
      SELECT ST_Contains(ST_GeomFromText(wkt), ST_point(0, 0)) FROM counties;
    DROP TABLE IF EXISTS tmp_table;
  '
done
date

