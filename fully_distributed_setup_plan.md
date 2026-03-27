# KẾ HOẠCH TRIỂN KHAI CỤM HADOOP FULLY-DISTRIBUTED

> **Cụm 1: Máy vật lý (Physical Machines)** — Hướng dẫn triển khai chi tiết  
> Mục tiêu: 1 Master (Arch Linux) + 2 Worker (Windows/WSL)  
> Ngày triển khai: 25/03/2026

---

## 1. Tổng Quan Kiến Trúc Hệ Thống

### 1.1 Sơ đồ cụm

```
┌──────────────────────────────────────────────────────────┐
│                   MẠNG CHUNG (WiFi / LAN / Hotspot)      │
│                                                          │
│  ┌───────────────┐  ┌───────────────┐  ┌───────────────┐ │
│  │  MASTER NODE  │  │  WORKER NODE 1│  │ WORKER NODE 2 │ │
│  │  (Laptop A)   │  │  (Laptop B)   │  │  (Laptop C)   │ │
│  │               │  │               │  │               │ │
│  │ • NameNode    │  │ • DataNode    │  │ • DataNode    │ │
│  │ • Resource-   │  │ • Node-       │  │ • Node-       │ │
│  │   Manager     │  │   Manager     │  │   Manager     │ │
│  │               │  │               │  │               │ │
│  │ Arch Linux    │  │ WSL2/Ubuntu   │  │ WSL2/Ubuntu   │ │
│  │ (native)      │  │ (bridged)     │  │ (bridged)     │ │
│  └───────┬───────┘  └───────┬───────┘  └───────┬───────┘ │
│          │                  │                  │          │
│          └─────── SSH ──────┴─────── SSH ──────┘          │
└──────────────────────────────────────────────────────────┘
```

### 1.2 Phân công vai trò

| Node | Hostname | Tiến trình | Thiết bị | Hệ điều hành |
|:---|:---|:---|:---|:---|
| **Master** | `master` | NameNode, ResourceManager | Laptop A (Lê Xuân Trí) | Arch Linux (native) |
| **Worker 1** | `worker1` | DataNode, NodeManager | Laptop B/C/D | Windows + WSL2 Ubuntu |
| **Worker 2** | `worker2` | DataNode, NodeManager | Laptop B/C/D | Windows + WSL2 Ubuntu |

### 1.3 Tại sao kiến trúc này đáp ứng yêu cầu Lab

- **Máy vật lý tách biệt** — 2+ laptop kết nối qua mạng LAN thực → đủ điều kiện nhận **điểm thưởng cụm vật lý**
- **2 Worker node** → hệ số nhân bản (replication) ≥ 2, thể hiện lưu trữ phân tán thực sự
- **Phân tách Master/Worker** → phản ánh mô hình triển khai Hadoop trong thực tế
- Tất cả các node có **IP riêng biệt** → thỏa mãn ràng buộc "không trùng IP"

---

## 2. Điều Kiện Tiên Quyết (TẤT CẢ CÁC NODE)

### 2.1 Yêu cầu phần mềm

| Phần mềm | Phiên bản | Ghi chú |
|:---|:---|:---|
| Java (JDK) | **OpenJDK 11** (khuyến nghị) | Xem hướng dẫn cài đặt theo từng OS bên dưới |
| Hadoop | **3.3.6** (hoặc phiên bản đang dùng cho Pseudo-distributed) | Bản cài phải giống nhau trên tất cả các node |
| SSH server | OpenSSH | Bắt buộc trên tất cả các node |

#### Master (Arch Linux)

```bash
# Cài Java
sudo pacman -S jdk11-openjdk

# Kiểm tra
java -version
# Kết quả mong đợi: openjdk version "11.x.x"

# Cài SSH server (thường đã có sẵn)
sudo pacman -S openssh
sudo systemctl enable sshd
sudo systemctl start sshd
```

#### Worker (WSL2 Ubuntu)

```bash
# Cài Java
sudo apt update
sudo apt install openjdk-11-jdk -y

# Kiểm tra
java -version

# Cài và khởi động SSH server
sudo apt install openssh-server -y
sudo service ssh start
```

