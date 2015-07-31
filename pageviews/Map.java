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
	private Text views = new Text("PageViews:");	
	private LogEntry logEntry;

	private DateFormat keyDateFormat = new SimpleDateFormat("yyyyMMdd");

	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		logEntry = new LogEntry(value.toString());
		
		views = new Text(keyDateFormat.format(logEntry.getLoggedTime()));

		output.collect(views, one);
	}

}