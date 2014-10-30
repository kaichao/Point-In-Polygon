package cnic.pip.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.core.geometry.ogc.OGCLineString;
import com.esri.core.geometry.ogc.OGCMultiLineString;
import com.esri.core.geometry.ogc.OGCPoint;

import com.google.common.base.Objects;

/**
 * 
 * @author kaichao
 * 
 * 	Parameters for PIP operation
 * 	envelope of the area 
 */
public class EnvelopeExtEsri_v1 implements Serializable{
	private static final long serialVersionUID = -3032526397343688842L;

	private double x0,x1,y0,y1;
	// points per degree
	private int ppd;
	private double epsilon;
	private long nx, nx0, nx1, ny0, ny1;
	/**
	 * 
	 * @param x0
	 * @param x1
	 * @param y0
	 * @param y1
	 * @param ppd		resolution, points per degree
	 * @param epsilon	precision, default value is max((y1-y0),(x1-x0)) * epsilon
	 */
	public EnvelopeExtEsri_v1(double x0, double x1, double y0, double y1, int ppd, double epsilon) {
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
		this.ppd = ppd;
		this.epsilon = epsilon;
//		this.epsilon = Math.max(x1-x0, y1-y0) * epsilon;
		nx1 = (int)Math.ceil((x1)*ppd)-1;
		ny1 = (int)Math.ceil((y1)*ppd)-1;
		nx0 = (int)Math.floor((x0)*ppd);
		ny0 = (int)Math.floor((y0)*ppd);
		nx = nx1 - nx0 + 1;
	}
	public EnvelopeExtEsri_v1(double x0, double x1, double y0, double y1, int ppd) {
		this(x0,x1,y0,y1,ppd,1e-7);
	}
	public EnvelopeExtEsri_v1(double x0, double x1, double y0, double y1) {
		this(x0,x1,y0,y1,100);
	}
	public EnvelopeExtEsri_v1() {
		this(-180,180,-90,90);
	}

	/**
	 * get indexes of 4 points 
	 * @param x
	 * @param y
	 * @return
	 */
	public long[] getIndexes(double x, double y){
		try {
			idx[0] = getIndex(x - epsilon, y - epsilon);
			idx[1] = getIndex(x - epsilon, y + epsilon);
			idx[2] = getIndex(x + epsilon, y - epsilon);
			idx[3] = getIndex(x + epsilon, y + epsilon);
		} catch (OutOfRangeException ex) {
			String msg = String.format("out of range:x=%s,y=%s",
					Double.toString(x), Double.toString(y));
			throw new OutOfRangeException(msg);
		}
		// delete duplicated elements
//		int n = 4;
//		for(int i=0;i<n-1;i++){
//			for(int j=i+1;j<n;j++){
//				if(idx[i] == idx[j]){
//					for(int k=i+1;k<n;k++){
//						idx[k-1] = idx[k];
//					}
//					n--,i--;
//					break;
//				}
//			}
//		}
//		return Arrays.copyOf(idx, n);
		if(idx[0] == idx[1] && idx[0] == idx[2] && idx[0] == idx[3] ){
			return new long[]{idx[0]};
		}
		return idx;
	}
	private long[] idx = new long[4];
	public long getIndex(double x, double y){
//		long ix = (long)Math.floor(x*ppd);
//		long iy = (long)Math.floor(y*ppd);
		long ix = getMathFloor(x*ppd);
		long iy = getMathFloor(y*ppd);
		if(ix<nx0 || ix >= nx1 || iy<ny0 || iy>=ny1){
			throw new OutOfRangeException();
		}
		return ix + iy * nx;
	}
	/*
	 * High performance version of Math.floor(double)
	 * with rounding error correction
	 */
//	private static double roundingError = 1e-14;
	public long getMathFloor(double d){
		if (d >= 0) {	// positive number
			return (long) d;
		} else if ((long) d == d) {	//negative integer
			return (long) d;
		} else {
			return -((long) (-d) + 1);
		}
	}

	public long[][] makeBorderData(String wkt) {
		OGCGeometry poly = OGCGeometry.fromText(wkt);
		List<Long> li = new ArrayList<Long>();
		for(long j=ny0;j<=ny1;j++){
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
				long i0 = getIndex(s.X(), s.Y());
				long i1 = getIndex(e.X(), e.Y());
				if(i0 < i1){
					li.add(i0);
					li.add(i1);
				} else {
					li.add(i1);
					li.add(i0);
				}
			} else if(g instanceof OGCMultiLineString){
				OGCMultiLineString ml = (OGCMultiLineString)g;
				for(int i=0;i<ml.numGeometries();i++){
					OGCLineString gg = (OGCLineString)ml.geometryN(i);
					OGCPoint s = gg.startPoint();
					OGCPoint e = gg.endPoint();
					long i0 = getIndex(s.X(), s.Y());
					long i1 = getIndex(e.X(), e.Y());
					if(i0 < i1){
						li.add(i0);
						li.add(i1);
					} else {
						li.add(i1);
						li.add(i0);
					}
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
	@Override
	public String toString(){
		return String.format("x0=%s, x1=%s, y0=%s, y1=%s, ppd=%d, epsilon=%s"
				,Double.toString(x0),Double.toString(x1),Double.toString(y0),Double.toString(y1)
				, ppd, Double.toString(epsilon));
	}
	@Override
	public boolean equals(Object o){
		EnvelopeExtEsri_v1 a = (EnvelopeExtEsri_v1) o;
		return x0==a.x0 && x1==a.x1 && y0==a.y0 && y1==a.y1 && ppd==a.ppd && epsilon==a.epsilon;
	}
	@Override
	public int hashCode(){
		return Objects.hashCode(x0,x1,y0,y1,ppd);
	}
}