### 2.2 Cài đặt Hadoop (TẤT CẢ CÁC NODE — Phải giống nhau)

Nếu node nào chưa cài Hadoop:

```bash
# Tải về (dùng CÙNG phiên bản trên TẤT CẢ node)
cd ~
wget https://dlcdn.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz

# Giải nén
tar -xzf hadoop-3.3.6.tar.gz
mv hadoop-3.3.6 ~/hadoop
```

### 2.3 Biến môi trường (TẤT CẢ CÁC NODE)

Thêm vào `~/.bashrc` (hoặc `~/.zshrc` nếu dùng zsh trên Arch):

```bash
# Java
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk        # Arch Linux
# export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64  # Ubuntu/WSL

# Hadoop
export HADOOP_HOME=~/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$JAVA_HOME/bin
```

Sau đó nạp lại:

```bash
source ~/.bashrc
```

**Kiểm tra trên từng node:**

```bash
echo $JAVA_HOME
echo $HADOOP_HOME
hadoop version
```

> [!IMPORTANT]
> Đường dẫn `JAVA_HOME` **khác nhau** giữa Arch Linux và Ubuntu. Kiểm tra kỹ trên từng node.

### 2.4 Thiết lập JAVA_HOME trong file cấu hình Hadoop (TẤT CẢ CÁC NODE)

Sửa file `$HADOOP_HOME/etc/hadoop/hadoop-env.sh`:

```bash
# Tìm dòng JAVA_HOME và sửa (bỏ comment nếu cần):
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk        # Arch
# export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64  # Ubuntu
```

### 2.5 Các cổng mạng cần thiết

| Cổng | Dịch vụ | Hướng kết nối |
|:---|:---|:---|
| **9000** | HDFS NameNode RPC | Worker → Master |
| **9870** | HDFS Web UI | Trình duyệt → Master |
| **8088** | YARN ResourceManager Web UI | Trình duyệt → Master |
| **8042** | YARN NodeManager Web UI | Trình duyệt → Worker |
| **9864** | DataNode Web UI | Trình duyệt → Worker |

> [!WARNING]
> **Tường lửa Windows** trên các laptop Worker có thể chặn kết nối đến WSL. Cần:
> 1. Tắt tạm thời Windows Firewall, HOẶC
> 2. Thêm rule cho phép các cổng 9000, 9864, 8042 trong cài đặt Firewall

### 2.6 Đồng bộ thời gian (Khuyến nghị)

Đảm bảo tất cả máy có thời gian hệ thống gần giống nhau. Lệch thời gian lớn có thể khiến HDFS từ chối báo cáo block.

```bash
# Kiểm tra thời gian trên tất cả node
date

# Nếu thời gian lệch đáng kể (>5 phút), đồng bộ:
# Arch: sudo timedatectl set-ntp true
# Ubuntu: sudo apt install ntp && sudo service ntp start
```

---

## 3. Cấu Hình Mạng

### 3.1 Kết nối tất cả máy vào cùng mạng

Tất cả laptop phải cùng nằm trên **một mạng WiFi / LAN / hotspot điện thoại**.

> [!TIP]
> Dùng **hotspot điện thoại** là phương án ổn định nhất khi làm việc ở quán cà phê — nó tạo ra một mạng LAN riêng mà tất cả laptop đều có thể tham gia.

### 3.2 Xác định địa chỉ IP

Chạy trên **từng máy**:

```bash
ip addr
```

Tìm IP ở giao diện mạng đang hoạt động (thường là `wlan0` cho WiFi, `eth0` cho LAN).

**Ví dụ kết quả (tìm dòng `inet`):**

```
3: wlan0: <BROADCAST,MULTICAST,UP,LOWER_UP>
    inet 192.168.1.100/24 brd 192.168.1.255 scope global wlan0
```

**Ghi lại các IP:**

