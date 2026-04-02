import java.lang.Iterable
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, LongWritable, Text, WritableComparable, WritableComparator}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import scala.collection.JavaConverters._

class CustomSortComparator extends WritableComparator(classOf[Text], true) {
  private val order = Map("f" -> 1, "i" -> 2, "t" -> 3, "h" -> 4, "c" -> 5, "m" -> 6, "u" -> 7, "s" -> 8)

  override def compare(a: WritableComparable[_], b: WritableComparable[_]): Int = {
    val key1 = a.asInstanceOf[Text].toString
    val key2 = b.asInstanceOf[Text].toString

    val val1 = order.getOrElse(key1, 99)
    val val2 = order.getOrElse(key2, 99)

    val1.compareTo(val2)
  }
}

class TokenizerMapper extends Mapper[LongWritable, Text, Text, IntWritable] {
  private val one = new IntWritable(1)
  private val wordKey = new Text()

  private val targetChars = Set('f', 'i', 't', 'h', 'c', 'm', 'u', 's')

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit = {
    val words = value.toString.split(" ")

    for (w <- words) {
      val trimmed = w.trim

      if (trimmed.nonEmpty) {
        val lower = trimmed.toLowerCase

        val firstChar = lower.charAt(0)

        if (targetChars.contains(firstChar)) {
          wordKey.set(firstChar.toString)
          context.write(wordKey, one)
        }
      }
    }
  }
}

class IntSumReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
  override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
    var sum = 0
    for (value <- values.asScala) {
      sum += value.get()
    }
    context.write(key, new IntWritable(sum))
  }
}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    val job = Job.getInstance(conf, "KHTN Word Count")

    job.setJarByClass(this.getClass)
    job.setMapperClass(classOf[TokenizerMapper])
    job.setCombinerClass(classOf[IntSumReducer])
    job.setReducerClass(classOf[IntSumReducer])

    job.setSortComparatorClass(classOf[CustomSortComparator])

    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])

    val inputPath = new Path("hdfs://localhost:9000/hcmus/23120099/words.txt")
    val outputPath = new Path("hdfs://localhost:9000/hcmus/23120099/output_wordcount")

    val fs = outputPath.getFileSystem(conf)
    if (fs.exists(outputPath)) {
      fs.delete(outputPath, true)
    }

    FileInputFormat.addInputPath(job, inputPath)
    FileOutputFormat.setOutputPath(job, outputPath)

    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
