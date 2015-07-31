/**
 * Manages the MapReduce process that counts the number of referring domains per day in the given log file
 */

package haloog.referring;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.mapred.jobcontrol.JobControl;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Driver extends Configured implements Tool {
		
	private String tempFolder = "/HaloogTemp";
	
	@Override
	public int run(String[] args) throws Exception {
		
		FileSystem fs = FileSystem.get(getConf());
		fs.delete(new Path(tempFolder), true);
		
		Configuration conf = getConf();
		
        JobConf jobConf1 = new JobConf(conf, Driver.class);
        jobConf1.setJobName("haloog-referring-1");
		
        jobConf1.setOutputKeyClass(Text.class);
        jobConf1.setOutputValueClass(IntWritable.class);
			
        jobConf1.setMapperClass(MapFirstStep.class);
        jobConf1.setCombinerClass(ReduceFirstStep.class);
        jobConf1.setReducerClass(ReduceFirstStep.class);
			
        jobConf1.setInputFormat(TextInputFormat.class);
        jobConf1.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(jobConf1, new Path(args[0]));
		FileOutputFormat.setOutputPath(jobConf1, new Path(tempFolder));
		
        JobConf jobConf2 = new JobConf(conf, Driver.class);
        jobConf2.setJobName("haloog-referring-2");
		
        jobConf2.setOutputKeyClass(Text.class);
        jobConf2.setOutputValueClass(IntWritable.class);
			
        jobConf2.setMapperClass(MapSecondStep.class);
        jobConf2.setCombinerClass(ReduceSecondStep.class);
        jobConf2.setReducerClass(ReduceSecondStep.class);
			
        jobConf2.setInputFormat(TextInputFormat.class);
        jobConf2.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(jobConf2, new Path(tempFolder));
		FileOutputFormat.setOutputPath(jobConf2, new Path(args[1]));
		
        Job job1 = new Job(jobConf1);
        Job job2 = new Job(jobConf2);
        JobControl jobControl = new JobControl("chaining");
        jobControl.addJob(job1);
        jobControl.addJob(job2);
        job2.addDependingJob(job1);
        
        Thread t = new Thread(jobControl); 
        t.setDaemon(true);
        t.start(); 
                      
        while (!jobControl.allFinished()) { 
          try { 
            Thread.sleep(1000); 
          } catch (InterruptedException e) { 
            // Ignore. 
          } 
        } 
        
        return 0;
	}

	public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new Driver(), args);
        System.exit(res);
		
	}

}