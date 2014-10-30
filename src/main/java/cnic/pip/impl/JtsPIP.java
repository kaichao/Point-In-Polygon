package cnic.pip.impl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import cnic.pip.PointInPolygonOperator;
import cnic.pip.PointMock;
import cnic.pip.PolygonMock;

public class JtsPIP implements PointInPolygonOperator {

	private static GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
	public boolean contains(PolygonMock poly, PointMock point) {
		try {
			Geometry geom = new WKTReader().read(poly.toString());
			Point p = factory.createPoint(new Coordinate(point.X(), point.Y()));
			return geom.contains(p);
		} catch (ParseException e) {
			return false;
		}
	}

}
