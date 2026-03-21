# Kế Hoạch Thực Hiện Dự Án - Lab 1: Hệ Sinh Thái Hadoop

Tài liệu này là bản kế hoạch thực thi chi tiết dành cho nhóm 4 thành viên (Thành viên A, B, C và D) nhằm hoàn thành đồ án **Introduction to Hadoop Ecosystem**. Kế hoạch được thiết kế để phân chia công việc hợp lý, tối ưu hóa điểm thưởng (bonus points), tránh xung đột mã nguồn và tuân thủ tuyệt đối quy trình về định dạng nộp bài (submission format).

---

## I. HIỂU RÕ YÊU CẦU ĐỒ ÁN

**Mục Tiêu Chính:**
1. **Cài đặt Hadoop & HDFS**: Thiết lập 1 cụm pseudo-distributed và 3 cụm fully-distributed. Thực hiện các thao tác HDFS cơ bản, quản lý phân quyền và chạy file `.jar` để xác thực hệ thống.
2. **WordCount bằng Scala**: Viết chương trình MapReduce bằng ngôn ngữ Scala để đếm số lượng các từ bắt đầu bằng các chữ cái `f, i, t, h, c, m, u, s` (không phân biệt chữ hoa/thường) trong file `words.txt`. Xuất kết quả ra định dạng chuẩn TSV (Tab-Separated Values).
3. **Strict Submission Rules**: Tuân thủ chính xác cấu trúc thư mục yêu cầu, nén thành file zip mang tên `<RepresentativeID>`, và tổng hợp tất cả tài liệu minh chứng (logs, ảnh chụp lỗi) vào file `Report.pdf`.

**Các Ràng Buộc Quan Trọng & Cập Nhật Từ Giảng Viên:**
- Bắt buộc sử dụng ngôn ngữ **Scala** cho bài WordCount (Sử dụng Python sẽ không được tính điểm phần ngôn ngữ lập trình).
- Phải nộp kèm file `<StudentID>_verification.txt`. Thiếu file này sẽ bị trừ **-4.5 điểm**.
- **CỰC KỲ QUAN TRỌNG:** Mỗi máy tính/cụm của sinh viên phải có địa chỉ IP độc lập. Giảng viên đã răn đe: **Nếu phát hiện các địa chỉ IP trùng lặp giữa các bài nộp, hình phạt sẽ là 0 ĐIỂM cho toàn bộ bài tập.**
- Báo cáo `Report.pdf` nếu nộp chung 1 bản duy nhất thì bắt buộc phải được chia nội dung RÕ RÀNG theo từng phần của mỗi `<StudentID>`.

---

## II. PHÂN CÔNG NHIỆM VỤ THÀNH VIÊN

**Bảng Ánh Xạ Thành Viên:**

| Mã Thành Viên | Họ và Tên |
| :---: | :--- |
| **A** | Lê Xuân Trí |
| **B** | Nguyễn Hồ Anh Tuấn |
| **C** | Trần Hữu Kim Thành |
| **D** | Trần Lê Trung Trực |

Nhằm đáp ứng yêu cầu $n-1$ cụm fully-distributed và hoàn thành tối đa điểm thưởng, công việc được phân bổ như sau:

### 1. Phân Công Giai Đoạn Cài Đặt (Installation Phase)

| Thành Viên | Môi Trường Mạng & Vai Trò | Cụm Hadoop Yêu Cầu | Ghi Chú & Mục Tiêu Điểm Thưởng |
| :--- | :--- | :--- | :--- |
| **A** (Nhóm trưởng) | **Pseudo-distributed** | 1 Pseudo-distributed cluster | Thiết lập cá nhân. Chịu trách nhiệm tổng hợp, kiểm tra quy chuẩn thư mục và đóng gói file ZIP cuối cùng trước khi nộp. |
| **B** | **Fully-distributed (VMs/Docker)** | 1 Fully-distributed cluster | Thiết lập YARN. **Cấu hình tối thiểu 1 Master và 2 workers (NodeManagers) để nhận Bonus #1.** (Ghi chú: Giảng viên có đề cập setup hướng tới 4 slaves, nhưng tối thiểu theo chuẩn file PDF để lấy điểm là 2 workers). |
| **C** | **Fully-distributed (Physical)** | 1 Fully-distributed cluster | Thiết lập trên thiết bị vật lý phân biệt hoặc 2 máy chủ Cloud instances. **Thiết lập ít nhất 2 physical machines và tối thiểu 2 workers để nhận Bonus #2.** |
| **D** | **Fully-distributed (WSL/VMs)** | 1 Fully-distributed cluster | Thiết lập YARN bằng WSL/VM. Chịu trách nhiệm **xây dựng mã nguồn Scala chuẩn** để nhóm tham chiếu. |

