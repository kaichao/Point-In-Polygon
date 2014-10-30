package pip;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(Math.getExponent(100.2));
		System.out.println(Math.getExponent(1025));
	}
//	@Test
	public void testMathFloorPerformance() {
		String fmt = "sum is %f, time is %d ms\n";
		long t0,t1;
		double sum;

		t0 = System.currentTimeMillis();
		sum = 0;
		for(double d= 0;d<100;d+=0.000001){
			sum += Math.floor(d);
		}
		t1 = System.currentTimeMillis();
		System.out.format(fmt, sum, (t1-t0) );

		t0 = System.currentTimeMillis();
		sum = 0;
		for(double d= 0;d<100;d+=0.000001){
			sum += mathFloor(d);
		}
		t1 = System.currentTimeMillis();
		System.out.format(fmt, sum, (t1-t0) );

		t0 = System.currentTimeMillis();
		sum = 0;
		for(double d= 0;d<100;d+=0.000001){
			sum += d;
		}
		t1 = System.currentTimeMillis();
		System.out.format(fmt, sum, (t1-t0) );
		
	}
//	@Test
	public void testMathFloorFunction() {
		assertEquals(Math.floor(1.6),mathFloor(1.6),1e-11);
		assertEquals(Math.floor(1.4),mathFloor(1.4),1e-11);
		assertEquals(Math.floor(1.0),mathFloor(1.0),1e-11);
		assertEquals(Math.floor(-1.0),mathFloor(-1.0),1e-11);
		assertEquals(Math.floor(-1.4),mathFloor(-1.4),1e-11);
		assertEquals(Math.floor(-1.6),mathFloor(-1.6),1e-11);
		for(double d= -100;d<100;d+=0.0001){
			assertEquals(Math.floor(d),mathFloor(d),1e-14);
		}

	}
	private double mathFloor(double d){
		if (d >= 0) {	// positive number
			return (long) d;
		} else if ((long) d == d) {	//negative integer
			return (long) d;
		} else {
			return -((long) (-d) + 1);
		}
	}
	@Test
	public void testGetMathFloorFunction() {
		assertEquals(getMathFloor(1.000001),2l);
		assertEquals(getMathFloor(1.00000000001),1l);
		assertEquals(getMathFloor(0.99999999999),1l);
		assertEquals(getMathFloor(0.999999),0l);
		assertEquals(getMathFloor(0.00000000001),0l);
		assertEquals(getMathFloor(0),0l);
		assertEquals(getMathFloor(-0.00000000001),0l);
		assertEquals(getMathFloor(-0.99999999999),-1l);
		assertEquals(getMathFloor(-1.00000000001),-1l);
	}
	/*
	 * rounding error
	 */
	private long getMathFloor(double d){
		
		if (d >= 0) {	// positive number
			return (long) d;
		} else if ((long) d == d) {	//negative integer
			return (long) d;
		} else {
			return -((long) (-d) + 1);
		}
	}
	
	@Test
	public void testDedup() {
		long[] a1 = {1,2,3,4};
		assertEquals(dedup(a1).length,4);

		long[] a2 = {1,1,3,4};
		assertEquals(dedup(a2).length,3);
		long[] a3 = {1,2,2,4};
		assertEquals(dedup(a3).length,3);
		long[] a4 = {1,2,3,3};
		assertEquals(dedup(a4).length,3);
		long[] a5 = {1,1,1,4};
		assertEquals(dedup(a5).length,2);
		long[] a6 = {1,2,2,2};
		assertEquals(dedup(a6).length,2);
		long[] a7 = {1,2,1,1};
		assertEquals(dedup(a7).length,2);
		long[] a8 = {1,1,1,1};
		assertEquals(dedup(a8).length,1);
		System.out.println(Arrays.toString(dedup(a1)));
		System.out.println(Arrays.toString(dedup(a2)));
		System.out.println(Arrays.toString(dedup(a3)));
		System.out.println(Arrays.toString(dedup(a4)));
		System.out.println(Arrays.toString(dedup(a5)));
		System.out.println(Arrays.toString(dedup(a6)));
		System.out.println(Arrays.toString(dedup(a7)));
		System.out.println(Arrays.toString(dedup(a8)));
	}
	// deduplicate elements in an array
	private long[] dedup(long[] idx){
		int n = idx.length;
		for(int i=0;i<n-1;i++){
			for(int j=i+1;j<n;j++){
				if(idx[i] == idx[j]){
					for(int k=i+1;k<n;k++){
						idx[k-1] = idx[k];
					}
					n--;
					i--;
					break;
				}
			}
		}
		return Arrays.copyOf(idx, n);
	}

}
