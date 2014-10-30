package cnic.pip;

import com.google.common.base.Objects;

public class PolygonMock extends GeometryMock{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5918068245557302456L;
	private String wkt;
	public PolygonMock(String wkt){
		this.wkt = wkt;
	}
	public String getWkt(){
		return wkt;
	}
	@Override
	public String toString(){
		return wkt;
	}
	@Override
	public boolean equals(Object o){
		PolygonMock a = (PolygonMock) o;
		return wkt.equals(a.wkt);
	}
	@Override
	public int hashCode(){
		return Objects.hashCode(wkt);
	}
}
