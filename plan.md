# PROJECT PLAN - LAB 01: INTRODUCTION TO HADOOP ECOSYSTEM
*(Tài liệu này được thiết kế chi tiết ở mức độ đặc tả kỹ thuật, đảm bảo bất kỳ AI Agent hay thành viên nào đọc vào cũng có thể hiểu 100% bối cảnh, yêu cầu và quy trình thực thi dự án).*

---

## I. TỔNG QUAN DỰ ÁN (EXECUTIVE SUMMARY)
- **Môn học:** Introduction to Big Data.
- **Mục tiêu cốt lõi:** Làm quen với Hệ sinh thái Hadoop, biết cách tự cài đặt cụm phân tán (cluster), thao tác thành thạo với HDFS (Hadoop Distributed File System) qua dòng lệnh, và giải quyết bài toán Big Data kinh điển (WordCount) bằng ngôn ngữ Scala.
- **Quy mô nhóm:** 4 thành viên (A, B, C, D).
- **Yêu cầu cài đặt:** Cần 1 cụm Pseudo-distributed và `n-1` (tức 3) cụm Fully-distributed.
- **Yêu cầu Nộp bài (Submission):** Cực kỳ khắt khe về định dạng thư mục, yêu cầu minh chứng bằng hình ảnh (screenshots) cho MỌI thao tác, và chống gian lận (IP thiết bị phải độc lập hoàn toàn, trùng IP = 0 điểm toàn bài).

---

## II. ĐẶC TẢ YÊU CẦU CHI TIẾT THEO LAB 1 - HDFS.PDF

### 1. Phần 1: Thiết lập Hadoop Cluster & HDFS (4.5 Điểm)
Tất cả các thành viên phải tự thực hiện trên máy ảo/môi trường riêng (có IP riêng biệt) các tác vụ sau:
*   **Cài đặt:** Khởi tạo các Node (NameNode, DataNode, ResourceManager, NodeManager).
*   **Thao tác HDFS & Phân quyền:**
    *   Tạo thư mục root: `hdfs dfs -mkdir /hcmus`
    *   Tạo user Linux nội bộ: `khtn_<StudentID>`
    *   Tạo thư mục con: `hdfs dfs -mkdir /hcmus/<StudentID>`
    *   Đẩy file dữ liệu lên HDFS: `hdfs dfs -put <file_bất_kỳ> /hcmus/<StudentID>/`
    *   Cấp quyền thư mục: `hdfs dfs -chmod 744 /hcmus/<StudentID>`
    *   Chuyển quyền sở hữu: `hdfs dfs -chown khtn_<StudentID> /hcmus/<StudentID>`
*   **Chạy File Xác Thực (Bắt buộc - Thiếu sẽ bị trừ 4.5đ):**
    *   Sử dụng file `hadoop-test.jar` được cung cấp.
    *   Lệnh: `java -jar /path/to/hadoop-test.jar <YOUR_HDFS_PORT> /hcmus/<StudentID>` (Port HDFS thường là 9000).
    *   Lệnh này sẽ sinh ra file `<StudentID>_verification.txt`. Phải lấy file này nộp kèm.
    *   *Lưu ý xử lý lỗi:* Nếu gặp lỗi mạng `Exception in thread main null... Main getMac`, phải tìm cách fix, hoặc ít nhất chụp lại quá trình tra cứu Google/debug bỏ vào báo cáo.

### 2. Phần 2: Khởi động với WordCount (5.0 Điểm)
*   **Bài toán:** Viết chương trình đếm số lượng các từ bắt đầu bằng danh sách chữ cái chỉ định: **f, i, t, h, c, m, u, s**.
*   **Ràng buộc logic:**
    *   Không phân biệt hoa thường (Case-insensitive).
    *   Bao gồm cả ký tự đặc biệt nối liền từ (Special character inclusive).
*   **Ràng buộc kỹ thuật:**
    *   **Ngôn ngữ BẮT BUỘC:** **Scala** (Sử dụng Python bị 0 điểm phần ngôn ngữ).
    *   Thuật toán: MapReduce hoặc Spark RDD cơ bản.
    *   **CẤM:** Không được dùng Regular Expression (Regex) để xử lý trên toàn bộ file văn bản (vì không tuân thủ tính chất phân tán của Big Data). Nếu dùng Python (cho dù mất điểm ngôn ngữ), cũng tuyệt đối cấm dùng phương pháp Streaming.
*   **Dữ liệu đầu vào:** File `words.txt` (nằm trên HDFS).
*   **Dữ liệu đầu ra:** Xuất ra file định dạng **TSV** (Tab-Separated Values). 
    *   Ví dụ cấu trúc file xuất ra:
        ```text
        f   1
        i   3
        t   5
        ```

