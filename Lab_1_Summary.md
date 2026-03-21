# Tóm tắt Đồ án: Lab 01 - Introduction to Hadoop Ecosystem

Tài liệu hướng dẫn thực hành Lab 1 môn Big Data. Mục tiêu chính là làm quen với hệ sinh thái Hadoop, bao gồm việc cài đặt cụm Hadoop và thực hiện bài toán Word Count cơ bản bằng mô hình MapReduce trên HDFS.

---

## Yêu cầu chi tiết từng phần

### Phần 1: Cài đặt Hadoop Cluster (Cụm Hadoop)

**1. Yêu cầu chung:**
- Cài đặt cụm Hadoop ở chế độ pseudo-distributed và thực hiện các lệnh HDFS cơ bản.
- Chụp màn hình tất cả các bước và trình bày chi tiết vào file **Report.pdf**. Nếu có lỗi, cần ghi lại chi tiết lỗi (kết quả tìm kiếm Google, phân tích debug) và cách xử lý.
- Chạy một file jar được cung cấp và xuất kết quả ra file `<StudentID>_verification.txt`.

**2. Hướng dẫn thực hiện chi tiết:**
- **Cài đặt Hadoop Pseudo-distributed:** Chọn 1 trong các phương pháp: Ubuntu native, Docker, Ubuntu VM (VirtualBox/VMWare), hoặc WSL.
- **Cài đặt Hadoop Fully-distributed:** Mỗi nhóm (n thành viên) phải có n-1 cài đặt fully distributed. Có thể chọn:
  - YARN trên Virtual Machines (Docker hoặc VM khác) với tối thiểu 2 workers (có điểm thưởng).
  - YARN trên các máy vật lý (physically separated machines) với tối thiểu 2 máy vật lý và 2 workers (có điểm thưởng).
- **Thao tác HDFS:**
  - Tạo thư mục `/hcmus` trên HDFS.
  - Tạo user có tên `khtn_<StudentID>`.
  - Tạo thư mục con `/hcmus/<StudentID>` và tải (upload) một file bất kỳ vào đó.
  - Phân quyền: Đổi quyền `/hcmus/<StudentID>` thành `744` (Chmod 744) và đổi chủ sở hữu (owner) của thư mục thành `khtn_<StudentID>`.
- **Chạy file xác thực (Verification):**
  - Chạy file `hadoop-test.jar` với lệnh:
    `java -jar /path/to/jar/file.jar <YOUR_HDFS_PORT> /hcmus/<StudentID>`
  - Xử lý các lỗi mạng (nếu có, ví dụ exception "getMac") và ghi lại quá trình fix lỗi vào báo cáo.
  - Lấy file `<StudentID>_verification.txt` được tạo ra trong thư mục chạy lệnh để nộp.

### Phần 2: Word Count (Khởi động)

**1. Yêu cầu chi tiết:**
- Viết chương trình đếm số lượng các từ bắt đầu bằng các chữ cái: **f, i, t, h, c, m, u, s** (không phân biệt hoa/thường, bao gồm cả ký tự đặc biệt) trong file từ điển `words.txt`.
- **Ngôn ngữ:** Bắt buộc dùng **Scala** để đạt điểm tối đa (nếu dùng Python sẽ mất điểm phần ngôn ngữ lập trình).
- Không được dùng Regular Expression (regex) trên toàn bộ file vì không chuẩn với xử lý Big Data.
- Nếu lỡ dùng Python, không được dùng phương pháp streaming.
- **Kết quả:** Xuất ra file định dạng TSV (tab-separated values) chứa từng chữ cái trên và số lượng từ tương ứng (Ví dụ: `f    1`).

### Phần 3: Hướng dẫn Nộp bài (Submission Guidelines)

- Nộp bài theo nhóm: Một đại diện (Representative) sẽ nộp 1 file nén duy nhất lên Moodle.
- **Cấu trúc thư mục nộp bài:**
  ```text
  <RepresentativeID>.zip  
  └── <RepresentativeID>/
      ├── <StudentID_1>/
      │   ├── docs/
      │   │   ├── Report.pdf
      │   │   └── <StudentID_1>_verification.txt
      │   └── src/
      │       ├── WordCount/
      │       │   ├── results.txt (file TSV kết quả WordCount)
      │       │   └── WordCount.scala (hoặc .py)
      │       └── README (tuỳ chọn - để hướng dẫn chạy code)
      ├── <StudentID_2>/
      ...
  ```
- **Lưu ý quan trọng:** Code cần được comment rõ ràng. Báo cáo, logs và ảnh chụp màn hình phải đầy đủ. Bất kỳ sự gian lận/sao chép nào sẽ dẫn đến 0 điểm cho cả nhóm.

---

## Tóm tắt Thang điểm Cơ bản (10.0đ)
- **Hadoop Cluster (4.5đ):** Cài đặt thành công (1.0đ), YARN distributed (1.5đ), Làm đúng các lệnh HDFS (1.0đ), Báo cáo chi tiết/sửa lỗi (1.0đ). Sẽ bị **trừ 4.5đ** nếu không có file verification xác thực từ file java.
- **WordCount (5.0đ):** Upload data (0.5đ), Đọc data (0.5đ), Xử lý MapReduce đúng (1.0đ), Export kết quả TSV (1.0đ), Format/kết quả đúng (1.0đ), Báo cáo chi tiết (1.0đ).
- **Ngôn ngữ (0.5đ):** Viết bằng Scala (0.5đ).