| Node | Hostname | IP ví dụ |
|:---|:---|:---|
| Master (Laptop A) | `master` | `192.168.1.100` |
| Worker 1 (Laptop B) | `worker1` | `192.168.1.101` |
| Worker 2 (Laptop C) | `worker2` | `192.168.1.102` |

> [!CAUTION]
> **Lưu ý quan trọng về mạng WSL2:** Mặc định, WSL2 sử dụng NAT networking và được cấp một **IP ảo** (ví dụ `172.x.x.x`) mà các máy khác **KHÔNG THỂ** truy cập được. Có hai cách giải quyết:
>
> **Cách A (Khuyến nghị): Dùng chế độ `mirrored`**
> Sửa file `%USERPROFILE%\.wslconfig` trên Windows:
> ```ini
> [wsl2]
> networkingMode=mirrored
> ```
> Sau đó khởi động lại WSL: `wsl --shutdown` → mở lại. WSL sẽ dùng chung IP với máy host Windows.
>
> **Cách B: Chuyển tiếp cổng (port forwarding) từ Windows sang WSL**
> ```powershell
> # Chạy trong PowerShell với quyền Admin trên Windows
> netsh interface portproxy add v4tov4 listenport=9000 listenaddress=0.0.0.0 connectport=9000 connectaddress=$(wsl hostname -I)
> # Lặp lại cho các cổng: 9864, 8042
> ```
>
> Chọn MỘT cách và áp dụng nhất quán.

### 3.3 Cấu hình `/etc/hosts` (TẤT CẢ CÁC NODE)

Sửa `/etc/hosts` trên **mọi máy** (Master và tất cả Worker):

```bash
sudo nano /etc/hosts
```

Thêm các dòng sau (thay IP bằng IP thực tế của bạn):

```
192.168.1.100   master
192.168.1.101   worker1
192.168.1.102   worker2
```

> [!WARNING]
> KHÔNG xóa các dòng có sẵn (như `127.0.0.1 localhost`). Chỉ **thêm mới** các dòng trên.

### 3.4 Đặt hostname (Từng node)

Trên từng máy, đặt hostname tương ứng:

```bash
# Trên Master:
sudo hostnamectl set-hostname master

# Trên Worker 1:
sudo hostnamectl set-hostname worker1

# Trên Worker 2:
sudo hostnamectl set-hostname worker2
```

Kiểm tra:

```bash
hostname
# Phải in ra: master (hoặc worker1, worker2)
```

### 3.5 Kiểm tra kết nối mạng

Từ **Master**, ping cả hai Worker:

```bash
ping -c 3 worker1
ping -c 3 worker2
```

Từ mỗi **Worker**, ping Master:

```bash
ping -c 3 master
```

**Kết quả mong đợi:** 0% mất gói, thời gian phản hồi < 100ms.

> **📸 Ảnh chụp:** Chụp kết quả `ping` từ Master → Worker1, Master → Worker2.

---

## 4. Thiết Lập SSH (SSH Không Mật Khẩu)

### 4.1 Tại sao cần SSH

Các script `start-dfs.sh` và `start-yarn.sh` của Hadoop sử dụng SSH để **khởi động tiến trình từ xa** trên các Worker. Nếu không có SSH không mật khẩu, Master không thể điều phối cụm.

### 4.2 Tạo khóa SSH trên Master

```bash
# Chỉ chạy trên Master
ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa -N ""
```

Lệnh này tạo ra:
- `~/.ssh/id_rsa` (khóa riêng — giữ trên Master)
- `~/.ssh/id_rsa.pub` (khóa công — phân phối cho Worker)

### 4.3 Phân phối khóa công đến tất cả các node

```bash
# Từ Master, copy khóa đến chính nó (cần thiết cho NameNode)
ssh-copy-id -i ~/.ssh/id_rsa.pub $(whoami)@master

# Copy khóa đến Worker 1
ssh-copy-id -i ~/.ssh/id_rsa.pub $(whoami)@worker1

# Copy khóa đến Worker 2
ssh-copy-id -i ~/.ssh/id_rsa.pub $(whoami)@worker2
```