### 3. Phần 3: Báo cáo & Nộp bài (Minh chứng & Định dạng)
*   **Luật Chụp Ảnh (Screenshotted Rule):** MỌI lệnh gõ ở Phần 1 và MỌI lỗi gặp phải đều phải chụp lại màn hình (ưu tiên lấy được IP và thời gian hệ thống) và đưa vào file `Report.pdf` kèm vài dòng giải thích.
*   **Định dạng Nén:** Một file ZIP duy nhất tên `<RepresentativeID>.zip`.

---

## III. PHÂN CÔNG NHIỆM VỤ VÀ QUY HOẠCH HỆ THỐNG

### 1. Bảng Ánh Xạ Thành Viên
| Mã Thành Viên | Họ và Tên |
| :---: | :--- |
| **A** | Lê Xuân Trí |
| **B** | Nguyễn Hồ Anh Tuấn |
| **C** | Trần Hữu Kim Thành |
| **D** | Nguyễn Lê Trung Trực |

### 2. Vai trò và Cấu hình Cụm (Cluster Roles)
Nhóm có 4 người, phân bổ để lấy 100% điểm thưởng (Bonus):

| Thành viên | Cụm cài đặt | Cấu hình & Mục tiêu | Ghi chú IP |
| :--- | :--- | :--- | :--- |
| **A (Leader)** | **Pseudo-distributed** | Cài local. Kiểm duyệt toàn bộ Report, IP, cấu trúc file, gom file và nộp. Đồng thời **A là Dev chính viết mã nguồn Scala** chuẩn cho cả nhóm xài chung. | Phải có IP độc lập |
| **B** | **Fully-distributed** | Dựng YARN bằng VM/Docker. Cần tối thiểu **1 Master + 2 Workers** để lấy **Bonus 1**. | Phải có IP độc lập |
| **C** | **Fully-distributed** | Dựng YARN trên thiết bị vật lý. Cần tối thiểu **2 physical machines + 2 Workers** lấy **Bonus 2**. | Phải có IP độc lập |
| **D** | **Fully-distributed** | Dựng YARN bằng WSL/VM. | Phải có IP độc lập |

### 2. Cấu Trúc Thư Mục Nộp Bài Chuẩn (Standard Blueprint)
**TUYỆT ĐỐI KHÔNG ĐƯỢC SAI LỆCH cấu trúc này:**
```text
<RepresentativeID>.zip (Ví dụ: 23120001.zip)
└── <RepresentativeID>/
    ├── <StudentID_A>/
    │   ├── docs/
    │   │   ├── Report.pdf (Báo cáo cá nhân chứa đủ 13+ ảnh minh chứng)
    │   │   └── <StudentID_A>_verification.txt (Sinh ra từ file JAR)
    │   └── src/
    │       └── WordCount/
    │           ├── results.txt (File TSV đếm từ)
    │           ├── WordCount.scala (Mã nguồn)
    │           └── README.md (Hướng dẫn chạy code)
    ├── <StudentID_B>/ (Cấu trúc y hệt A)
    ├── <StudentID_C>/ (Cấu trúc y hệt A)
    └── <StudentID_D>/ (Cấu trúc y hệt A)
```
*(Ghi chú: Dù A viết code, nhưng B, C, D phải lấy code đó tự chạy trên cụm của mình để lấy file results.txt và chụp ảnh terminal lúc chạy).*

### 3. Quy Luật Phân Nhánh và Quản Lý Mã Nguồn (Branching Strategy)
Để đảm bảo không có xung đột mã nguồn (Merge Conflict) và dễ dàng quản lý bài nộp, nhóm áp dụng quy tắc quản lý nhánh (Git) như sau:
*   **Nhánh `main`:** Lưu trữ phiên bản hoàn chỉnh và thư mục chuẩn. Chỉ Leader (A) mới có quyền hợp nhất (merge) các phần nộp và tạo bản nén ZIP cuối cùng. Không ai được làm việc (commit) trực tiếp trên nhánh này.
*   **Nhánh `member/<StudentID>`:** Mỗi cá nhân tự tạo nhánh riêng từ `main` để làm việc. Ví dụ: `member/23120001`.
    *   Thành viên chỉ tải (commit/push) tài liệu, hình ảnh, file kết quả vào đúng thư mục mang MSSV của mình.
    *   Khi làm xong, mở Pull Request (PR) về nhánh `main` để Leader đánh giá (QA) và duyệt.
