# KẾ HOẠCH THỰC THI – LAB 01: GIỚI THIỆU HỆ SINH THÁI HADOOP

> Tài liệu đặc tả kỹ thuật dành cho toàn bộ thành viên nhóm và hệ thống AI Agent hỗ trợ.
> Phiên bản cập nhật lần cuối: 21/03/2026.

---

## I. TỔNG QUAN DỰ ÁN

| Hạng mục | Nội dung |
| :--- | :--- |
| **Môn học** | Nhập môn Dữ liệu lớn (Introduction to Big Data) |
| **Mục tiêu** | Tự cài đặt cụm Hadoop, thao tác HDFS qua dòng lệnh, giải quyết bài toán WordCount bằng Scala |
| **Quy mô nhóm** | 4 thành viên (A, B, C, D) |
| **Yêu cầu cài đặt** | Mỗi cá nhân tự cài 1 cụm Pseudo-distributed. Nhóm phối hợp cài thêm 3 cụm Fully-distributed |
| **Ràng buộc nộp bài** | Cấu trúc thư mục nghiêm ngặt, minh chứng ảnh chụp bắt buộc, IP phải độc lập (trùng IP = 0 điểm) |

---

## II. ĐẶC TẢ YÊU CẦU

### 1. Thiết lập Hadoop Cluster và HDFS (4,5 điểm)

Toàn bộ 4 thành viên phải tự cài đặt cụm **Pseudo-distributed** trên môi trường cá nhân (có IP riêng biệt) và hoàn thành các tác vụ sau:

- **Chuẩn bị và cài đặt khối nhân Hadoop (Pseudo-distributed):**
  - Cài đặt Java (Khuyến nghị bản 8/11) và công cụ Scala.
  - Tải gói phát hành lõi Hadoop qua `wget`/trình duyệt web, thực hiện giải nén.
  - Định hình môi trường (`JAVA_HOME`, `HADOOP_HOME`, biến `PATH`) trên hệ điều hành.
  - Sửa đổi cấu hình XML (`core-site.xml`, `hdfs-site.xml`) đáp ứng chuẩn Pseudo-distributed.
  - Khởi tạo tiến trình chức năng (NameNode, DataNode, ResourceManager, NodeManager).
- **Thao tác HDFS và phân quyền:**
  - Tạo thư mục gốc: `hdfs dfs -mkdir /hcmus`
  - Tạo tài khoản Linux nội bộ: `khtn_<StudentID>`
  - Tạo thư mục con: `hdfs dfs -mkdir /hcmus/<StudentID>`
  - Tải dữ liệu lên HDFS: `hdfs dfs -put <file> /hcmus/<StudentID>/`
  - Cấp quyền: `hdfs dfs -chmod 744 /hcmus/<StudentID>`
  - Chuyển quyền sở hữu: `hdfs dfs -chown khtn_<StudentID> /hcmus/<StudentID>`
- **Chạy file xác thực (bắt buộc — thiếu sẽ bị trừ 4,5 điểm):**
  - Lệnh: `java -jar /path/to/hadoop-test.jar <HDFS_PORT> /hcmus/<StudentID>`
  - Kết quả: file `<StudentID>_verification.txt` được sinh ra tại thư mục thực thi.
  - Nếu gặp lỗi `Exception in thread main null... Main getMac`: ghi nhận, tra cứu nguyên nhân, sửa lỗi và ghi toàn bộ quá trình vào báo cáo.

### 2. Bài toán WordCount (5,0 điểm)

- **Yêu cầu:** Đếm số lượng từ bắt đầu bằng các chữ cái: **f, i, t, h, c, m, u, s**.
- **Ràng buộc logic:** Không phân biệt hoa thường. Bao gồm cả ký tự đặc biệt liền kề.
- **Ràng buộc kỹ thuật:**
  - Ngôn ngữ bắt buộc: **Scala** (dùng Python sẽ bị 0 điểm phần ngôn ngữ).
  - Thuật toán: MapReduce hoặc Spark RDD.
  - Bắt buộc đọc dữ liệu trực tiếp từ HDFS (không đọc file local), ví dụ: `sc.textFile("hdfs://<namenode-host>:<port>/hcmus/<StudentID>/words.txt")`.
  - Cấm sử dụng Regular Expression trên toàn bộ file (không tuân thủ nguyên tắc xử lý phân tán).
  - Nên tách từ bằng phương pháp đơn giản (ví dụ: `split(" ")` + lọc chuỗi rỗng) thay vì dùng `split("\\s+")` để tránh bị quy vào sử dụng regex.
  - Chuẩn hóa chữ thường trước khi kiểm tra ký tự đầu, ví dụ: `word.toLowerCase.startsWith(...)` để đảm bảo đúng yêu cầu case-insensitive.
  - Khi xuất kết quả TSV phải dùng đúng ký tự tab `\t` giữa key và value; không thay bằng khoảng trắng.