> [!IMPORTANT]
> **Tên user** phải giống nhau trên tất cả các node (ví dụ: đều dùng user `hadoop` hoặc đều dùng user mặc định). Nếu tên user khác nhau, chỉ rõ: `ssh-copy-id user@worker1`.

### 4.4 Kiểm tra SSH không mật khẩu

```bash
# Từ Master, test SSH đến từng node (KHÔNG được hỏi mật khẩu)
ssh master "echo 'SSH den master OK'"
ssh worker1 "echo 'SSH den worker1 OK'"
ssh worker2 "echo 'SSH den worker2 OK'"
```

**Kết quả mong đợi:** Mỗi lệnh in ra thông báo ngay lập tức, KHÔNG hỏi mật khẩu.

> **📸 Ảnh chụp:** Chụp kết quả test SSH cho thấy không yêu cầu mật khẩu.

### 4.5 Xử lý lỗi SSH

| Triệu chứng | Nguyên nhân | Cách sửa |
|:---|:---|:---|
| `Permission denied (publickey)` | Khóa chưa được copy đúng | Chạy lại `ssh-copy-id`, kiểm tra `~/.ssh/authorized_keys` trên máy đích |
| `Connection refused` | SSH server chưa chạy | `sudo systemctl start sshd` (Arch) hoặc `sudo service ssh start` (Ubuntu) |
| `Connection timed out` | Tường lửa chặn cổng 22 | Tắt firewall hoặc thêm rule cho cổng 22 |
| `Host key verification failed` | Kết nối lần đầu | Xóa entry cũ trong `~/.ssh/known_hosts` hoặc dùng `ssh -o StrictHostKeyChecking=no` |

---

## 5. Cấu Hình Hadoop (QUAN TRỌNG)

> [!IMPORTANT]
> Tất cả file cấu hình nằm trong `$HADOOP_HOME/etc/hadoop/`. Sửa trên **Master trước**, rồi copy sang Worker (Mục 6).

### 5.1 `core-site.xml`

Sửa file `$HADOOP_HOME/etc/hadoop/core-site.xml`:

```xml
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://master:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/home/${user.name}/hadoop-data/tmp</value>
    </property>
</configuration>
```

**Giải thích:**
- `fs.defaultFS` → cho TẤT CẢ node biết NameNode nằm ở đâu (Master, cổng 9000)
- `hadoop.tmp.dir` → thư mục gốc cho dữ liệu tạm thời của Hadoop

### 5.2 `hdfs-site.xml`

Sửa file `$HADOOP_HOME/etc/hadoop/hdfs-site.xml`:

```xml
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file:///home/${user.name}/hadoop-data/namenode</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file:///home/${user.name}/hadoop-data/datanode</value>
    </property>
</configuration>
```

**Giải thích:**
- `dfs.replication=2` → mỗi block dữ liệu được sao chép sang 2 DataNode (ta có đúng 2 Worker)
- `dfs.namenode.name.dir` → nơi NameNode lưu metadata (chỉ dùng trên Master)
- `dfs.datanode.data.dir` → nơi DataNode lưu block dữ liệu thực (chỉ dùng trên Worker)

### 5.3 `yarn-site.xml`

Sửa file `$HADOOP_HOME/etc/hadoop/yarn-site.xml`:

```xml
<configuration>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>master</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services.mapreduce_shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
    </property>
</configuration>
```

**Giải thích:**
- `yarn.resourcemanager.hostname=master` → cho tất cả NodeManager biết nơi đăng ký
- `mapreduce_shuffle` → bật dịch vụ shuffle cần thiết cho MapReduce

### 5.4 `mapred-site.xml`

Sửa file `$HADOOP_HOME/etc/hadoop/mapred-site.xml`:

