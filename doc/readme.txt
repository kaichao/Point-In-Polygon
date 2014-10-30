
High performance implementation of Point-in-Polygon algorithm based on ESRI spatial-framework-hadoop(https://github.com/Esri/spatial-framework-for-hadoop/)
Usage in Hive:
1. copy necessory file to hive client
  scp ~/.m2/repository/com/esri/geometry/esri-geometry-api/1.2/esri-geometry-api-1.2.jar \
    ~/.m2/repository/com/esri/hadoop/spatial-sdk-hive/1.1/spatial-sdk-hive-1.1.jar \
    ~/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar \
    ~/.m2/repository/com/vividsolutions/jts/1.13/jts-1.13.jar \
    h1:/tmp
  scp pip-1.0.0.jar h1:/tmp
2. configure parameters in file pip.conf, and put it to HDFS /tmp/pip.conf
3. preprocess geometry field by using the following hive script
	SELECT ST_Contains(ST_GeomFromText(wkt), ST_point(0, 0)) FROM spatial_table;
4. execute hive scripts in directory src/test/hive
