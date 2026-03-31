import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import java.io.PrintWriter
import scala.io.Source

object WordCount {
  def main(args: Array[String]): Unit = {
    // Khởi tạo Spark Session
    val spark = SparkSession.builder()
      .appName("WordCount")
      .getOrCreate()

    val sc = spark.sparkContext

    // Đường dẫn file HDFS (PHẢI đọc từ HDFS, không phải local)
    val hdfsPath = "hdfs://localhost:9000/hcmus/23120099/words.txt"

    // Danh sách ký tự cần đếm (không phân biệt hoa thường)
    val targetChars = Set('f', 'i', 't', 'h', 'c', 'm', 'u', 's')

    // Đọc file từ HDFS
    val rdd = sc.textFile(hdfsPath)

    // Tách từ (split bằng khoảng trắng, loại bỏ chuỗi rỗng)
    val words = rdd
      .flatMap(line => line.split(" "))
      .filter(_.nonEmpty)

    // Filter: lấy những từ bắt đầu bằng một trong 8 ký tự (case-insensitive)
    val filtered = words.filter { word =>
      val firstChar = word.toLowerCase.charAt(0)
      targetChars.contains(firstChar)
    }

    // Map: tách từ thành (firstChar, 1)
    val mapped = filtered.map { word =>
      val firstChar = word.toLowerCase.charAt(0)
      (firstChar.toString, 1)
    }

    // Reduce: đếm số lần xuất hiện
    val counted = mapped.reduceByKey(_ + _)

    // Sort theo ký tự
    val sorted = counted.sortByKey()

    // Chuyển sang local để xuất file
    val result = sorted.collect()

    // Xuất kết quả thành file TSV (sử dụng \t)
    val outputPath = "results.txt"
    val writer = new PrintWriter(outputPath)

    for ((char, count) <- result) {
      writer.println(s"$char\t$count")  // Tab character \t
    }

    writer.close()

    // In ra console để kiểm tra
    println("=== WordCount Results ===")
    for ((char, count) <- result) {
      println(s"$char: $count")
    }

    spark.stop()
  }
}
