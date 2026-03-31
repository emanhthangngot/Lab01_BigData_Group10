# HƯỚNG DẪN CHẠY WORDCOUNT

> **Compile hoàn thành:** ✅ 30/03/2026 18:30
> **JAR file:** `target/scala-2.13/WordCount-assembly-1.0.jar` (5.8MB)
> **Input file:** `/hcmus/23120099/words.txt` (trên HDFS)

---

## 📋 KIỂM TRA TRƯỚC KHI CHẠY

Trước khi chạy, hãy kiểm tra:

### 1️⃣ HDFS đang chạy?

Mở terminal và chạy:
```bash
jps | grep -E "NameNode|DataNode"
```

**Kỳ vọng output:**
```
72482 NameNode
72654 DataNode
```

Nếu không thấy → Khởi động HDFS:
```bash
start-dfs.sh
sleep 2
jps
```

---

### 2️⃣ File words.txt tồn tại trên HDFS?

```bash
hdfs dfs -ls /hcmus/23120099/words.txt
```

**Kỳ vọng:** Thấy file

Nếu không → Tải file lên:
```bash
hdfs dfs -put "/home/pearspringmind/Studying/Big Data/Lab 01/WordCount/words.txt" /hcmus/23120099/
```

---

## 🚀 CHẠY WORDCOUNT

### Phương pháp 1: Copy-Paste lệnh (dễ nhất)

Mở terminal **mới** và copy toàn bộ lệnh này:

```bash
export SPARK_HOME=$HOME/opt/spark && export PATH=$SPARK_HOME/bin:$PATH && cd "/home/pearspringmind/Studying/Big Data/Lab 01/WordCount" && spark-submit --class WordCount --master local[*] target/scala-2.13/WordCount-assembly-1.0.jar
```

**Bấm Enter** và chờ kết quả.

---

### Phương pháp 2: Chạy script

Mở terminal **mới** và chạy:

```bash
bash "/home/pearspringmind/Studying/Big Data/Lab 01/WordCount/RUN_SPARK.sh"
```

---

## ✅ KỲ VỌNG KẾT QUẢ

Khi chạy xong, bạn sẽ thấy output:

```
...
26/03/30 18:22:17 INFO SparkContext: Running Spark version 3.4.0
...
=== WordCount Results ===
c: 4
f: 6
h: 3
i: 5
m: 4
s: 3
t: 2
u: 1
26/03/30 18:22:17 INFO SparkContext: Successfully stopped SparkContext
...
```

---

## 📸 GỢI Ý CHỤP ẢNH

**Ảnh #SP13 (bắt buộc):**
- Chụp toàn bộ terminal từ lúc chạy `spark-submit` đến khi thấy `=== WordCount Results ===` + kết quả

**Cách chụp:**
1. Mở terminal
2. Chạy lệnh
3. Khi thấy kết quả, dùng Print Screen hoặc screenshot tool để chụp
4. Save file (ảnh .png hoặc .jpg)

---

## 📝 LỆNH CHI TIẾT (Breakdown)

Nếu muốn chạy từng bước:

```bash
# Bước 1: Thiết lập biến môi trường
export SPARK_HOME=$HOME/opt/spark
export PATH=$SPARK_HOME/bin:$PATH

# Bước 2: Kiểm tra Spark
spark-submit --version

# Bước 3: Đi vào thư mục
cd "/home/pearspringmind/Studying/Big Data/Lab 01/WordCount"

# Bước 4: Chạy WordCount
spark-submit --class WordCount --master local[*] target/scala-2.13/WordCount-assembly-1.0.jar

# Bước 5: Kiểm tra results.txt
cat results.txt
hexdump -C results.txt | head -10
```

---

## ❌ TROUBLESHOOTING

### Lỗi: "spark-submit: command not found"
→ Mở **terminal mới** (tự động load `$HOME/opt/spark`)

### Lỗi: "Cannot resolve HDFS"
→ Chạy `start-dfs.sh` trước

### Lỗi: "HDFS file not found"
→ Tải file lên HDFS: `hdfs dfs -put words.txt /hcmus/23120099/`

---

**Good luck! 🚀**