- **Dữ liệu đầu vào:** File `words.txt` trên HDFS.
- **Dữ liệu đầu ra:** File định dạng TSV, ví dụ:
  ```text
  f	1
  i	3
  t	5
  ```

### 3. Báo cáo và nộp bài

- Mọi lệnh và lỗi phải được chụp ảnh màn hình kèm thời gian hệ thống và địa chỉ IP.
- Mỗi thành viên viết báo cáo bằng lời văn cá nhân, mô tả lỗi và cách xử lý theo cách riêng, tránh trùng lặp nội dung.
- File nén duy nhất: `<RepresentativeID>.zip`.

---

## III. PHÂN CÔNG NHIỆM VỤ VÀ QUY HOẠCH HỆ THỐNG

### 1. Danh sách thành viên

| Mã | Họ và Tên | Hệ điều hành |
| :---: | :--- | :--- |
| **A** | Lê Xuân Trí | Arch Linux (native) |
| **B** | Nguyễn Hồ Anh Tuấn | Windows |
| **C** | Trần Hữu Kim Thành | Windows |
| **D** | Nguyễn Lê Trung Trực | Windows |

### 2. Kế hoạch cài đặt Pseudo-distributed (bắt buộc cho mỗi cá nhân)

Toàn bộ 4 thành viên **bắt buộc phải tự cài đặt** một cụm Hadoop ở chế độ Pseudo-distributed trên máy tính cá nhân của mình. Đây là điều kiện tiên quyết để:

- Sinh được file `<StudentID>_verification.txt` thông qua lệnh chạy file JAR.
- Có hệ thống HDFS nội bộ để đẩy dữ liệu `words.txt` và chạy chương trình WordCount.
- Thu thập ảnh chụp minh chứng các bước thao tác cho báo cáo cá nhân.

Không thành viên nào được ủy thác phần việc này cho người khác.

### 3. Kế hoạch cài đặt Fully-distributed (3 cụm — phối hợp tập thể)

Theo quy định, nhóm 4 người cần hoàn thành **n − 1 = 3** cụm Fully-distributed. Trước khi đi vào chi tiết từng cụm, cần nắm rõ hai loại hình cài đặt mà đề bài phân biệt:

#### Phân biệt hai loại hình Fully-distributed

**Loại 1 — Máy ảo (Virtual Machines):**
Nhiều node Hadoop được tạo ra và hoạt động bên trong **cùng một máy tính vật lý duy nhất**. Các node này có thể là Docker container, máy ảo VirtualBox/VMware, hoặc nhiều instance WSL trên cùng một laptop. Dù có bao nhiêu node đi nữa, nếu tất cả đều nằm trên cùng một thiết bị phần cứng thì đó là cụm trên máy ảo. Yêu cầu tối thiểu **2 Worker** để được tính điểm thưởng.

**Loại 2 — Máy vật lý (Physically Separated Machines):**
Các node Hadoop chạy trên **2 hoặc nhiều thiết bị phần cứng riêng biệt** (laptop, PC, cloud instance,...) được kết nối với nhau qua mạng WiFi hoặc LAN. Ví dụ: laptop A làm Master, laptop B làm Worker — đó là 2 máy vật lý thực sự. Yêu cầu tối thiểu **2 máy vật lý** và **2 Worker** để được tính điểm thưởng.

#### Tổng quan phân loại 3 cụm của nhóm

| Cụm | Loại hình | Phương pháp | Người phụ trách | Điểm thưởng hướng tới |
| :---: | :--- | :--- | :---: | :--- |
| **Cụm 1** | **Máy vật lý** (Physical) | Kết nối 2+ laptop qua mạng LAN | A (Master) + B/C/D (Worker) | Bonus máy vật lý |
| **Cụm 2** | **Máy ảo** (Virtual) | Nhiều instance WSL trên 1 laptop | B | Bonus máy ảo |
| **Cụm 3** | **Máy ảo** (Virtual) | Docker/VM trên 1 laptop | C | Bonus máy ảo |

Dưới đây là phân công chi tiết cho từng cụm:

---