### 2. Danh Sách Nghiệp Vụ Bắt Buộc (Mandatory Shared Checklist)

Mỗi cá nhân phải chủ động thực thi các lệnh sau trên hệ thống cá nhân (Lưu ý: Phải chụp lại minh chứng cho mỗi bước theo Phần IV):
- [ ] **Lấy định danh IP**: Chạy lệnh `ip addr` hoặc `ifconfig` để chứng minh IP máy hiện tại là DUY NHẤT.
- [ ] Thiết lập thư mục root HDFS: `hdfs dfs -mkdir /hcmus`
- [ ] Khởi tạo user HDFS: (Lệnh ví dụ trên Linux: `sudo adduser khtn_<StudentID>`)
- [ ] Thiết lập phân khu cá nhân: `hdfs dfs -mkdir /hcmus/<StudentID>`
- [ ] Cập nhật (Upload) file lên HDFS: `hdfs dfs -put <local_filepath> /hcmus/<StudentID>/`
- [ ] Quyền hạn (chmod): `hdfs dfs -chmod 744 /hcmus/<StudentID>`
- [ ] Chủ sở hữu (chown): `hdfs dfs -chown khtn_<StudentID> /hcmus/<StudentID>`
- [ ] **Thực hiện Java verification**: `java -jar hadoop-test.jar <CỔNG_HDFS_CỦA_BẠN> /hcmus/<StudentID>`
- [ ] Bảo lưu file `<StudentID>_verification.txt`.

---

## III. QUY CÁCH QUẢN LÝ VERSION & DIROCTORY STRUCTURE CỦA DỰ ÁN

### 1. Sơ Đồ Định Dạng Cấu Trúc (Structural Blueprint)
```text
Root/
├── <RepresentativeID>/ (Ví dụ: 23120001)
│   ├── <StudentID_A>/
│   │   ├── docs/
│   │   │   ├── Report.pdf
│   │   │   └── <StudentID_A>_verification.txt
│   │   └── src/
│   │       └── WordCount/
│   │           ├── results.txt
│   │           ├── WordCount.scala
│   │           └── README.md
│   ├── <StudentID_B>/
│   ├── <StudentID_C>/
│   └── <StudentID_D>/
└── .gitignore
```
*Lưu ý từ giảng viên:* Dù code được share từ Member D, **MỖI SINH VIÊN** vẫn bắt buộc phải sở hữu và nộp file `WordCount.scala` của riêng mình vào trong thư mục `src/WordCount/` mang `<StudentID>` của bản thân.

### 2. Chiến Lược Hợp Nhất Các Branch
- Nhánh **`main`**: Archive Branch lưu phiên bản nén để nộp.
- Nhánh **`member/<StudentID>`**: Không gian làm việc cá nhân của mỗi thành viên, loại bỏ hoàn toàn rủi ro Merge Conflict.

---

## IV. PHẦN 1 – QUY TRÌNH CÀI ĐẶT VÀ CHỤP ẢNH MINH CHỨNG (INSTALLATION & SCREENSHOT WORKFLOW)

**Quy tắc tối thượng:** Thực thi lệnh ➔ Chụp toàn bộ góc rộng màn hình (ưu tiên thấy thời gian & IP) ➔ Dán vào file Báo cáo ➔ Làm tiếp.

### Quy Trình Thao Tác Chụp Ảnh Chuẩn Xác

**Bước 0: Xác thực Chỉ Số IP (Bắt Buộc Bổ Sung Mới Khẩn Cấp)**
- **Thao tác:** Mở Terminal nội bộ, gõ `ip addr` hoặc `ifconfig`.
- **Hành động minh chứng (Screenshot 0):** Chụp rõ địa chỉ IP IPv4 của thiết bị ảo/vật lý hiện hành. Địa chỉ này phải lưu vào tài liệu để giảng viên đối chiếu tính nguyên bản, tránh hình phạt 0 điểm.

**Bước 1: Khởi tạo và Format HDFS**
- **Thao tác:** Khai báo XML xong, chạy lệnh `hdfs namenode -format`.
- **Hành động minh chứng (Screenshot 1):** Chụp Terminal hiển thị rõ báo cáo `successfully formatted`.

**Bước 2: Khởi chạy cụm Hadoop (Startup Cluster)**
- **Thao tác:** Chạy `start-dfs.sh` và `start-yarn.sh`. 
- **Hành động minh chứng (Screenshot 2):** Chụp màn hình Terminal sau khi cả hai dịch vụ nạp xong.

**Bước 3: Thẩm vấn các Processes (Lệnh jps)**
- **Thao tác:** Khởi phát `jps` trên tất cả các Worker/Master.
- **Hành động minh chứng (Screenshot 3):** Chụp Console trả output (phải có đủ DataNode/NodeManager/NameNode/ResourceManager).

