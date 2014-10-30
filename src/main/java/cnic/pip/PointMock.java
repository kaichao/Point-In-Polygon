package cnic.pip;

import com.google.common.base.Objects;

public class PointMock extends GeometryMock {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2902654195120653340L;
	private double x,y;
	public PointMock(double x, double y){
		this.x = x;
		this.y = y;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(x,y);
	}
	@Override
	public boolean equals(Object obj) {
		PointMock p = (PointMock) obj;
		return x==p.x && y == p.y;
	}
	@Override
	public String toString() {
		return "PointMock [x=" + x + ", y=" + y + "]";
	}

	public double X() {
		return x;
	}
	public double Y() {
		return y;
	}
}
