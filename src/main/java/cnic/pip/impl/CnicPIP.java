package cnic.pip.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.base.Objects;

import cnic.pip.PointInPolygonOperator;
import cnic.pip.PointMock;
import cnic.pip.PolygonMock;
/**
 * 
 * @author kaichao<kaichao@cnic.cn>
 *
 */
public class CnicPIP implements PointInPolygonOperator {
	EnvelopeExt env;
	private Map<String, long[][]> map = new HashMap<String, long[][]>();
	public boolean contains(PolygonMock polygon, PointMock point) {
		String poly = polygon.toString();
		long[][] borders = map.get(poly);
		if(borders==null){
			String dataFile = String.format("%s/d_%d/f_%d", dataDir,
					Objects.hashCode(env), Objects.hashCode(poly));
			try {
				if(fs.exists(new Path(dataFile))){
					readDataFile(dataFile);
					borders = map.get(poly);
				} else {
					borders = env.makeBorderData(polygon.getWkt());
					writeDataFile(env,poly,borders);
					map.put(poly, borders);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		boolean ret = true;
		try {
			long[] ls = env.getIndexes(point.X(), point.Y());
			for (long n : ls) {
//			for (long n : env.getIndexes(point.X(), point.Y())) {
				int i = Arrays.binarySearch(borders[0], n);
				if (i < 0) {
					i = -i - 2;
					ret = i >= 0 && n <= borders[1][i];
				}
				if (!ret)
					break;
			}
		} catch (OutOfRangeException e) {
			System.out.println(e.getMessage());
			ret = false;
		}
		return ret;
	}

	public CnicPIP(){
		conf = new Configuration();
		try {
			fs = FileSystem.get(URI.create(conf.get("fs.defaultFS")), conf);
			Path path = new Path("/tmp/pip.conf");
			Properties props = new Properties();
			if(fs.exists(path)) {
				props.load(fs.open(path));
			}
			dataDir = props.getProperty("pip.data_dir", "/tmp/index");
			double x0 = Double.parseDouble(props.getProperty("x0","-180"));
			double x1 = Double.parseDouble(props.getProperty("x1","180"));
			double y0 = Double.parseDouble(props.getProperty("y0","-90"));
			double y1 = Double.parseDouble(props.getProperty("y1","90"));
			int ppd = Integer.parseInt(props.getProperty("ppd"));
			env = EnvelopeExt.getInstance(props.getProperty("pip.prepare","esri"));
			env.setParameter(x0, x1, y0, y1, ppd);
		} catch (Exception ex) {
			// default EnvelopeExt
			env = EnvelopeExt.getInstance("esri");
		}
	}
	private Configuration conf;
	private FileSystem fs;
	private String dataDir;
	private void readDataFile(String dataFile){
		try {
			ObjectInputStream in;
			if (dataFile.endsWith(".gz")) {
				in = new ObjectInputStream(new GZIPInputStream(
						fs.open(new Path(dataFile))));
			} else {
				in = new ObjectInputStream(fs.open(new Path(dataFile)));
			}
			EnvelopeExt env = (EnvelopeExt) in.readObject();
			env.equals(this.env);
			String poly = (String) in.readObject();
			long[][] values = (long[][]) in.readObject();
			in.close();
			map.put(poly, values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void writeDataFile(EnvelopeExt env, String poly, long[][] values){
		try {
			// text represntation of EnvelopeExt
			String descFile = String.format("%s/d_%d/envelope.desc", dataDir,
					Objects.hashCode(env), Objects.hashCode(poly));
			if(!fs.exists(new Path(descFile))){
				// desc file not exists, create it
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fs.create(new Path(descFile))));
				bw.write(env.toString());
				bw.close();
			}
			// output current polygon data
			String dataFile = String.format("%s/d_%d/f_%d", dataDir,
					Objects.hashCode(env), Objects.hashCode(poly));
			ObjectOutputStream out = new ObjectOutputStream(
					fs.create(new Path(dataFile)));
			out.writeObject(env);
			out.writeObject(poly);
			out.writeObject(values);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
