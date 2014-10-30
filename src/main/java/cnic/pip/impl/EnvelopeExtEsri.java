package cnic.pip.impl;

import java.util.ArrayList;
import java.util.List;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.core.geometry.ogc.OGCLineString;
import com.esri.core.geometry.ogc.OGCMultiLineString;
import com.esri.core.geometry.ogc.OGCPoint;

public class EnvelopeExtEsri extends EnvelopeExt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5916939340740533962L;

	public EnvelopeExtEsri() {
		super();
	}

	public long[][] makeBorderData(String wkt) {
		OGCGeometry poly = OGCGeometry.fromText(wkt);
		List<Long> li = new ArrayList<Long>();
		double xs,xe,y;

		Envelope env = new Envelope();
		poly.getEsriGeometry().queryEnvelope(env);
		long nys = (long)Math.floor((env.getYMin())*ppd);
		long nye = (long)Math.floor((env.getYMax())*ppd);

		for(long j=nys;j<=nye;j++){
			String ls = String.format("LINESTRING(%s %s,%s %s)", 
					Double.toString(x0),Double.toString((j+0.5)/ppd), 
					Double.toString(x1),Double.toString((j+0.5)/ppd));
			OGCGeometry line = OGCGeometry.fromText(ls);
			OGCGeometry g = poly.intersection(line);
			if(g.isEmpty()){
				continue;
			}
			if(g instanceof OGCLineString){
				OGCPoint s = ((OGCLineString) g).startPoint();
				OGCPoint e = ((OGCLineString) g).endPoint();
				xs = s.X() + roundingError;
				xe = e.X() - roundingError;
				y = s.Y();
				li.add(getIndex(xs,y));
				li.add(getIndex(xe,y));
			} else if(g instanceof OGCMultiLineString){
				OGCMultiLineString ml = (OGCMultiLineString)g;
				for(int i=0;i<ml.numGeometries();i++){
					OGCLineString gg = (OGCLineString)ml.geometryN(i);
					OGCPoint s = gg.startPoint();
					OGCPoint e = gg.endPoint();
					xs = s.X() + roundingError;
					xe = e.X() - roundingError;
					y = s.Y();
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
