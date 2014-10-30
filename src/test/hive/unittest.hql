
USE kaichao;

SOURCE 'testcases/common/cnic_head.hql';

SELECT ST_AsText(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))')) FROM dual;

--True
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,0)) FROM dual;

--False
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(-3,0)) FROM dual;
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(-2,0)) FROM dual;

--True
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(-1.99999999,0)) FROM dual;
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(1.99999998,0)) FROM dual;

--False
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(2,0)) FROM dual;
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(3,0)) FROM dual;


--False
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,-1.5)) FROM dual;
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,-1)) FROM dual;
--True
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,-0.99999999)) FROM dual;
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,0.99999998)) FROM dual;
--False
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,1)) FROM dual;
SELECT ST_Contains(ST_GeomFromText('MULTIPOLYGON(((-2 -1,-2 1, 2 1, 2 -1, -2 -1)))'),ST_Point(0,1.5)) FROM dual;


SELECT ST_AsText(ST_GeomFromText(wkt)) from counties limit 1;

