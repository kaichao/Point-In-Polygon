package pip;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tukaani.xz.XZInputStream;

import cnic.pip.impl.EnvelopeExt;

public class EnvelopeExtTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	EnvelopeExt ee1,ee2;
	@Before
	public void setUp() throws Exception {
		ee1 = EnvelopeExt.getInstance("esri-v2");
		ee2 = EnvelopeExt.getInstance("esri");
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * test the correctness of the boundary condition added in new version of EnvelopeExt
	 */
	@Test
	public void test() throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new XZInputStream(new FileInputStream(
						"data/polygons/counties.xz"))));
		String line;
		while((line = in.readLine()) != null) {
			System.out.println(line.split("\\|")[0]);
			String wkt = line.split("\\|")[4];
			long[][] l1 = ee1.makeBorderData(wkt);
			long[][] l2 = ee2.makeBorderData(wkt);
			assertTrue(Arrays.deepEquals(l1, l2));
		}
		in.close();
	}

}
