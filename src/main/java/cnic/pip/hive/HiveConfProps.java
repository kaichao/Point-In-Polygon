package cnic.pip.hive;

import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.exec.UDF;

public class HiveConfProps extends UDF {
	public String evaluate(){
		HiveConf conf = new HiveConf();
		Properties props = conf.getAllProperties();
		String ret = "";
		for(Map.Entry<Object, Object> entry:props.entrySet()){
			ret = ret + entry.getKey().toString() + "=" + entry.getValue().toString() + ";|||";
		}
		return ret;
	}
}