**Bước 4: Kiểm chứng qua Web UI**
- **Thao tác:** Truy cập `http://localhost:9870`, di chuyển sang Tab **Datanodes**.
- **Hành động minh chứng (Screenshot 4):** Chụp toàn bộ giao diện Browser hiển thị rõ số lượng Live Nodes trùng khớp.

**Bước 5: Thẩm định HDFS (Khởi tạo thư mục & Upload)**
- **Thao tác:** Chạy tuần tự `mkdir`, `put`, và chốt bằng lệnh `hdfs dfs -ls /hcmus/<StudentID>/`.
- **Hành động minh chứng (Screenshot 5):** Chụp Terminal biểu hiện nội dung file vừa upload hiển thị trong danh sách list.

**Bước 6: Khai Thác Phân Phối Quyền HDFS (Chmod & Chown)**
- **Thao tác:** Setup `chmod 744`, `chown`, và check lại bằng `hdfs dfs -ls /hcmus/`.
- **Hành động minh chứng (Screenshot 6):** Chụp minh chứng output `ls` hiển thị quyền chủ quản đích danh thuộc về `khtn_<StudentID>`.

**Bước 7: Xuất lệnh Verification Executable File (jar)**
- **Thao tác:** Chạy `java -jar hadoop-test.jar <CỔNG> /hcmus/<StudentID>`.
- **Hành động minh chứng (Screenshot 7):** Chụp màn hình Terminal báo Success. Gõ thêm `ls` tại local để xác nhận file `.txt` sinh ra thành công rồi chụp lại.

**Bước 8 (NẾU CÓ LỖI): Luồng Xử Lý Sự Cố (Troubleshooting Workflow)**
- Chụp khẩn toàn màn hình báo lỗi (vd: `Connection Refused`).
- Khi sử dụng Google, chụp cả giao diện Browser mang giải pháp.
- Áp dụng phương án và chụp ảnh lúc cấu hình (sửa `/etc/hosts`...).
- Test lại lệnh lỗi ban đầu, chụp xác nhận thành công. Tổ hợp 4 ảnh này là kho điểm cho `Report.pdf`.

---

## V. PHẦN 2 – PHÁT TRIỂN CHƯƠNG TRÌNH (WORDCOUNT)

### 1. Ràng Buộc Triển Khai
- **Ngôn ngữ**: Scala.
- **Target Characters**: `f, i, t, h, c, m, u, s` (Case-insensitive).
- **Tuyệt đối không sử dụng Regex** cho thuật toán xử lý dữ liệu lớn.

### 2. Triển Khai Xử Lý Chuỗi (Implementation Guide)
```scala
val targetChars = Set('f', 'i', 't', 'h', 'c', 'm', 'u', 's')

val words = textFile.flatMap(line => line.split("\\s+")) 
val filteredPairs = words.filter(_.nonEmpty).map { word =>
    val firstChar = word.charAt(0).toLower
    if (targetChars.contains(firstChar)) {
        (firstChar.toString, 1) 
    } else {
        ("", 0) 
    }
}.filter(_._1.nonEmpty)

val counts = filteredPairs.reduceByKey(_ + _)
```

### 3. Quy Ước Đầu Ra (Output Format TSV)
Quy chuẩn Export data yêu cầu định dạng **TSV**. Các thông số được cách ly bởi phím `\t`. (Ghi chú: Text định dạng `.txt` nhưng ruột phải là tab-separated).

---

## VI. PHẦN 3 – THỂ THỨC NỘP BÀI (SUBMISSION GUIDELINE)

### Kiểm Kê Quy Tắc Giai Đoạn Đóng Gói (Pre-Flight Submission Checks)
Hoàn thiện các tiêu chí sau nhầm tránh mất trắng điểm số:
- [ ] **Kiểm tra IP Lần Cuối**: Các thành viên trong nhóm đã đối chiếu IP để đảm bảo 100% không bị trùng IP chưa? (Trùng = 0 điểm môn học).
- [ ] Root Directory của file nén bằng đúng định mức `<RepresentativeID>` chưa?
- [ ] Mỗi khối hệ thống `docs/` đều nộp thành công file `<StudentID>_verification.txt` chưa?
- [ ] Code Scala WordCount đã được clone rải đều vào TẤT CẢ các thư mục `src/WordCount` của TỪNG sinh viên chưa? (Bắt buộc mỗi người nộp 1 bản cá nhân).
- [ ] Báo cáo `Report.pdf` chung đã có phần Heading chia nhỏ minh bạch theo mỗi `<StudentID>` chưa?
- [ ] Trưởng nhóm đã chạy `tree <RepresentativeID>` để xác nhận hệ phân vùng 100% không bị dư hay thiếu cấu trúc nào chưa?
