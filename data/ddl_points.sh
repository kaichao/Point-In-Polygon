#!/bin/bash
# FORMAT:   point_table,point_file/dir
STR=(points_10k,points_10k.bz2 points_100k,points_100k.bz2 points_1m,points_1m.bz2)

for s in ${STR[@]}
do
  arr=(${s//,/ })
  export pp=/tmp/pip/data/${arr[0]}
  export tb=${arr[0]}
  hdfs dfs -rm -r -skipTrash $pp
  hdfs dfs -mkdir $pp
  hdfs dfs -put ${arr[1]} $pp
  hive -e '
    USE pip;
    DROP TABLE IF EXISTS ${env:tb};
    CREATE TABLE ${env:tb} (
      x DOUBLE,
      y DOUBLE
    ) ROW FORMAT DELIMITED FIELDS TERMINATED BY ","
      LOCATION "${env:pp}";
  '
done
