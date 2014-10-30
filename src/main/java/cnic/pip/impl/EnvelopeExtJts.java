package cnic.pip.impl;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class EnvelopeExtJts extends EnvelopeExt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8378434653149304262L;

	private static GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
	public EnvelopeExtJts() {
		super();
	}

	public long[][] makeBorderData(String wkt) {
		Geometry geom = null;
		try {
			geom = new WKTReader().read(wkt);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		List<Long> li = new ArrayList<Long>();
		double xs,xe,y;
		for(long j=ny0;j<=ny1;j++){
			Coordinate[] coords = {new Coordinate(x0 ,(j+0.5)/ppd),new Coordinate(x1 ,(j+0.5)/ppd)};
			LineString line = factory.createLineString(coords);
			Geometry g = geom.intersection(line);
			if(g.isEmpty()){
				continue;
			}
			if(g instanceof LineString){
				coords = g.getCoordinates();
				xs = coords[0].x + roundingError;
				xe = coords[1].x - roundingError;
				y = coords[0].y;
				li.add(getIndex(xs,y));
				li.add(getIndex(xe,y));
			} else if(g instanceof MultiLineString){
				for(int i=0;i<g.getNumGeometries();i++){
					Geometry gg = g.getGeometryN(i);
					coords = gg.getCoordinates();
					xs = coords[0].x + roundingError;
					xe = coords[1].x - roundingError;
					y = coords[0].y;
					li.add(getIndex(xs,y));
					li.add(getIndex(xe,y));
				}
			}
		}
		long[][] ret = new long[2][li.size()/2];
		for(int i=0;i<li.size()/2;i++){
			ret[0][i] = li.get(2*i);
			ret[1][i] = li.get(2*i+1);
		}
		return ret;
	}

}
