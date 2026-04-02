import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.WritableComparable
import org.apache.hadoop.io.WritableComparator
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

class TokenizerMapper extends Mapper[Object, Text, Text, IntWritable] {

  val one = new IntWritable(1)
  val word = new Text()
  val valid = Set('f','i','t','h','c','m','u','s')

  override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
    val tokens = value.toString.split("\\s+")

    for (token <- tokens) {
      if (token.length > 0) {
        val first = token.toLowerCase.charAt(0)
        if (valid.contains(first)) {
          word.set(first.toString)
          context.write(word, one)
        }
      }
    }
  }
}

class IntSumReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
  override def reduce(key: Text, values: java.lang.Iterable[IntWritable],
                      context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {

    var sum = 0
    val iter = values.iterator()

    while (iter.hasNext) {
      sum += iter.next().get()
    }

    context.write(key, new IntWritable(sum))
  }
}

class CustomComparator extends WritableComparator(classOf[Text], true) {

  val order = "fithcmus"

  override def compare(a: WritableComparable[_], b: WritableComparable[_]): Int = {
    val key1 = a.asInstanceOf[Text].toString.charAt(0)
    val key2 = b.asInstanceOf[Text].toString.charAt(0)

    order.indexOf(key1) - order.indexOf(key2)
  }
}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    val job = Job.getInstance(conf, "Word Count")

    job.setJarByClass(this.getClass)
    job.setMapperClass(classOf[TokenizerMapper])
    job.setReducerClass(classOf[IntSumReducer])

    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])

    job.setSortComparatorClass(classOf[CustomComparator])

    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
