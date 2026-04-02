# Thông tin bài làm

* **MSSV:** 23120166
* **Bài tập:** WordCount bằng Hadoop MapReduce
* **Mục tiêu:** Đếm số từ theo ký tự đầu trong tập `f, i, t, h, c, m, u, s`

## Cấu trúc bài làm

```text
23120166/
├── README.md
├── docs/
│   ├── 23120166_verification.txt
│   └── Report.pdf
└── src/
    ├── FilterWordCount.scala
    └── results.txt
```

## Mô tả thành phần chính

* `src/FilterWordCount.scala`: File mã nguồn Scala thực thi MapReduce.
  * `TokenizerMapper`: tách từ, trích xuất ký tự đầu và lọc các ký tự nằm trong tập mục tiêu.
  * `IntSumReducer`: gom nhóm, cộng dồn số lần xuất hiện và duy trì tự xuất ra theo yêu cầu bằng cơ chế `LinkedHashMap`.
  * `FilterWordCount` (Driver): cấu hình môi trường YARN Job, khai báo lớp đầu vào và file Output/Input trên HDFS.
* `src/results.txt`: tệp ghi nhận kết quả cuối cùng đã được kiểm tra trên WSL.
* `docs/23120166_verification.txt`: tệp dùng để xác thực hệ thống mạng và MAC Address từ môi trường làm việc khi thiết lập giả phân tán.
* `docs/Report.pdf`: Báo cáo quá trình làm bài và khắc phục các vấn đề lỗi gặp phải.

---

# Hướng dẫn chạy chương trình WordCount (Scala)

## 1. Môi trường yêu cầu
- Đã cài đặt và cấu hình Hadoop Pseudo-Distributed Mode
- Đã khởi động các tiến trình của Hadoop (NameNode, DataNode, ResourceManager, NodeManager):
  ```bash
  start-dfs.sh
  start-yarn.sh
  ```
- Đã cài đặt Scala 2.11.x (kiểm tra bằng lệnh `scala -version`)

## 2. Chuẩn bị dữ liệu trên HDFS
Khởi tạo cấu trúc thư mục và đưa file đầu vào lên HDFS:
```bash
hdfs dfs -mkdir -p /hcmus/23120166/input
hdfs dfs -put <đường_dẫn_tới_file>/words.txt /hcmus/23120166/input/
```

## 3. Biên dịch và đóng gói mã nguồn Scala
Di chuyển vào thư mục chứa code `FilterWordCount.scala` (thư mục `src/WordCount/`):
```bash
cd src/WordCount/
```

Biên dịch mã nguồn Scala thành các file `.class` (cần nạp classpath của Hadoop):
```bash
scalac -classpath $(hadoop classpath) FilterWordCount.scala
```

Đóng gói tất cả file class vừa được tạo ra thành file `WordCount.jar`:
```bash
jar -cvf WordCount.jar *.class
```

## 4. Cấu hình Class Path cho Scala Runtime
Vì chương trình được viết bằng Scala, Hadoop YARN khi khởi chạy sẽ cần nạp `scala-library.jar`. Hãy nạp biến môi trường sau trước khi chạy (lệnh find tự động tìm đường dẫn thư viện Scala đã cài đặt trong hệ thống Ubuntu/WSL):
```bash
export HADOOP_CLASSPATH=$(find /usr/share -name "scala-library*.jar" | head -n 1)
```

## 5. Chạy Job MapReduce
Thực thi MapReduce job thông qua YARN:
```bash
hadoop jar WordCount.jar FilterWordCount /hcmus/23120166/input /hcmus/23120166/output
```
*Lưu ý: Nếu nhận lỗi báo thư mục output đã tồn tại, bạn hãy xóa nó trước khi chạy lại bằng lệnh:*
`hdfs dfs -rm -r /hcmus/23120166/output`

## 6. Kiểm tra và trích xuất kết quả
Sau khi Job báo `completed successfully`, bạn có thể kiểm tra danh sách xuất ra:
```bash
hdfs dfs -cat /hcmus/23120166/output/part-r-00000
```
Kết quả hiển thị dạng Key-Value tương tự như sau (với các từ nằm trong tập {f, i, t, h, c, m, u, s}):
```text
f       15870
i       15220
t       25223
h       18662
c       38934
m       25194
u       23790
s       50571
```
Bạn cũng có thể xem kết quả được lưu tại tập tin `results.txt` đã được xuất sẵn.

