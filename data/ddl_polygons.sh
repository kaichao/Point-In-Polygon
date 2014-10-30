hdfs dfs -mkdir /tmp/pip/data/counties
hdfs dfs -put chmapwkt.txt /tmp/pip/data/counties

hive -e '
  USE pip;
  CREATE TABLE counties( 
    code1 INT,
    name1 STRING, 
    code2 INT,
    name2 STRING, 
    wkt STRING
  ) ROW FORMAT DELIMITED FIELDS TERMINATED BY "|"
    LOCATION "/tmp/pip/data/counties";
'
