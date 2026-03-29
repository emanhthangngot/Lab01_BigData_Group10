# 🛠️ HƯỚNG DẪN CÀI ĐẶT HADOOP MASTER (NATIVE LINUX) - PHIÊN BẢN CẢI TIẾN

Tài liệu này đã được tối ưu để xử lý các lỗi kết nối SSH và Java thường gặp, giúp cụm của bạn hoạt động ổn định nhất.

---

## 🛠️ GIAI ĐOẠN 0: YÊU CẦU HỆ THỐNG & CÀI ĐẶT PHẦN MỀM

### 1. Phiên bản Java (OpenJDK 8/11)
- `sudo pacman -S jdk8-openjdk`
- Xác định đường dẫn: `ls -l /usr/lib/jvm/` (Thường là `/usr/lib/jvm/java-8-openjdk`).

### 2. Cài đặt Hadoop 3.3.6
```bash
wget https://downloads.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz
tar -xzvf hadoop-3.3.6.tar.gz
mv hadoop-3.3.6 ~/hadoop
```

### 3. Biến môi trường (`~/.bashrc`)
Thêm các dòng này vào cuối file:
```bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk
export HADOOP_HOME=~/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```
*Lưu ý: Nhớ chạy `source ~/.bashrc` sau khi lưu.*

---

## 🏗️ GIAI ĐOẠN 1: THIẾT LẬP MẠNG TAILSCALE
- **Khuyên dùng**: Cài Tailscale trên máy Linux Native và lấy IP Tailscale (giả sử `100.64.0.1`).
- **File `/etc/hosts`**:
```text
100.64.0.1  master
100.64.0.2  worker1
100.64.0.3  worker2
```

---

## 🔑 GIAI ĐOẠN 2: CẤU HÌNH SSH ĐA USER (QUAN TRỌNG)

Nếu tên user trên máy Master và Worker khác nhau (VD: Master là `tri`, Worker là `tuan`), cluster sẽ không khởi động được. Hãy giải quyết bằng cách tạo file `~/.ssh/config`:

```bash
nano ~/.ssh/config
```
Nội dung file:
```text
Host worker1
    HostName 100.64.0.2
    User tuan  # Tên user thật sự trên máy worker1

Host worker2
    HostName 100.64.0.3
    User thanh # Tên user thật sự trên máy worker2
```
*Sau đó bạn chỉ cần chạy `ssh-copy-id worker1` mà không cần quan tâm tên user.*

---

## ⚙️ GIAI ĐOẠN 3: CẤU HÌNH HADOOP CHI TIẾT

### 1. Fix lỗi JAVA_HOME (`etc/hadoop/hadoop-env.sh`)
Hadoop thường "quên" biến môi trường khi SSH. Hãy ghi đè trực tiếp vào file:
```bash
echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk" >> ~/hadoop/etc/hadoop/hadoop-env.sh
```

### 2. File `mapred-site.xml`
```xml
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>yarn.app.mapreduce.am.env</name>
        <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
    </property>
    <property>
        <name>mapreduce.map.env</name>
        <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
    </property>
    <property>
        <name>mapreduce.reduce.env</name>
        <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
    </property>
</configuration>
```

### 3. File `yarn-site.xml` (Giới hạn tài nguyên cho WSL)
```xml
<configuration>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>master</value>
    </property>
    <property>
        <name>yarn.nodemanager.resource.memory-mb</name>
        <value>2048</value> <!-- Giới hạn 2GB RAM mỗi node -->
    </property>
    <property>
        <name>yarn.scheduler.maximum-allocation-mb</name>
        <value>2048</value>
    </property>
</configuration>
```

---

## 🚀 GIAI ĐOẠN 4: KHỞI CHẠY & KIỂM TRA

1. `hdfs namenode -format`
2. `start-dfs.sh`
3. `start-yarn.sh`

> [!TIP]
> Nếu `jps` trên Worker không thấy DataNode, hãy kiểm tra log tại `~/hadoop/logs/`. Lỗi phổ biến nhất là **IP Master không ping tới được từ Worker**.