#### Cụm 1: Máy vật lý — Physical Machines (Mục tiêu: Bonus máy vật lý)

> **Phân loại:** Đây là cụm thuộc loại **Physically Separated Machines** vì sử dụng 2 hoặc nhiều laptop/PC thực sự khác nhau, kết nối qua mạng LAN chung.
> Yêu cầu tối thiểu 2 máy vật lý khác nhau và 2 Worker để được tính điểm thưởng.

**Kiến trúc mạng:**

| Vai trò | Thiết bị | Hệ điều hành | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Master Node** (NameNode + ResourceManager) | Laptop của **A** | Arch Linux | Máy Linux native giúp cấu hình SSH Server, mở port và thiết lập mạng dễ dàng, không bị Firewall Windows chặn |
| **Worker Node 1** (DataNode + NodeManager) | Laptop của **B** hoặc **C** hoặc **D** | Windows (chạy WSL/VM bên trong) | Cài Ubuntu trên WSL/VM, cấu hình card mạng Bridged để nhận IP cùng dải LAN với Master |
| **Worker Node 2** (DataNode + NodeManager) | Laptop của thành viên còn lại | Windows (chạy WSL/VM bên trong) | Tương tự Worker 1 |

**Quy trình triển khai:**

1. Tất cả các máy kết nối chung một mạng WiFi hoặc LAN (có thể dùng mobile hotspot).
2. Máy A (Arch Linux) cài đặt và khởi chạy Hadoop ở vai trò Master. Cấu hình `core-site.xml` trỏ `fs.defaultFS` về IP thực của A trong mạng LAN.
3. Các máy Worker cài Hadoop trong WSL/VM, cấu hình card mạng là **Bridged Adapter** để lấy IP cùng dải mạng vật lý với Master.
4. Trên tất cả các máy: cấu hình `/etc/hosts` để hostname nội bộ trỏ đúng về IP tương ứng.
5. Thiết lập SSH không mật khẩu (passwordless SSH) giữa Master và các Worker.
6. Cấu hình file `workers` (hoặc `slaves`) trên Master, liệt kê hostname của các Worker.
7. Khởi chạy `start-dfs.sh` và `start-yarn.sh` từ Master, kiểm tra bằng `jps` trên tất cả các máy.
8. Truy cập WebUI (`http://<IP_Master>:9870`) để xác nhận đủ Live DataNode.

**Người chịu trách nhiệm chính:** Thành viên **A** (điều phối tổng thể, giữ vai Master).
**Hỗ trợ:** Thành viên **D** phụ trách rà soát cấu hình mạng LAN, kiểm tra kết nối SSH và hostname.

**Phương án dự phòng (Plan B):** Nếu mạng LAN không ổn định hoặc không thể kết nối vật lý được, nhóm chuyển sang triển khai Docker multi-container trên máy A thay thế (vẫn tính là fully-distributed mode trên VM).

---

#### Cụm 2: WSL — Máy ảo (Thành viên B)

> **Phân loại:** Đây là cụm thuộc loại **Virtual Machines** vì tất cả các node (Master + Worker) đều chạy bên trong cùng một laptop duy nhất của B thông qua nhiều instance WSL.

Thành viên **B** chịu trách nhiệm cài đặt một cụm Fully-distributed trên nền tảng WSL (Windows Subsystem for Linux).

- Cài đặt Ubuntu trên WSL2, tạo tối thiểu 3 instance (1 Master + 2 Worker) bằng cách export/import WSL image.
- Cấu hình Hadoop YARN trên cả 3 instance, đảm bảo chúng giao tiếp được với nhau qua mạng nội bộ WSL.
- Khởi chạy cluster và chụp ảnh minh chứng đầy đủ (jps, WebUI, Live Nodes ≥ 2).

---

#### Cụm 3: Docker/VM — Máy ảo (Thành viên C)

> **Phân loại:** Đây là cụm thuộc loại **Virtual Machines** vì tất cả các container hoặc máy ảo đều chạy bên trong cùng một laptop duy nhất của C.

Thành viên **C** chịu trách nhiệm cài đặt một cụm Fully-distributed sử dụng máy ảo (VirtualBox/VMware) hoặc Docker.

- Tạo tối thiểu 3 container/VM (1 Master + 2 Worker).
- Cấu hình mạng nội bộ giữa các container/VM để chúng giao tiếp được với nhau.
- Khởi chạy cluster và chụp ảnh minh chứng đầy đủ (jps, WebUI, Live Nodes ≥ 2).

