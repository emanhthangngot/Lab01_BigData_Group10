// --- KHÂU 1: NHẬP THƯ VIỆN ---
import java.lang.Iterable
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, LongWritable, Text, WritableComparable, WritableComparator}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import scala.collection.JavaConverters._

// --- KHÂU 2: BỘ SẮP XẾP TÙY CHỈNH (CustomSortComparator) ---
// Thay vì sắp xếp A-Z mặc định, class này ép hệ thống xếp đúng thứ tự f,i,t,h,c,m,u,s
class CustomSortComparator extends WritableComparator(classOf[Text], true) {
  // Gán "trọng số" cho từng chữ cái. Chữ nào số nhỏ sẽ đứng trước.
  private val order = Map("f" -> 1, "i" -> 2, "t" -> 3, "h" -> 4, "c" -> 5, "m" -> 6, "u" -> 7, "s" -> 8)

  override def compare(a: WritableComparable[_], b: WritableComparable[_]): Int = {
    val key1 = a.asInstanceOf[Text].toString
    val key2 = b.asInstanceOf[Text].toString
    
    // Lấy trọng số của chữ cái. Nếu xuất hiện ký tự lạ, gán tít xuống cuối (99)
    val val1 = order.getOrElse(key1, 99)
    val val2 = order.getOrElse(key2, 99)
    
    val1.compareTo(val2)
  }
}

// --- KHÂU 3: BỘ PHÂN LOẠI (Mapper) ---
// Đọc từng dòng văn bản, nhặt ra các từ hợp lệ và gắn số "1" cho nó
class TokenizerMapper extends Mapper[LongWritable, Text, Text, IntWritable] {
  private val one = new IntWritable(1) // Tạo sẵn số 1 (chuẩn Hadoop)
  private val wordKey = new Text()     // Tạo biến lưu chữ cái đầu (chuẩn Hadoop)
  
  // Tập hợp 8 chữ cái mục tiêu theo đề bài
  private val targetChars = Set('f', 'i', 't', 'h', 'c', 'm', 'u', 's')

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit = {
    // Bước 1: Tách dòng thành các từ bằng dấu cách đơn thuần (KHÔNG dùng Regex)
    val words = value.toString.split(" ")
    
    // Bước 2: Duyệt qua từng từ trên băng chuyền
    for (w <- words) {
      val trimmed = w.trim // Cắt khoảng trắng dư thừa
      
      // Bước 3: Lọc bỏ các chuỗi rỗng (do 2 dấu cách dính liền tạo ra)
      if (trimmed.nonEmpty) {
        // Bước 4: Chuyển về chữ thường (Case insensitive)
        val lower = trimmed.toLowerCase
        
        // Bước 5: Lấy ký tự đầu tiên của từ
        val firstChar = lower.charAt(0)
        
        // Bước 6: So khớp xem ký tự đầu có nằm trong tập 8 chữ cái không
        if (targetChars.contains(firstChar)) {
          wordKey.set(firstChar.toString) // Chuyển chữ cái thành dạng Text
          context.write(wordKey, one)     // Ghi ra cặp (Chữ_cái, 1). Ví dụ: ("f", 1)
        }
      }
    }
  }
}

// --- KHÂU 4: BỘ ĐẾM (Reducer) ---
// Gom tất cả các số "1" của cùng một chữ cái lại và cộng dồn
class IntSumReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
  override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
    var sum = 0
    // Lặp qua danh sách các số 1 (ví dụ chữ f có list [1, 1, 1, 1...])
    for (value <- values.asScala) {
      sum += value.get() // Cộng dồn
    }
    // Ghi kết quả cuối cùng ra. Ví dụ: ("f", 15870)
    // Hadoop tự động chèn dấu Tab (\t) giữa key và value tạo thành định dạng TSV
    context.write(key, new IntWritable(sum))
  }
}

// --- KHÂU 5: TRUNG TÂM ĐIỀU KHIỂN (Driver / Main) ---
// Nơi cấu hình và khởi động toàn bộ dây chuyền
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    val job = Job.getInstance(conf, "KHTN Word Count") // Đặt tên cho Job
    
    // Khai báo các thành phần của dây chuyền
    job.setJarByClass(this.getClass)
    job.setMapperClass(classOf[TokenizerMapper])
    job.setCombinerClass(classOf[IntSumReducer]) // Combiner gộp tạm thời ở nhánh để tối ưu tốc độ
    job.setReducerClass(classOf[IntSumReducer])
    
    // Cài đặt bộ ép thứ tự f,i,t...
    job.setSortComparatorClass(classOf[CustomSortComparator])
    
    // Định nghĩa kiểu dữ liệu đầu ra: Key là Text, Value là số nguyên IntWritable
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])

    // Đường dẫn Input và Output trên HDFS (Đã đổi thành MSSV 23120180)
    val inputPath = new Path("hdfs://localhost:9000/hcmus/23120180/words.txt")
    val outputPath = new Path("hdfs://localhost:9000/hcmus/23120180/output_wordcount")
    
    // An toàn: Tự động xóa thư mục output nếu nó đã tồn tại từ lần chạy trước
    val fs = outputPath.getFileSystem(conf)
    if (fs.exists(outputPath)) {
      fs.delete(outputPath, true)
    }

    FileInputFormat.addInputPath(job, inputPath)
    FileOutputFormat.setOutputPath(job, outputPath)

    // Chạy Job và thoát với mã 0 nếu thành công, mã 1 nếu thất bại
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
