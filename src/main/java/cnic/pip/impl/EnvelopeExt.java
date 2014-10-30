package cnic.pip.impl;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * 
 * @author kaichao
 * 
 * 	Parameters for PIP operation
 * 	envelope of the area 
 */
public abstract class EnvelopeExt implements Serializable{
	private static final long serialVersionUID = -3032526397343688842L;

	protected double x0,x1,y0,y1;
	// points per degree
	protected int ppd;
	// tolerance precision
	private static double tolerance = 1e-8;
	protected static double roundingError = 1e-11;
	protected long nx, nx0, nx1, ny0, ny1;
	/**
	 * 
	 * @param x0
	 * @param x1
	 * @param y0
	 * @param y1
	 * @param ppd		resolution, points per degree
	 */
	public EnvelopeExt(double x0, double x1, double y0, double y1, int ppd) {
		setParameter(x0,x1,y0,y1,ppd);
	}
	public EnvelopeExt(double x0, double x1, double y0, double y1) {
		this(x0,x1,y0,y1,10);
	}
	public EnvelopeExt() {
		this(-180,180,-90,90);
	}
	public static EnvelopeExt getInstance(String type){
		if("esri".equals(type)){
			return new EnvelopeExtEsri();
		} else if("esri-v2".equals(type)){
			return new EnvelopeExtEsri_v2();
		} else if("jts".equals(type)){
			return new EnvelopeExtJts();
		} else {
			return null;
		}
	}
	public void setParameter(double x0, double x1, double y0, double y1, int ppd){
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
		this.ppd = ppd;

		nx1 = (int)Math.ceil((x1)*ppd)-1;
		ny1 = (int)Math.ceil((y1)*ppd)-1;
		nx0 = (int)Math.floor((x0)*ppd);
		ny0 = (int)Math.floor((y0)*ppd);
		nx = nx1 - nx0 + 1;
	}
	/**
	 * get indexes of 4 points 
	 * @param x
	 * @param y
	 * @return
	 */
	public long[] getIndexes(double x, double y){
		try {
			idx[0] = getIndex(x - tolerance, y - tolerance);
			idx[1] = getIndex(x - tolerance, y + tolerance);
			idx[2] = getIndex(x + tolerance, y - tolerance);
			idx[3] = getIndex(x + tolerance, y + tolerance);
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
		if(idx[0] == idx[3] && idx[1] == idx[3] && idx[2] == idx[3] ){
			return new long[]{idx[0]};
		} else {
			
		}
		return idx;
	}
	private long[] idx = new long[4];
	public long getIndex(double x, double y){
		long ix = Math.round(x*ppd-0.5);
		long iy = Math.round(y*ppd-0.5);
//		long ix = mathFloor(x*ppd);
//		long iy = mathFloor(y*ppd);
		if(ix<nx0 || ix >= nx1 || iy<ny0 || iy>=ny1){
			throw new OutOfRangeException();
		}
		return ix + iy * nx;
	}
	/*
	 * High performance version of Math.floor(d)
	 */
	public long mathFloor(double d){
		if (d >= 0) {	// positive number
			return (long) d;
		} else if ((long) d == d) {	//negative integer
			return (long) d;
		} else {
			return -((long) (-d) + 1);
		}
	}
	public abstract long[][] makeBorderData(String wkt);
	@Override
	public String toString(){
		return String.format("x0=%s, x1=%s, y0=%s, y1=%s, ppd=%d"
				,Double.toString(x0),Double.toString(x1),Double.toString(y0),Double.toString(y1)
				, ppd);
	}
	@Override
	public boolean equals(Object o){
		EnvelopeExt a = (EnvelopeExt) o;
		return x0==a.x0 && x1==a.x1 && y0==a.y0 && y1==a.y1 && ppd==a.ppd;
	}
	@Override
	public int hashCode(){
		return Objects.hashCode(x0,x1,y0,y1,ppd);
	}
}
