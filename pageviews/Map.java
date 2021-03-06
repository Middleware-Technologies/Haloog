/**
 * Map class for the pageviews job. For each log entry it outputs a key-value pair of kind <day>:<1>
 */

package haloog.pageviews;

import haloog.LogEntry;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text outKey;	
	private LogEntry logEntry;

	private DateFormat keyDateFormat = new SimpleDateFormat("yyyyMMdd");

	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		// Parse the log entry to be more accessible
		logEntry = new LogEntry(value.toString());
		
		// The key of the output is the day formatted ad yyyyMMdd
		outKey = new Text(keyDateFormat.format(logEntry.getLoggedTime()));
		
		output.collect(outKey, one);
	}

}