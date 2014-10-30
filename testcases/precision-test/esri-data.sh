#!/bin/bash

hive -e '
    SOURCE ../common/esri_head.hql;

  SET mapreduce.input.fileinputformat.split.maxsize=8000;

  USE pip;
  CREATE DATABASE IF NOT EXISTS tmp_precision;

  DROP TABLE IF EXISTS tmp_precision.esri_10k;
  CREATE TABLE tmp_precision.esri_10k AS
    SELECT x, y, code1 AS code
    FROM counties RIGHT OUTER JOIN points_10k
    WHERE ST_Contains(ST_GeomFromText(wkt),ST_Point(x, y))
    DISTRIBUTE BY code
    SORT BY code;

  DROP TABLE IF EXISTS tmp_precision.esri_100k;
  CREATE TABLE tmp_precision.esri_100k AS
    SELECT x, y, code1 AS code
    FROM counties RIGHT OUTER JOIN points_100k
    WHERE ST_Contains(ST_GeomFromText(wkt),ST_Point(x, y))
    DISTRIBUTE BY code
    SORT BY code;

  DROP TABLE IF EXISTS tmp_precision.esri_1m;
  CREATE TABLE tmp_precision.esri_1m AS
    SELECT x, y, code1 AS code
    FROM counties RIGHT OUTER JOIN points_1m
    WHERE ST_Contains(ST_GeomFromText(wkt),ST_Point(x, y))
    DISTRIBUTE BY code
    SORT BY code;

  DROP TABLE IF EXISTS tmp_precision.esri_10m;
  CREATE TABLE tmp_precision.esri_10m AS
    SELECT x, y, code1 AS code
    FROM counties RIGHT OUTER JOIN points_10m
    WHERE ST_Contains(ST_GeomFromText(wkt),ST_Point(x, y))
    DISTRIBUTE BY code
    SORT BY code;
'