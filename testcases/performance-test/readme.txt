points: 10k,50k,100k,500k,1m,5m,10m
polygons:
	provinces

tests:
	10k,50k,100k		postgis(with-spatial-index/without-spatial-index),esri,cnic
	500k,1m,5m,10m		esri,cnic
	