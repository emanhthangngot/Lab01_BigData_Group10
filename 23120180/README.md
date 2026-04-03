# Lab 01 - Hadoop MapReduce WordCount (Scala)

## Thông tin bài làm
- MSSV: 23120180
- Bài tập: WordCount bằng Hadoop MapReduce
- Mục tiêu: Đếm số từ theo ký tự đầu trong tập `f, i, t, h, c, m, u, s`

## Cấu trúc bài làm

```text
23120180/
├─ README.md
├─ docs/
│  ├─ 23120180_verification.txt
│  └─ Report.pdf
└─ src/
  └─ WordCount/
    ├─ WordCount.scala
    └─ results.txt
```

## Mô tả thành phần chính
- `src/WordCount/WordCount.scala`:
  - `TokenizerMapper`: tách từ, lấy ký tự đầu, lọc theo tập ký tự mục tiêu.
  - `IntSumReducer`: cộng dồn số lần xuất hiện.
  - `CustomSortComparator`: ép thứ tự output `f -> i -> t -> h -> c -> m -> u -> s`.
  - `WordCount` (Driver): cấu hình job, khai báo input/output HDFS, tự xóa output cũ.
- `src/WordCount/results.txt`: kết quả cuối đã trích xuất về máy local.
- `docs/23120180_verification.txt`: thông tin xác thực môi trường máy chạy bài.

## Dữ liệu đầu vào/đầu ra
- Input HDFS (theo mã nguồn): `hdfs://localhost:9000/hcmus/23120180/words.txt`
- Output HDFS: `hdfs://localhost:9000/hcmus/23120180/output_wordcount`

## Cách chạy (MapReduce thuần)

### 1) Kiểm tra daemon Hadoop

```bash
jps | grep -E "NameNode|DataNode|ResourceManager|NodeManager"
```

Nếu thiếu tiến trình, chạy:

```bash
start-dfs.sh
start-yarn.sh
sleep 2
jps
```

### 2) Kiểm tra input trên HDFS

```bash
hdfs dfs -ls /hcmus/23120180/words.txt
```

Nếu chưa có file:

```bash
hdfs dfs -put words.txt /hcmus/23120180/
```

### 3) Build Fat JAR

```bash
sbt assembly
```

### 4) Submit job MapReduce

```bash
hadoop jar target/scala-2.12/*assembly*.jar WordCount
```

Nếu dự án build ra thư mục Scala version khác (ví dụ `scala-2.13`), thay lại đường dẫn tương ứng.

### 5) Trích xuất kết quả

```bash
hdfs dfs -getmerge /hcmus/23120180/output_wordcount results.txt
cat results.txt
```

## Kết quả kỳ vọng
- Trên terminal khi submit job: `map 100% reduce 100%`.
- File output ở HDFS có định dạng TSV, đúng thứ tự `f, i, t, h, c, m, u, s`.

## Kết quả hiện tại (`src/WordCount/results.txt`)

```text
f	15870
i	15220
t	25223
h	18662
c	38934
m	25194
u	23790
s	50571
```

## Yêu cầu môi trường
- Java 11
- Scala + SBT
- Hadoop 3.x (HDFS + YARN)

## Troubleshooting
- Lỗi `java.lang.NoClassDefFoundError: scala/runtime/BoxedUnit`:
  - Dùng `sbt assembly` để tạo Fat JAR (không dùng JAR thường từ `sbt package`).
- Lỗi `Could not find or load main class org.apache.hadoop.mapreduce.v2.app.MRAppMaster`:
  - Kiểm tra cấu hình `mapred-site.xml` có khai báo `HADOOP_MAPRED_HOME`.
  - Restart YARN rồi chạy lại:

```bash
stop-yarn.sh
start-yarn.sh
```

- Lỗi không truy cập được HDFS:
  - Chạy `start-dfs.sh` trước khi submit job.
- Lỗi thiếu file input trên HDFS:
  - Upload lại bằng `hdfs dfs -put words.txt /hcmus/23120180/`.

## Ghi chú
- Job có cơ chế tự xóa thư mục output cũ trong code để tránh lỗi `output already exists`.
