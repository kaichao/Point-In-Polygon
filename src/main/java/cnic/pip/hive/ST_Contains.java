package cnic.pip.hive;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.udf.UDFType;

import cnic.pip.PointMock;
import cnic.pip.PolygonMock;
import cnic.pip.impl.CnicPIP;

@UDFType(deterministic = true)
@Description(
		name = "ST_Contains",
		value = "_FUNC_(geometry1, geometry2) - return true if geometry1 contains geometry2",
		extended = "Example:\n" + 
		"SELECT _FUNC_(st_polygon(1,1, 1,4, 4,4, 4,1), st_point(2, 3) from src LIMIT 1;  -- return true\n" + 
		"SELECT _FUNC_(st_polygon(1,1, 1,4, 4,4, 4,1), st_point(8, 8) from src LIMIT 1;  -- return false"	
		)

public final class ST_Contains extends UDF {

	private CnicPIP pip;
	public ST_Contains(){
		pip = new CnicPIP();
	}
	public Boolean evaluate(final PolygonMock poly, final PointMock point) {
		return pip.contains(poly, point);
	}
}