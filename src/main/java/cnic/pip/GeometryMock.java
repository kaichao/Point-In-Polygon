package cnic.pip;

import java.io.Serializable;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.ogc.OGCGeometry;

public abstract class GeometryMock extends OGCGeometry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3649049135489891509L;

	@Override
	public String geometryType() {
		return null;
	}

	@Override
	public OGCGeometry boundary() {
		return null;
	}

	@Override
	public OGCGeometry locateAlong(double mValue) {
		return null;
	}

	@Override
	public OGCGeometry locateBetween(double mStart, double mEnd) {
		return null;
	}

	@Override
	public Geometry getEsriGeometry() {
		return null;
	}

}