```xml
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

### 5.5 File `workers`

Sửa file `$HADOOP_HOME/etc/hadoop/workers`:

```
worker1
worker2
```

> [!WARNING]
> - **KHÔNG** thêm `master` vào file này (trừ khi muốn Master cũng là DataNode)
> - **KHÔNG** để `localhost` trong file — xóa hoặc thay thế nó
> - Mỗi hostname phải nằm trên **một dòng riêng**, không có khoảng trắng thừa

> **📸 Ảnh chụp:** Chụp kết quả `cat` của cả 4 file XML và file `workers`.

---

## 6. Đồng Bộ Cấu Hình Giữa Các Node

### 6.1 Tạo thư mục dữ liệu (TẤT CẢ CÁC NODE)

```bash
# Trên Master:
mkdir -p ~/hadoop-data/namenode
mkdir -p ~/hadoop-data/tmp

# Trên mỗi Worker:
mkdir -p ~/hadoop-data/datanode
mkdir -p ~/hadoop-data/tmp
```

### 6.2 Sao chép cấu hình từ Master sang Worker

Từ **Master**, chạy:

```bash
# Copy toàn bộ thư mục cấu hình Hadoop sang Worker 1
scp -r $HADOOP_HOME/etc/hadoop/* worker1:$HADOOP_HOME/etc/hadoop/

# Copy toàn bộ thư mục cấu hình Hadoop sang Worker 2
scp -r $HADOOP_HOME/etc/hadoop/* worker2:$HADOOP_HOME/etc/hadoop/
```

### 6.3 Kiểm tra cấu hình trên Worker

SSH vào từng Worker để kiểm tra:

```bash
ssh worker1 "cat \$HADOOP_HOME/etc/hadoop/core-site.xml"
ssh worker2 "cat \$HADOOP_HOME/etc/hadoop/core-site.xml"
```

**Xác nhận `fs.defaultFS` hiển thị `hdfs://master:9000`** trên cả hai Worker.

### 6.4 Các lỗi thường gặp khi đồng bộ

| Lỗi | Hậu quả | Cách phòng tránh |
|:---|:---|:---|
| Quên copy cấu hình sau khi sửa | Worker vẫn dùng cấu hình cũ/mặc định | Luôn `scp` sau MỌI thay đổi cấu hình |
| Đường dẫn `JAVA_HOME` khác nhau giữa Arch và Ubuntu | Hadoop không khởi động được trên Worker | Đặt đúng `JAVA_HOME` trong `hadoop-env.sh` trên từng node riêng |
| File `workers` vẫn còn `localhost` | Master khởi động DataNode trên chính nó thay vì Worker | Xóa `localhost`, chỉ thêm `worker1` và `worker2` |
| Hostname không phân giải được | `start-dfs.sh` báo lỗi "unknown host" | Kiểm tra `/etc/hosts` trên TẤT CẢ node |

> [!IMPORTANT]
> Sau khi copy cấu hình, phải **sửa lại thủ công** `JAVA_HOME` trong `$HADOOP_HOME/etc/hadoop/hadoop-env.sh` trên mỗi Worker nếu đường dẫn Java khác với Master.

---

## 7. Khởi Tạo Cụm

### 7.1 Format NameNode (CHỈ TRÊN MASTER — Chạy MỘT LẦN DUY NHẤT)

```bash
# CHỈ CHẠY TRÊN MASTER
hdfs namenode -format
```

**Kết quả mong đợi (các dòng cuối):**

```
... INFO common.Storage: Storage directory /home/xxx/hadoop-data/namenode has been successfully formatted.
... INFO namenode.FSNamesystem: Retry cache on namenode is enabled
... Exiting with status 0
```

> [!CAUTION]
> - Chạy lệnh này **CHỈ trên Master**
> - Chạy **MỘT LẦN DUY NHẤT** (format lại sẽ xóa toàn bộ dữ liệu HDFS)
> - Nếu thấy `status 0` → thành công. Nếu `status 1` → có lỗi cần kiểm tra

> **📸 Ảnh chụp:** Chụp thông báo "successfully formatted".

### 7.2 Khởi động HDFS (từ Master)

```bash
# TRÊN MASTER
start-dfs.sh
```

**Kết quả mong đợi:**

```
Starting namenodes on [master]
Starting datanodes
Starting secondary namenodes [master]
```

Script sẽ SSH vào `worker1` và `worker2` để khởi động tiến trình DataNode.

### 7.3 Khởi động YARN (từ Master)

```bash
# TRÊN MASTER
start-yarn.sh
```

**Kết quả mong đợi:**

```
Starting resourcemanager
Starting nodemanagers
```

> **📸 Ảnh chụp:** Chụp output của cả `start-dfs.sh` và `start-yarn.sh`.

---

## 8. Xác Minh & Kiểm Tra

### 8.1 Kiểm tra tiến trình bằng `jps`

#### Trên Master:

```bash
jps
```

**Các tiến trình cần thấy:**

```
12345 NameNode
12346 ResourceManager
12347 SecondaryNameNode
12348 Jps
```

#### Trên mỗi Worker:

```bash
ssh worker1 "jps"
ssh worker2 "jps"
```

**Các tiến trình cần thấy (trên mỗi Worker):**

```
23456 DataNode
23457 NodeManager
23458 Jps
```

> **📸 Ảnh chụp:** Chụp kết quả `jps` trên Master, Worker1 và Worker2.

### 8.2 Kiểm tra Web UI

Mở trình duyệt trên Master (hoặc bất kỳ máy nào cùng mạng):

| URL | Kiểm tra gì |
|:---|:---|
| `http://master:9870` | HDFS Web UI — Click tab "Datanodes" → phải thấy **2 Live Nodes** |
| `http://master:8088` | YARN Web UI — Click "Nodes" → phải thấy **2 Active Nodes** |

> [!TIP]
> Nếu truy cập từ máy khác, thay `master` bằng IP thực tế của Master (ví dụ: `http://192.168.1.100:9870`).

> **📸 Ảnh chụp:** Chụp cả hai trang Web UI:
> 1. Trang HDFS Datanodes hiển thị 2 live node
> 2. Trang YARN Nodes hiển thị 2 active node

### 8.3 Kiểm tra chức năng (Thao tác HDFS)

```bash
# Tạo thư mục
hdfs dfs -mkdir -p /test

# Tạo file test trên máy local
echo "hello hadoop distributed cluster" > /tmp/testfile.txt

# Tải lên HDFS
hdfs dfs -put /tmp/testfile.txt /test/

# Liệt kê file trên HDFS
hdfs dfs -ls /test/

# Đọc lại nội dung file
hdfs dfs -cat /test/testfile.txt

# Kiểm tra hệ số nhân bản
hdfs dfs -stat "%r" /test/testfile.txt
# Kết quả mong đợi: 2 (khớp với cấu hình dfs.replication)

# Dọn dẹp
hdfs dfs -rm -r /test
```

> **📸 Ảnh chụp:** Chụp các lệnh `put`, `ls` và `cat` cùng kết quả.

---

## 9. Các Lỗi Thường Gặp & Cách Xử Lý

### 9.1 Lỗi SSH

| Triệu chứng | Nguyên nhân | Cách sửa |
|:---|:---|:---|
| `start-dfs.sh` treo hoặc hỏi mật khẩu | SSH không mật khẩu chưa được thiết lập | Chạy lại `ssh-copy-id`, kiểm tra bằng `ssh worker1` |
| `ssh: connect to host worker1 port 22: Connection refused` | SSH server chưa chạy trên Worker | Khởi động SSH: `sudo service ssh start` (Ubuntu) |
| `WARN: UNPROTECTED PRIVATE KEY FILE` | Quyền file `~/.ssh/id_rsa` quá mở | `chmod 600 ~/.ssh/id_rsa` |

### 9.2 Lỗi phân giải hostname

| Triệu chứng | Nguyên nhân | Cách sửa |
|:---|:---|:---|
| `java.net.UnknownHostException: master` | `/etc/hosts` chưa cấu hình trên node đó | Thêm entry vào `/etc/hosts` trên TẤT CẢ node |
| `Connection refused to master:9000` | NameNode chưa chạy HOẶC bind sai giao diện mạng | Kiểm tra `jps` trên Master; xác nhận NameNode đã khởi động |

### 9.3 DataNode không xuất hiện

| Triệu chứng | Nguyên nhân | Cách sửa |
|:---|:---|:---|
| Web UI hiển thị 0 Live DataNode | DataNode bị crash ngầm | Kiểm tra log: `cat $HADOOP_HOME/logs/hadoop-*-datanode-*.log` |
| DataNode log: `Incompatible clusterIDs` | NameNode đã format lại nhưng DataNode còn dữ liệu cũ | Xóa thư mục dữ liệu DataNode trên Worker: `rm -rf ~/hadoop-data/datanode/*`, rồi khởi động lại |
| DataNode log: `Problem connecting to server: master:9000` | `core-site.xml` trên Worker không trỏ đến Master | Sửa `fs.defaultFS` → `hdfs://master:9000` trong cấu hình Worker |

### 9.4 Lỗi YARN

| Triệu chứng | Nguyên nhân | Cách sửa |
|:---|:---|:---|
| NodeManager không xuất hiện trên YARN UI | `yarn.resourcemanager.hostname` cấu hình sai | Kiểm tra `yarn-site.xml` có `master` là hostname ResourceManager |
| `Container killed by YARN for exceeding memory` | Bộ nhớ mặc định quá thấp | Tăng `yarn.nodemanager.resource.memory-mb` trong `yarn-site.xml` |

### 9.5 Vị trí file log

```bash
# Tất cả log của Hadoop nằm ở đây:
ls $HADOOP_HOME/logs/

# Các file log quan trọng:
# hadoop-<user>-namenode-<hostname>.log     → Lỗi NameNode
# hadoop-<user>-datanode-<hostname>.log     → Lỗi DataNode
# yarn-<user>-resourcemanager-<hostname>.log → Lỗi YARN
# yarn-<user>-nodemanager-<hostname>.log    → Lỗi NodeManager
```

---

## 10. Kế Hoạch Chụp Ảnh Minh Chứng

| Bước | Lệnh / Thao tác | Nội dung cần chụp | Kết quả mong đợi |
|:---|:---|:---|:---|
| **3.2** | `ip addr` trên tất cả node | Terminal hiển thị IP | Mỗi node có IP riêng, cùng subnet |
| **3.5** | `ping -c 3 worker1` từ Master | Kết quả ping | 0% mất gói |
| **4.4** | `ssh worker1 "echo OK"` | Test SSH | In `OK` mà không hỏi mật khẩu |
| **5.1** | `cat $HADOOP_HOME/etc/hadoop/core-site.xml` | Nội dung cấu hình | Hiển thị `hdfs://master:9000` |
| **5.2** | `cat $HADOOP_HOME/etc/hadoop/hdfs-site.xml` | Nội dung cấu hình | Hiển thị replication=2, đúng thư mục |
| **5.3** | `cat $HADOOP_HOME/etc/hadoop/yarn-site.xml` | Nội dung cấu hình | Hiển thị ResourceManager=master |
| **5.5** | `cat $HADOOP_HOME/etc/hadoop/workers` | File workers | Hiển thị `worker1` và `worker2` |
| **7.1** | `hdfs namenode -format` | Output format | "successfully formatted" |
| **7.2** | `start-dfs.sh` | Output khởi động | "Starting namenodes... Starting datanodes" |
| **7.3** | `start-yarn.sh` | Output khởi động | "Starting resourcemanager... nodemanagers" |
| **8.1** | `jps` trên Master | Danh sách tiến trình | Có NameNode, ResourceManager |
| **8.1** | `jps` trên Worker (qua SSH) | Danh sách tiến trình | Có DataNode, NodeManager |
| **8.2** | Trình duyệt: `http://master:9870` | HDFS Web UI | 2 Live Datanodes |
| **8.2** | Trình duyệt: `http://master:8088` | YARN Web UI | 2 Active Nodes |
| **8.3** | `hdfs dfs -put` + `hdfs dfs -ls` | Thao tác file | File hiển thị trong HDFS |

---

## 11. Quy Trình Tắt Cụm

Khi hoàn thành, tắt cụm **theo thứ tự sau**:

```bash
# Trên Master:
stop-yarn.sh
stop-dfs.sh
```

Kiểm tra tất cả đã dừng:

```bash
jps
# Chỉ nên thấy: Jps
```

---

## 12. Bảng Tham Chiếu Nhanh — Các Lệnh Thường Dùng

```bash
# ─── KHỞI ĐỘNG ───
start-dfs.sh          # Khởi động HDFS (NameNode + DataNode)
start-yarn.sh         # Khởi động YARN (ResourceManager + NodeManager)

# ─── TẮT ───
stop-yarn.sh
stop-dfs.sh

# ─── KIỂM TRA TRẠNG THÁI ───
jps                                    # Kiểm tra tiến trình Java
hdfs dfsadmin -report                  # Báo cáo sức khỏe HDFS
yarn node -list                        # Liệt kê các YARN node

# ─── THAO TÁC HDFS ───
hdfs dfs -mkdir /duong-dan             # Tạo thư mục
hdfs dfs -put file-local /duong-dan/   # Tải file lên
hdfs dfs -ls /duong-dan                # Liệt kê file
hdfs dfs -cat /duong-dan/file          # Đọc file
hdfs dfs -rm -r /duong-dan             # Xóa

# ─── WEB UI ───
# http://master:9870   → HDFS
# http://master:8088   → YARN
```

---

## Phụ Lục: Bảng Kiểm Tra Triển Khai (Checklist)

Dùng bảng này trong quá trình triển khai. Đánh dấu ✓ khi hoàn thành từng mục.

- [ ] **Tất cả laptop kết nối cùng mạng**
- [ ] **Đã ghi lại địa chỉ IP** (tất cả khác nhau, cùng subnet)
- [ ] **Java đã cài và kiểm tra** trên tất cả node
- [ ] **Hadoop đã cài** (cùng phiên bản) trên tất cả node
- [ ] **Biến môi trường đã thiết lập** (`JAVA_HOME`, `HADOOP_HOME`, `PATH`) trên tất cả node
- [ ] **`/etc/hosts` đã cấu hình** trên tất cả node với master, worker1, worker2
- [ ] **Hostname đã đặt** trên từng node
- [ ] **Test `ping` thành công** giữa tất cả các node
- [ ] **SSH server đang chạy** trên tất cả node
- [ ] **Đã tạo SSH key** trên Master
- [ ] **Đã phân phối SSH key** đến master, worker1, worker2
- [ ] **SSH không mật khẩu đã kiểm tra** từ Master đến tất cả node
- [ ] **`core-site.xml` đã cấu hình** (fs.defaultFS = hdfs://master:9000)
- [ ] **`hdfs-site.xml` đã cấu hình** (replication=2, thư mục dữ liệu)
- [ ] **`yarn-site.xml` đã cấu hình** (ResourceManager = master)
- [ ] **`mapred-site.xml` đã cấu hình** (framework = yarn)
- [ ] **File `workers` đã cấu hình** (worker1, worker2)
- [ ] **`hadoop-env.sh` JAVA_HOME** đã đặt đúng theo từng OS
- [ ] **Cấu hình đã copy** từ Master sang tất cả Worker qua `scp`
- [ ] **JAVA_HOME đã sửa lại** trên Worker (nếu đường dẫn Arch ≠ Ubuntu)
- [ ] **Thư mục dữ liệu đã tạo** trên tất cả node
- [ ] **NameNode đã format** (chỉ Master, một lần)
- [ ] **`start-dfs.sh` chạy thành công**
- [ ] **`start-yarn.sh` chạy thành công**
- [ ] **`jps` đã kiểm tra** trên Master (NameNode, ResourceManager)
- [ ] **`jps` đã kiểm tra** trên Worker (DataNode, NodeManager)
- [ ] **HDFS Web UI** hiển thị 2 Live DataNode
- [ ] **YARN Web UI** hiển thị 2 Active Node
- [ ] **Kiểm tra chức năng HDFS** (mkdir, put, ls, cat) thành công
- [ ] **Đã chụp tất cả ảnh minh chứng**
