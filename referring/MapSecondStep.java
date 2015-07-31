package haloog.referring;

import haloog.LogEntry;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class MapSecondStep extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private static final Pattern inputPattern = Pattern.compile("<(\\d+), ([^>]+)>(.*)");
	private final static IntWritable one = new IntWritable(1);

	private Text outKey;	


	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		Matcher matcher = inputPattern.matcher(value.toString());
		if(matcher.matches()) {
			outKey = new Text(matcher.group(1));	
			
			if(!matcher.group(2).equals(LogEntry.NO_DOMAIN)) {
				output.collect(outKey, one);
			}
		}
		
	}

}