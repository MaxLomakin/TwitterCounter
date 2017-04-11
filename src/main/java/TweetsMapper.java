import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class TweetsMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    private LongWritable count = new LongWritable();
    private Text word = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        System.out.println("!!!" + value);

        word.set(String.valueOf(value.getLength()));
        count.set(1);
        context.write(word, count);
    }
}