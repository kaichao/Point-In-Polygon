package cnic.pip.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

import cnic.pip.PolygonMock;

public class ST_GeomFromText extends UDF {
	public PolygonMock evaluate(final String wkt) {
		return new PolygonMock(wkt);
	}
}
