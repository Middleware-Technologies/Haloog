# Haloog
##Log processing with Hadoop

During April of 2003 one of Internet's first viral videos was born. It was the video of a kid pretending to wield a Star Wars lightsaber. For many years the video remained the top viral video on the Internet. The video's name was "Star Wars Kid".

Andy Baio, creater of XOXO and former CTO of Kickstarter, played an important role in spreading the viral video. He has made the logs of his Waxy.org Apache Servers available so that we can study the spreading phenomenon. You can download the logs at the following URL: http://home.deib.polimi.it/guinea/middleware/star_wars_data.zip. The file is more or less 160 MB in zipped form, and 1.6 GB in unziped form. The data goes from the 10th of April to the 26th of November; the video was posted to Waxy.org on the 29th of April. Among other things the log provides dates, times, IP addresses, user agents, and referer information.

Using Hadoop in a fully distributed cluster (use your own phisical/virtual machines) provide the following information:

- Waxy.org total pageviews per day in the entire time range
- Video downloads (wmv files) per day in the entire time range. (Aggregate the two video versions - normal and remixed)
- Number of referring domains per day (between the 22nd of April and the 30th of May)
- Number of referrals per domain (between the 22nd of April and the 30th of May)

Optional: Provide charts for the information

## Usage
Before running the program, you should create on HDFS a folder where to put the log file. You can refer to "[Using the command line to manage files on HDFS]"(http://hortonworks.com/hadoop-tutorial/using-commandline-manage-files-hdfs) to know how.

To run the jar file
```hadoop jar Haloog.jar class /path/to/input /path/to/input```

Where __class__ should be the driver class for the wanted operation (i.e. downloads, pageviews, referrals, or referring). For example
```hadoop jar Haloog.jar haloog.referring.Driver /user/middle/haloog/input/star_wars_kid.log /user/middle/haloog/output```

To show the result run
```hadoop fs -cat /path/to/output/*```
 
Before running a second time, you should delete the output folder 
```hadoop fs -rm -r /path/to/output*```