*   **Không gian chia sẻ mã nguồn (Leader A):** Leader A sau khi viết xong mã nguồn Scala chuẩn, sẽ đưa lên một nhánh riêng biệt, ví dụ `shared/wordcount-core`. Các thành viên B, C, D sẽ tải (pull/cherry-pick) code từ nhánh này về máy tính cá nhân để thực thi trên cụm riêng của mình.

---

## IV. WORKFLOW DÀNH CHO AI AGENT HỖ TRỢ THÀNH VIÊN
Nếu một AI Agent được giao nhiệm vụ hỗ trợ một thành viên (ví dụ A, B, C, D), Agent cần tuân thủ Workflow sau:

### Phase 1: Môi trường & Minh chứng IP
- Yêu cầu người dùng chạy lệnh `ip addr` hoặc `ifconfig`.
- **Nhắc nhở:** Báo người dùng chụp ảnh màn hình lệnh này lưu vào báo cáo (Bức ảnh số 0).

### Phase 2: Triển khai & Chụp ảnh HDFS
Agent cung cấp chính xác các lệnh shell sau và yêu cầu người dùng **CHỤP ẢNH TỪNG BƯỚC MỘT**:
1. Lệnh Format: `hdfs namenode -format`
2. Lệnh Start: `start-dfs.sh` và `start-yarn.sh`
3. Lệnh kiểm tra: `jps` (Xác nhận đủ 5 tiến trình) và check WebUI `http://localhost:9870`.
4. Các lệnh File System:
   - `hdfs dfs -mkdir /hcmus`
   - Tạo user (tùy OS, vd: `sudo adduser khtn_<StudentID>`)
   - `hdfs dfs -mkdir /hcmus/<StudentID>`
   - `hdfs dfs -put words.txt /hcmus/<StudentID>/`
   - `hdfs dfs -chmod 744 /hcmus/<StudentID>`
   - `hdfs dfs -chown khtn_<StudentID> /hcmus/<StudentID>`
5. Chạy file JAR xác thực: Hướng dẫn người dùng gõ `java -jar hadoop-test.jar 9000 /hcmus/<StudentID>` và lưu trữ file TXT sinh ra.
*Lưu ý xử lý lỗi:* Nếu có lỗi xảy ra ở bất kỳ lệnh nào, Agent phải bảo người dùng chụp màn hình lỗi lại, cung cấp solution sửa lỗi, và bảo người dùng chụp lại quá trình sửa để nhét vào Report.

### Phase 3: Triển khai Code Scala
Agent cung cấp mã nguồn Scala bám sát yêu cầu (Không regex toàn cục, Lọc theo chữ cái thường/hoa, Trả về mảng Key-Value, ReduceByKey, Lưu file format TSV). Mẫu code cơ bản:
```scala
val targetChars = Set('f', 'i', 't', 'h', 'c', 'm', 'u', 's')
val words = textFile.flatMap(line => line.split("\\s+")) 
val filteredPairs = words.filter(_.nonEmpty).map { word =>
    val firstChar = word.charAt(0).toLower
    if (targetChars.contains(firstChar)) (firstChar.toString, 1) else ("", 0) 
}.filter(_._1.nonEmpty)
val counts = filteredPairs.reduceByKey(_ + _)
// Yêu cầu cấu hình ouput theo chuẩn TSV: s"${key}\t${value}"
```
Yêu cầu người dùng chụp ảnh màn hình lúc `spark-submit` hoặc chạy IDE thành công, và chụp nội dung lệnh `cat results.txt`.

### Phase 4: Kiểm duyệt cuối (Pre-Flight Check)
Agent viết một script kiểm tra cấu trúc thư mục (vd bash script dùng lệnh `tree` và check file tồn tại) để xác nhận hệ thống thư mục của thành viên hoàn toàn khớp với định dạng mục III.2. Đảm bảo file `_verification.txt` tồn tại.

---

## V. DANH SÁCH CÁC BỨC ẢNH BẮT BUỘC PHẢI CHỤP (MANDATORY SCREENSHOT CHEAT SHEET)
*(Tất cả 4 thành viên A, B, C, D đều phải tự nộp đủ danh sách ảnh này trong Report cá nhân. Bất kỳ ảnh nào thiếu đều dẫn đến trừ điểm).*

**Quy tắc chung khi chụp:** Mở rộng toàn màn hình terminal/giao diện, PHẢI THẤY RÕ thời gian hệ thống, địa chỉ IP (nếu có thể) và kết quả/đầu ra của câu lệnh vừa gõ.

