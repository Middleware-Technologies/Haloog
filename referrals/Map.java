/**
 * Map class for the referrals job. For each log entry that falls in the set time range, it outputs a key-value pair of kind <domain>:<1>
 */

package haloog.referrals;

import haloog.LogEntry;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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


	private DateFormat keyDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss X", Locale.US);
	private Date startDate, endDate;
	
	public Map() {
		try {
			startDate = keyDateFormat.parse("22/Apr/2003:00:00:00 -0700");
			endDate = keyDateFormat.parse("30/May/2003:23:59:59 -0700");
		} catch (ParseException e) {
			// Ignore
		}
	}

	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		// Parse the log entry to be more accessible
		logEntry = new LogEntry(value.toString());
		
		if( !logEntry.getLoggedTime().before(startDate) && !logEntry.getLoggedTime().after(endDate) ) {
			// The key of the output is the domain
			outKey = new Text(logEntry.getRefererDomain());
			
			output.collect(outKey, one);
		}
		
	}

}