---

#### Vai trò thành viên D

Thành viên **D** không phụ trách riêng một cụm Fully-distributed. Thay vào đó, D đảm nhận các nhiệm vụ hỗ trợ:

- Hỗ trợ A triển khai Cụm 1 (vật lý): rà soát hostname, cấu hình SSH, kiểm tra kết nối mạng.
- Thu thập và đối chiếu ảnh chụp `ip addr` / `ifconfig` của toàn bộ thành viên, lập bảng xác minh IP không trùng lặp.
- Quản lý tiến độ so sánh chéo mã nguồn Scala giữa các thành viên.

---

### 4. Cấu trúc thư mục nộp bài

```text
<RepresentativeID>.zip
└── <RepresentativeID>/
    ├── <StudentID_A>/
    │   ├── docs/
    │   │   ├── Report.pdf
    │   │   └── <StudentID_A>_verification.txt
    │   └── src/
    │       └── WordCount/
    │           ├── results.txt
    │           ├── WordCount.scala
    │           └── README.md
    ├── <StudentID_B>/  (cấu trúc tương tự)
    ├── <StudentID_C>/  (cấu trúc tương tự)
    └── <StudentID_D>/  (cấu trúc tương tự)
```

### 5. Quản lý mã nguồn (Git)

- **Nhánh `main`:** Chỉ Leader (A) có quyền merge. Lưu trữ phiên bản hoàn chỉnh cuối cùng.
- **Nhánh `member/<StudentID>`:** Mỗi cá nhân tạo nhánh riêng, commit tài liệu và kết quả vào thư mục mang MSSV của mình. Hoàn thành thì mở Pull Request về `main` để Leader duyệt.
- **Chiến lược phát triển mã nguồn Scala:**
  - Mỗi thành viên (A, B, C, D) **tự lập trình một phiên bản mã nguồn WordCount.scala riêng**.
  - Nhóm tổ chức buổi đối chiếu chéo (cross-check): so sánh logic thuật toán MapReduce và xác nhận kết quả file TSV (`results.txt`) khớp nhau hoàn toàn.
  - Mỗi thành viên phải có khả năng giải thích rõ ràng các thành phần cốt lõi trong code của mình: `map`, `reduceByKey`, logic `filter`, cách xuất TSV.
  - Mỗi người tự viết `README.md` theo lời văn cá nhân.

### 6. Quy trình xác minh IP (chống trùng lặp)

1. Ngay khi bắt đầu triển khai, mỗi thành viên chạy lệnh `ip addr` hoặc `ifconfig` và gửi ảnh chụp cho Leader.
2. Leader (hoặc D) lập bảng đối chiếu IP theo mẫu:

| Thành viên | IP Private | Mạng sử dụng | Trùng lặp? |
| :---: | :--- | :--- | :---: |
| A | ... | ... | Không |
| B | ... | ... | Không |
| C | ... | ... | Không |
| D | ... | ... | Không |

3. Nếu phát hiện trùng IP: thành viên liên quan phải đổi sang mạng khác (4G, hotspot, WiFi khác) trước khi tiếp tục.

### 7. Quy trình kiểm định kết quả WordCount

- Tạo một file test nhỏ (10–20 từ) có đáp án tính tay trước.
- Upload file test lên HDFS và chạy WordCount bằng đường dẫn HDFS để xác nhận pipeline đúng từ đầu vào đến đầu ra.
- Chạy chương trình WordCount trên file test và so sánh đầu ra TSV với đáp án kỳ vọng (kiểm tra đủ 3 điều kiện: đọc từ HDFS, xử lý `toLowerCase`, định dạng tab `\t`).
- Sau khi xác nhận logic đúng, mới chạy trên file `words.txt` chính thức trên HDFS.

---

## IV. LỊCH TRÌNH THỰC THI

> **Mốc thời gian:** Hôm nay 21/03/2026 (Thứ Bảy). Mục tiêu hoàn thành: **01/04/2026**. Hạn nộp chính thức: **03/04/2026** (2 ngày dự phòng).

### Giai đoạn 1: Cài đặt Pseudo-distributed cá nhân (22/03 – 24/03)

