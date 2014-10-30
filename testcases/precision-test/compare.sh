#!/bin/bash

ppds=(10 100 1000 10000 100000)
pnts=(10k 100k 1m)
for pnt in ${pnts[@]} 
do
  export tb_esri=esri_${pnt}
  for ppd in ${ppds[@]} 
  do
    export tb_cnic=cnic_${pnt}_${ppd}
    export tb_joint=joint_${pnt}_${ppd}
    export tb_esri_cnic=esri_cnic_${pnt}_${ppd}
    export tb_cnic_esri=cnic_esri_${pnt}_${ppd}
    hive -e '
      USE tmp_precision;
      DROP TABLE IF EXISTS ${env:tb_joint};
      DROP TABLE IF EXISTS ${env:tb_esri_cnic};
      DROP TABLE IF EXISTS ${env:tb_cnic_esri};
-- INTERSECT
      CREATE TABLE ${env:tb_joint} AS
        SELECT ${env:tb_esri}.*
        FROM ${env:tb_esri} INNER JOIN ${env:tb_cnic} 
          ON ${env:tb_esri}.x = ${env:tb_cnic}.x 
          AND ${env:tb_esri}.y = ${env:tb_cnic}.y 
          AND ${env:tb_esri}.code = ${env:tb_cnic}.code
      ;
-- esri MINUS cnic
      CREATE TABLE ${env:tb_esri_cnic} AS
        SELECT ${env:tb_esri}.*
        FROM ${env:tb_esri} LEFT OUTER JOIN ${env:tb_cnic} 
          ON ${env:tb_esri}.x = ${env:tb_cnic}.x 
          AND ${env:tb_esri}.y = ${env:tb_cnic}.y 
          AND ${env:tb_esri}.code = ${env:tb_cnic}.code
        WHERE ${env:tb_cnic}.x IS NULL
      ;
-- cnic MINUS esri
      CREATE TABLE ${env:tb_cnic_esri} AS
        SELECT ${env:tb_cnic}.*
        FROM ${env:tb_cnic} LEFT OUTER JOIN ${env:tb_esri} 
          ON ${env:tb_esri}.x = ${env:tb_cnic}.x 
          AND ${env:tb_esri}.y = ${env:tb_cnic}.y 
          AND ${env:tb_esri}.code = ${env:tb_cnic}.code
        WHERE ${env:tb_esri}.x IS NULL
      ;
    '
  done
done
