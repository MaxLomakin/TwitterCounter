import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by Max on 15.03.2017.
 */
public class TweetsAggregator extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {

        System.out.println("Started");

        String query = "AFP -rt";

        Configuration conf = new Configuration();
        Job job = new Job(conf, "twitter");

        conf.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
        );

        FileSystem fs = FileSystem.get(conf);

        String filePath = "/input" + new Random().nextInt();
        String outputFilePath = "/output" + new Random().nextInt();

        System.out.println(filePath);
        fs.createNewFile(new Path(filePath));

        FSDataOutputStream fin = fs.create(new Path(filePath));

        try {
            List<String> tweets = TwitterClient.search(query);

            for (String t : tweets) {
                fin.write((t + "\n").getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                fin.flush();
                fin.close();
            }
        }

        job.setJarByClass(getClass());
        job.setJobName(getClass().getSimpleName());

        FileInputFormat.addInputPath(job, new Path(filePath));
        FileOutputFormat.setOutputPath(job, new Path(outputFilePath));

        job.setMapperClass(TweetsMapper.class);
        job.setCombinerClass(TweetsReducer.class);
        job.setReducerClass(TweetsReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        boolean completion = job.waitForCompletion(true);
        return completion ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int rc = ToolRunner.run(new TweetsAggregator(), args);
        System.exit(rc);
    }
}
