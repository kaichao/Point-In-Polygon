#!/bin/bash

ppds=(10 100 1000 10000)
pnts=(10k 100k 1m)
echo '\n' > /tmp/result.txt

for pnt in ${pnts[@]} 
do
  export tb_esri=esri_${pnt}
  echo -n ${tb_esri}: >> /tmp/result.txt
  hive -S -e 'USE tmp_precision;SELECT count(*) FROM ${env:tb_esri};'>> /tmp/result.txt

  for ppd in ${ppds[@]} 
  do
    export tb_cnic=cnic_${pnt}_${ppd}
    echo -n ${tb_cnic}: >> /tmp/result.txt
    hive -S -e 'USE tmp_precision;SELECT count(*) FROM ${env:tb_cnic};'>> /tmp/result.txt

    export tb_joint=joint_${pnt}_${ppd}
    echo -n ${tb_joint}: >> /tmp/result.txt
    hive -S -e 'USE tmp_precision;SELECT count(*) FROM ${env:tb_joint};'>> /tmp/result.txt
<< COMMENT
    export tb_esri_cnic=esri_cnic_${pnt}_${ppd}
    echo -n ${tb_esri_cnic} >> /tmp/result.txt
    hive -S -e 'USE tmp_precision;SELECT count(*) FROM ${env:tb_esri_cnic};'>> /tmp/result.txt
 
    export tb_cnic_esri=cnic_esri_${pnt}_${ppd}
    echo -n ${tb_cnic_esri} >> /tmp/result.txt
    hive -S -e 'USE tmp_precision;SELECT count(*) FROM ${env:tb_cnic_esri};'>> /tmp/result.txt
COMMENT
  done
done