| Ngày | Nội dung | Người chịu trách nhiệm |
| :---: | :--- | :--- |
| **22/03 (CN)** | Cài đặt Hadoop Pseudo-distributed trên máy cá nhân. Chụp ảnh IP (`ip addr`/`ifconfig`), format NameNode, start cluster (`start-dfs.sh`, `start-yarn.sh`), kiểm tra `jps` và WebUI | Tất cả (A, B, C, D) |
| **23/03 (T2)** | Hoàn thành các lệnh HDFS: mkdir, adduser, put, chmod, chown. Chạy file JAR xác thực để sinh `<StudentID>_verification.txt`. Chụp ảnh minh chứng từng bước. Gửi ảnh IP cho Leader/D để đối chiếu | Tất cả |
| **24/03 (T3)** | Xử lý lỗi phát sinh (nếu có). Bổ sung ảnh chụp troubleshooting vào báo cáo. Ai chưa xong Pseudo-distributed thì hoàn tất trong ngày | Tất cả |

### Giai đoạn 2: Cài đặt Fully-distributed — Cụm 1 Physical (25/03)

| Ngày | Nội dung | Người chịu trách nhiệm |
| :---: | :--- | :--- |
| **25/03 (T4)** | **Cả nhóm hẹn gặp mặt tại quán cà phê.** Kết nối chung mạng WiFi/LAN. A (Arch Linux) làm Master Node, các thành viên khác mang laptop Windows đến làm Worker Node. Cấu hình `/etc/hosts`, SSH không mật khẩu, file `workers`, `core-site.xml`. Khởi chạy `start-dfs.sh` và `start-yarn.sh` từ Master, kiểm tra `jps` trên tất cả máy, xác nhận Live DataNode trên WebUI. Chụp ảnh minh chứng đầy đủ | Tất cả (A, B, C, D) |

### Giai đoạn 3: Cài đặt Fully-distributed — Cụm 2 & 3 + WordCount (26/03 – 29/03)

| Ngày | Nội dung | Người chịu trách nhiệm |
| :---: | :--- | :--- |
| **26/03 (T5)** | **Cụm 2 (WSL):** B tạo instance WSL, cấu hình Hadoop YARN (1 Master + 2 Worker). **Cụm 3 (Docker/VM):** C bắt đầu triển khai Docker hoặc VM tương tự. Đồng thời mỗi người bắt đầu tự viết phiên bản `WordCount.scala` riêng | B (Cụm 2), C (Cụm 3), Tất cả (Scala) |
| **27/03 (T6)** | B và C hoàn tất cụm 2 và 3, chụp minh chứng (jps, WebUI, Live Nodes ≥ 2). Mỗi người chạy thử code Scala trên file test nhỏ (10–20 từ), so sánh kết quả với đáp án tính tay | B, C (cluster), Tất cả (Scala) |
| **28/03 (T7)** | Buổi đối chiếu chéo code Scala (họp nhóm hoặc trực tuyến): so sánh 4 phiên bản code, đối chiếu kết quả TSV, xác nhận logic đúng. Chạy chính thức trên `words.txt`, xuất file `results.txt` | Tất cả |
| **29/03 (CN)** | Dự phòng cho giai đoạn 2 & 3. Xử lý lỗi cluster còn tồn đọng. Hoàn thiện code Scala nếu chưa xong | Tất cả |

### Giai đoạn 4: Báo cáo và đóng gói (30/03 – 01/04)

| Ngày | Nội dung | Người chịu trách nhiệm |
| :---: | :--- | :--- |
| **30/03 (T2)** | Mỗi người hoàn thiện Report.pdf cá nhân: viết lời văn riêng, sắp xếp ảnh chụp, mô tả lỗi và cách xử lý, ghi chú kỹ thuật cá nhân. Mỗi người viết `README.md` riêng cho code của mình | Tất cả |
| **31/03 (T3)** | Leader QA toàn bộ: kiểm cấu trúc thư mục từng thành viên, đối chiếu bảng IP, merge nhánh Git, chạy script pre-flight check | A (Leader) |
| **01/04 (T4)** | Đóng gói file ZIP cuối cùng `<RepresentativeID>.zip`. Sửa lỗi nếu Leader phát hiện vấn đề | A (Leader) |

### Dự phòng

| Ngày | Nội dung |
| :---: | :--- |
| **02/04 (T5)** | Ngày dự phòng: sửa lỗi phút cuối, bổ sung ảnh chụp thiếu, chỉnh sửa Report nếu cần |
| **03/04 (T6)** | **HẠN NỘP CHÍNH THỨC.** Đại diện nhóm nộp file ZIP lên Moodle |

---

## V. DANH SÁCH ẢNH CHỤP BẮT BUỘC

