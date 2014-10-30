package cnic.pip.impl;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.core.geometry.ogc.OGCMultiPolygon;
import com.esri.core.geometry.ogc.OGCPoint;

import cnic.pip.PointInPolygonOperator;
import cnic.pip.PointMock;
import cnic.pip.PolygonMock;

public class EsriPIP implements PointInPolygonOperator {
	private static SpatialReference sr = SpatialReference.create(4326);
	public boolean contains(PolygonMock polygon, PointMock point) {
		OGCPoint pt = new OGCPoint(new Point(point.X(),point.Y()), sr);
		OGCMultiPolygon poly = (OGCMultiPolygon)OGCGeometry.fromText(
				polygon.getWkt());
		return poly.contains(pt);
	}

}
