import java.io.IOException
import java.util.StringTokenizer
import scala.collection.mutable

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

class TokenizerMapper extends Mapper[Object, Text, Text, IntWritable] {
	private val one = new IntWritable(1)
	private val outKey = new Text()
	private val targetChars = Set('f', 'i', 't', 'h', 'c', 'm', 'u', 's')

	@throws[IOException]
	@throws[InterruptedException]
	override def map(
			key: Object,
			value: Text,
			context: Mapper[Object, Text, Text, IntWritable]#Context
	): Unit = {
		val tokenizer = new StringTokenizer(value.toString)

		while (tokenizer.hasMoreTokens) {
			val token = tokenizer.nextToken().toLowerCase
			if (token.nonEmpty) {
				val firstChar = token.charAt(0)
				if (targetChars.contains(firstChar)) {
					outKey.set(firstChar.toString)
					context.write(outKey, one)
				}
			}
		}
	}
}

class IntSumReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
	private val result = new IntWritable()
	private val targetChars = Vector('f', 'i', 't', 'h', 'c', 'm', 'u', 's')
	private val counts = mutable.LinkedHashMap(targetChars.map(ch => ch -> 0): _*)

	@throws[IOException]
	@throws[InterruptedException]
	override def reduce(
			key: Text,
			values: java.lang.Iterable[IntWritable],
			context: Reducer[Text, IntWritable, Text, IntWritable]#Context
	): Unit = {
		var sum = 0
		val iterator = values.iterator()

		while (iterator.hasNext) {
			sum += iterator.next().get()
		}

		val first = key.toString.charAt(0)
		if (counts.contains(first)) {
			counts.update(first, sum)
		}
	}

	@throws[IOException]
	@throws[InterruptedException]
	override def cleanup(context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
		counts.foreach { case (ch, sum) =>
			result.set(sum)
			context.write(new Text(ch.toString), result)
		}
	}
}

object FilterWordCount {
	def main(args: Array[String]): Unit = {
		if (args.length != 2) {
			System.err.println("Usage: FilterWordCount <input_path> <output_path>")
			System.exit(2)
		}

		val conf = new Configuration()
		val job = Job.getInstance(conf, "filter word count fit hcmus")

		job.setJarByClass(this.getClass)
		job.setMapperClass(classOf[TokenizerMapper])
		job.setReducerClass(classOf[IntSumReducer])
		job.setNumReduceTasks(1)

		job.setOutputKeyClass(classOf[Text])
		job.setOutputValueClass(classOf[IntWritable])

		FileInputFormat.addInputPath(job, new Path(args(0)))
		FileOutputFormat.setOutputPath(job, new Path(args(1)))

		System.exit(if (job.waitForCompletion(true)) 0 else 1)
	}
}
