package cnic.pip.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

import cnic.pip.PointMock;
import cnic.pip.PolygonMock;

public class ST_AsText extends UDF {
	public String evaluate(final PolygonMock geom) {
		return geom.toString();
	}
	public String evaluate(final PointMock geom) {
		return geom.toString();
	}
}