> Tất cả 4 thành viên đều phải nộp đủ danh sách ảnh này trong Report cá nhân.
> Quy tắc chung: mở toàn màn hình, hiển thị rõ thời gian hệ thống, địa chỉ IP và kết quả lệnh.

**Phần 1: Chuẩn bị hệ thống và Khởi tạo HDFS (Bổ sung quy chuẩn "Chụp toàn bộ quy trình")**

| STT | Nội dung | Lệnh / Giao diện |
| :---: | :--- | :--- |
| 0.1 | Khởi tạo công cụ | Màn hình cài đặt thành công Java/Scala (Ví dụ: lệnh `pacman`, `apt`) |
| 0.2 | Tải và giải nén nền tảng | Kết quả dòng lệnh `wget` hoặc minh chứng đã tải về và giải nén (`tar`) thành công thư mục Hadoop |
| 0.3 | Cấu hình biến môi trường (`PATH`) | Mở tệp `~/.bashrc` hoặc mục System Variables trên Windows hiển thị trỏ đường dẫn `JAVA_HOME` và `HADOOP_HOME` |
| 0.4 | Cấu hình tham số lõi XML | Cú pháp in tệp tin (`cat`) cấu hình `core-site.xml` và `hdfs-site.xml` của Hadoop |
| 0.5 | Minh chứng IP định tuyến | Lệnh kiểm tra thẻ mạng `ip addr` hoặc `ifconfig` |
| 1 | Format NameNode | Lệnh `hdfs namenode -format` (thấy "successfully formatted") |
| 2 | Khởi động cluster | `start-dfs.sh` và `start-yarn.sh` |
| 3 | Kiểm tra tiến trình | `jps` (đủ NameNode, DataNode, ResourceManager, NodeManager) |
| 4 | WebUI | `http://localhost:9870` — tab Datanodes |
| 5 | Tạo thư mục gốc và user | `hdfs dfs -mkdir /hcmus` + `sudo adduser khtn_<StudentID>` |
| 6 | Tạo thư mục con | `hdfs dfs -mkdir /hcmus/<StudentID>` |
| 7 | Tải dữ liệu lên HDFS | `hdfs dfs -put` + `hdfs dfs -ls` để xác nhận |
| 8 | Phân quyền và chuyển owner | `hdfs dfs -chmod 744` + `hdfs dfs -chown` + `hdfs dfs -ls` |
| 9 | Chạy JAR xác thực | `java -jar hadoop-test.jar <PORT> /hcmus/<StudentID>` |

**Phần 2: WordCount bằng Scala (tối thiểu 2 ảnh)**

| STT | Nội dung | Lệnh / Giao diện |
| :---: | :--- | :--- |
| 10 | Chạy mã Scala thành công | Terminal hiển thị `spark-submit` hoặc `sbt run` không lỗi |
| 11 | Kết quả file TSV | `cat results.txt` — thấy rõ định dạng tab-separated |

**Phần 3: Xử lý sự cố (bắt buộc nếu gặp lỗi)**

| STT | Nội dung |
| :---: | :--- |
| 12 | Màn hình báo lỗi |
| 13 | Quá trình tra cứu và sửa lỗi (Google, StackOverflow, chỉnh config) |
| 14 | Kết quả sau khi sửa thành công |

---

## VI. BẢNG TIÊU CHÍ CHẤM ĐIỂM

| Hạng mục | Điểm |
| :--- | :---: |
| **Hadoop Cluster** | **4,5** |
| — Cài đặt thành công (NameNode, DataNode) | 1,0 |
| — YARN trên VM và máy vật lý | 1,5 |
| — Hoàn thành các lệnh HDFS theo hướng dẫn | 1,0 |
| — Báo cáo chi tiết (giải thích từng bước, cách sửa lỗi) | 1,0 |
| — Thiếu file xác thực Java | **−4,5** |
| **WordCount** | **5,0** |
| — Tải dữ liệu lên HDFS thành công | 0,5 |
| — Đọc dữ liệu thành công | 0,5 |
| — Tính toán MapReduce thành công | 1,0 |
| — Xuất kết quả TSV | 1,0 |
| — Định dạng và kết quả chính xác | 1,0 |
| — Báo cáo chi tiết phần WordCount | 1,0 |
| **Code Quality & Language** | **0,5** |
| — Mã nguồn bằng Scala | 0,5 |
| **Tổng cộng** | **10,0** |

> **Lưu ý nghiêm trọng:** Trùng IP giữa các thành viên = 0 điểm toàn bài. Sai cấu trúc ZIP = trừ điểm nặng hoặc 0 điểm.