**Phần 1: Khởi tạo cụm và HDFS (10 Ảnh)**
*   **[Ảnh 0] Chống gian lận IP:** Lệnh `ip addr` hoặc `ifconfig` (Thấy rõ địa chỉ IPv4 và tên card mạng).
*   **[Ảnh 1] Format NameNode:** Lệnh `hdfs namenode -format` (Thấy dòng báo thành công "has been successfully formatted").
*   **[Ảnh 2] Start Cluster:** Lệnh `start-dfs.sh` & `start-yarn.sh` (Thấy các node đang khởi động).
*   **[Ảnh 3] Kiểm tra Tiến trình:** Lệnh `jps` (Phải thấy đủ `NameNode`, `DataNode`, `ResourceManager`, `NodeManager`).
*   **[Ảnh 4] Giao diện WebUI:** Trình duyệt vào `http://localhost:9870`, tab **Datanodes** (Thấy rõ số lượng Live Nodes - Thành viên B/C cần thấy đủ >= 2 node để minh chứng lấy Bonus).
*   **[Ảnh 5] Tạo Root & User:** Lệnh `hdfs dfs -mkdir /hcmus` và lệnh tạo user hệ điều hành (vd: `sudo adduser khtn_<StudentID>`).
*   **[Ảnh 6] Tạo Subfolder:** Lệnh `hdfs dfs -mkdir /hcmus/<StudentID>`.
*   **[Ảnh 7] Upload Dữ liệu:** Lệnh `hdfs dfs -put words.txt /hcmus/<StudentID>/` và lệnh kiểm tra `hdfs dfs -ls /hcmus/<StudentID>/` (Thấy rõ file nằm trên HDFS).
*   **[Ảnh 8] Phân Quyền & Owner:** Lệnh `hdfs dfs -chmod 744` và `hdfs dfs -chown khtn_<StudentID>`. Phải có lệnh `hdfs dfs -ls /hcmus/` để minh chứng quyền `drwxr--r--` và owner đã được đổi thành công.
*   **[Ảnh 9] Chạy Jar Xác thực:** Lệnh `java -jar hadoop-test.jar <PORT> /hcmus/<StudentID>` (Thấy thông báo thành công. Đừng quên lưu lại file `_verification.txt` được sinh ra).

**Phần 2: WordCount bằng Scala (Tối thiểu 2 Ảnh)**
*   **[Ảnh 10] Chạy mã Scala:** Giao diện Terminal/IDE lúc build và chạy mã MapReduce/Spark báo thành công, không văng lỗi (Hiển thị lệnh `spark-shell`, `spark-submit` hoặc chạy bằng sbt).
*   **[Ảnh 11] Kết quả xuất file TSV:** Lệnh in ra nội dung kết quả (ví dụ: `cat results.txt` hoặc `head results.txt`) -> Bắt buộc thấy rõ định dạng xuất ra được ngăn cách bằng khoảng trắng Tab (VD: `f    1`).

**Phần 3: Xử lý sự cố (Troubleshooting - BẮT BUỘC CHỤP NẾU GẶP LỖI)**
*   *Lưu ý từ PDF:* "Even if you encounter any error... you must provide information about that error".
*   **[Ảnh 12] Màn hình báo lỗi:** Chụp Terminal báo lỗi (Ví dụ: Connection Refused, Exception getMac, ...).
*   **[Ảnh 13] Quá trình Fix:** Giao diện tra cứu giải pháp trên Google Search, StackOverflow, hoặc ảnh bạn đang sửa file cấu hình (`core-site.xml`, `/etc/hosts`...).
*   **[Ảnh 14] Kết quả sau Fix:** Chạy lại câu lệnh bị lỗi ban đầu và chụp kết quả thành công.

---

## VI. BẢNG TIÊU CHÍ CHẤM ĐIỂM (GRADING RUBRIC)
Để đối chiếu chất lượng công việc, đây là bảng điểm gốc từ Giảng viên:
- **Hadoop Cluster (4.5 đ)**
  - Cài đặt thành công (1.0đ)
  - YARN VM/Physical (Bonus: 1.5đ)
  - Chạy đủ các lệnh ưu tiên HDFS (1.0đ)
  - Report chi tiết, có giải thích từng bước và cách sửa lỗi (1.0đ)
  - Thiếu file xác thực Java: **Trừ 4.5đ**
- **WordCount Warm Up (5.0 đ)**
  - Upload data lên HDFS thành công (0.5đ)
  - Đọc data thành công (0.5đ)
  - Tính toán MapReduce thành công (1.0đ)
  - Export ra file TSV (1.0đ)
  - File format TSV và đáp án đúng (1.0đ)
  - Báo cáo chi tiết phần này (1.0đ)
- **Code Quality & Language (0.5 đ)**
  - Nộp mã nguồn bằng Scala (0.5đ)
- **Tổng: 10.0 Điểm.** Trùng IP = 0 Điểm. Sai cấu trúc Zip = Trừ điểm nặng/0 điểm.