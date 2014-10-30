package cnic.pip.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

import cnic.pip.PointMock;

public class ST_Point extends UDF {
	public PointMock evaluate(final double x, final double y) {
		return new PointMock(x, y);
	}
}
