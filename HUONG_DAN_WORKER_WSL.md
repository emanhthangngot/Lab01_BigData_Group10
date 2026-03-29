# 💻 HƯỚNG DẪN CÀI ĐẶT HADOOP WORKER (WSL-WSL2) - PHIÊN BẢN CẢI TIẾN

Tài liệu này tập trung vào việc khắc phục các hạn chế về mạng của WSL2 thông qua Tailscale để Master có thể điều khiển trực tiếp các node con.

---

## 🛠️ GIAI ĐOẠN 0: YÊU CẦU HỆ THỐNG & CÀI ĐẶT PHẦN MỀM

### 1. Phiên bản Java (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-8-jdk -y
# Kiểm tra JAVA_HOME:
# Thông thường là /usr/lib/jvm/java-8-openjdk-amd64
```

### 2. Cài đặt Hadoop 3.3.6
```bash
wget https://downloads.apache.org/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz
tar -xzvf hadoop-3.3.6.tar.gz
mv hadoop-3.3.6 ~/hadoop
```

### 3. Biến môi trường (`~/.bashrc`)
Thêm các dòng này vào cuối file:
```bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export HADOOP_HOME=~/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```
*Lưu ý: Nhớ chạy `source ~/.bashrc` sau khi lưu.*

---

## 🏗️ GIAI ĐOẠN 1: THIẾT LẬP MẠNG TAILSCALE (TRONG WSL)

**Khuyên dùng**: Cài Tailscale **trực tiếp trong WSL2** thay vì dùng Tailscale Windows. Điều này giúp WSL có IP riêng biệt trong mạng Tailnet.

### 1. Cài đặt Tailscale trong WSL2
```bash
curl -fsSL https://tailscale.com/install.sh | sh
```

### 2. Khởi động Tailscale (Không cần systemd)
Nếu WSL của bạn chưa bật systemd, hãy khởi động thủ công:
```bash
sudo tailscaled &
sudo tailscale up
```
Lấy IP của máy: `tailscale ip -4` (Giả sử `100.64.0.2`). Gửi IP này cho Master.

### 3. Cấu hình `/etc/hosts`
Sửa file `/etc/hosts` và thêm:
```text
100.64.0.1  master
```

---

## 🔑 GIAI ĐOẠN 2: FIX LỖI JAVA TRONG SSH (RẤT QUAN TRỌNG)

Khi Master dùng lệnh `start-dfs.sh` để gọi Worker, nó sẽ không "nhìn thấy" biến môi trường trong `.bashrc`. Bạn phải ép Java Home vào file này:

```bash
echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" >> ~/hadoop/etc/hadoop/hadoop-env.sh
```

---

## ⚙️ GIAI ĐOẠN 3: ĐỒNG BỘ CẤU HÌNH TỪ MASTER

Sau khi Master đã chỉnh sửa xong các file XML, bạn có thể copy chúng sang máy mình tại `~/hadoop/etc/hadoop/`. 

> [!CAUTION]
> Đảm bảo đường dẫn `JAVA_HOME` trong `hadoop-env.sh` của Worker trỏ đúng vào bản Java của máy bạn (thường khác với Master).

### Kiểm tra quyền ghi cho HDFS
```bash
mkdir -p ~/hadoop_data/datanode
chmod -R 755 ~/hadoop_data
```

---

## 🚀 GIAI ĐOẠN 4: KHỞI ĐỘNG VÀ THEO DÕI

1. **Khởi động SSH Server**: `sudo service ssh start`
2. **Theo dõi log**:
```bash
tail -f ~/hadoop/logs/hadoop-*-datanode-*.log
```

> [!TIP]
> Nếu Master chạy `start-dfs.sh` mà DataNode trên máy bạn không lên, hãy thử chạy lệnh sau để debug:
> `hdfs --daemon start datanode`
> Nếu nó báo lỗi liên quan đến **Version ID**, hãy xóa sạch folder `~/hadoop_data/` và thử lại.
