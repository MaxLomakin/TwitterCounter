import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TweetsReducer<KEY> extends Reducer<KEY, LongWritable, KEY,LongWritable> {

    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    private Set<Integer> averageData = new HashSet<>();

    private LongWritable result = new LongWritable();

    public void reduce(KEY key, Iterable<LongWritable> values,
                       Context context) throws IOException, InterruptedException {

        min = Math.min(min, Integer.valueOf(key.toString()));
        max = Math.max(max, Integer.valueOf(key.toString()));

        averageData.add(Integer.valueOf(key.toString()));

        long sum = 0;
        for (LongWritable val : values) {
            sum += val.get();
        }
        result.set(sum);
        context.write(key, result);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        System.out.println("MIN VALUE = " + min);
        System.out.println("MAX VALUE = " + max);
        System.out.println("AVERAGE VALUE = " + calculateAverage());

        super.cleanup(context);
    }

    private float calculateAverage () {
        int sum = 0;
        for (Integer integer : averageData) {
            sum += integer;
        }

        return ((float) sum) / averageData.size();
    }
}