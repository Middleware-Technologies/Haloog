package haloog.referring;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class ReduceFirstStep extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	
	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		// Just pass through, we nee
		output.collect(key, one);

	}

}
