# ĐÁNH GIÁ KẾ HOẠCH LAB 01 (PHIÊN BẢN CHẤM THẲNG NHƯ GIẢNG VIÊN)

## I. ĐÁNH GIÁ TỔNG QUAN

### Kết luận nhanh
Kế hoạch hiện tại của nhóm **rất tốt (xấp xỉ 8.5 - 9.0/10)**, thậm chí vượt mức yêu cầu cơ bản của Lab 1.

### Điểm mạnh nổi bật
- Bám sát yêu cầu đề bài và tài liệu PDF.
- Thể hiện tư duy System Design, Workflow và QA rõ ràng.
- Có nhận diện rủi ro tốt (IP, cấu trúc nộp bài, screenshot bắt buộc).
- Có tư duy DevOps qua chiến lược Git branch, kiểm duyệt và pre-check.

### Nhận xét thẳng
Đây không còn là mức kế hoạch sinh viên cơ bản, mà tiệm cận cách làm của Team Lead/PM.

---

## II. NHỮNG PHẦN LÀM RẤT TỐT (NÊN GIỮ NGUYÊN)

### 1) Mức độ bám sát đề gần như tuyệt đối
Kế hoạch đã cover đầy đủ các yêu cầu quan trọng:
- Pseudo-distributed + (n-1) fully-distributed cluster.
- Các lệnh HDFS bắt buộc.
- Chạy file verification JAR.
- WordCount bằng Scala.
- Xuất kết quả TSV.
- Quy tắc screenshot minh chứng.
- Cấu trúc thư mục nộp bài.

**Kết luận:** Không thiếu requirement trọng yếu.

### 2) Phân công vai trò hợp lý
- A làm leader + dev chính: hợp lý.
- B/C/D triển khai các loại cụm khác nhau: tối ưu khả năng lấy bonus.
- Mỗi người tự chạy code trên cụm riêng: đúng tinh thần chống gian lận.

**Điểm cộng lớn:** A viết code chuẩn, nhưng B/C/D tự chạy và tự sinh kết quả của mình là đúng hướng chấm.

### 3) Workflow hỗ trợ bằng AI Agent rất mạnh
- Chuẩn hóa quy trình thao tác.
- Giảm lỗi cho thành viên yếu kỹ thuật.
- Có thể tái sử dụng và mở rộng.

**Đánh giá:** Đây là lợi thế cạnh tranh rõ rệt so với nhiều nhóm khác.

### 4) Checklist screenshot rất thực dụng
- Chuyển yêu cầu mơ hồ thành checklist có thứ tự.
- Mapping mỗi bước với minh chứng tương ứng.

**Ý nghĩa:** Giảm tối đa nguy cơ mất điểm do thiếu bằng chứng.

### 5) Chiến lược Git branch tốt hơn mức cần thiết
- Có `main`, `member/<StudentID>`, `shared/wordcount-core`.
- Có luồng PR và QA trước khi hợp nhất.

**Nhận xét:** Overkill theo hướng tích cực.

---

## III. VẤN ĐỀ VÀ RỦI RO CẦN SỬA

### 1) Rủi ro lớn: A viết code cho cả nhóm
**Vấn đề:**
PDF nhấn mạnh yếu tố “by yourself”. Giảng viên có thể hỏi random từng thành viên về logic xử lý.

**Nguy cơ:**
B/C/D không hiểu sâu code thì dễ mất điểm ở phần giải thích.

**Khuyến nghị bổ sung vào plan:**
- Mỗi thành viên phải tự giải thích được:
  - `map`
  - `reduceByKey`
  - logic `filter`
- Mỗi người tự viết `README.md` theo lời văn cá nhân.
- Có mini-oral rehearsal nội bộ trước khi nộp.

### 2) Thiếu cơ chế phân hóa báo cáo cá nhân
**Vấn đề:**
Nếu report quá giống nhau dễ bị nghi copy.

**Khuyến nghị bổ sung:**
- Mỗi người phải có ảnh IP riêng rõ ràng.
- Mô tả lỗi và cách xử lý bằng lời văn riêng.
- Bổ sung ghi chú cá nhân (môi trường, khó khăn, quyết định kỹ thuật).

### 3) Thiếu timeline cụ thể
**Vấn đề:**
Kế hoạch chi tiết nhưng chưa có mốc thời gian cứng.

**Khuyến nghị timeline mẫu:**
- Day 1: Setup môi trường.
- Day 2: HDFS + chụp ảnh bắt buộc.
- Day 3: Chạy WordCount + xuất TSV.
- Day 4: Hoàn thiện report cá nhân.
- Day 5: Leader QA, merge và đóng gói ZIP.

### 4) Rủi ro trùng IP chưa có quy trình kiểm soát
**Vấn đề:**
Có nêu yêu cầu IP khác nhau nhưng chưa có bước kiểm chứng tập trung.

**Khuyến nghị bổ sung:**
- Leader thu screenshot `ip addr`/`ifconfig` của tất cả thành viên ngay từ đầu.
- Lập bảng đối chiếu IP private/public theo từng người.
- Nếu trùng, đổi mạng ngay (4G/hotspot/Wi-Fi khác).

### 5) Điểm nhạy cảm ở WordCount logic (regex)
**Vấn đề:**
Mẫu `split("\\s+")` dùng regex, có thể bị bắt bẻ tùy giảng viên.

**Khuyến nghị an toàn hơn:**
- Dùng tách đơn giản theo khoảng trắng (`split(" ")`) kết hợp lọc token rỗng.
- Hoặc tự viết parser đơn giản không phụ thuộc regex toàn cục.

### 6) Thiếu bước validate kết quả WordCount
**Vấn đề:**
Hiện mới dừng ở việc chạy ra kết quả, chưa có kiểm định đúng/sai.

**Khuyến nghị bổ sung:**
- Tạo file test nhỏ có đáp án tính tay.
- Chạy chương trình và so sánh đầu ra TSV với expected output.

### 7) Thiếu phương án dự phòng khi cluster lỗi
**Vấn đề:**
Fully-distributed dễ lỗi do mạng, hostname, SSH, config mismatch.

**Khuyến nghị bổ sung Plan B:**
- Ưu tiên sửa lỗi theo checklist chuẩn.
- Nếu vẫn fail, chuyển qua phương án Docker hoặc VM backup.
- Ghi lại toàn bộ troubleshooting để vẫn giữ điểm report.

---

## IV. GỢI Ý NÂNG CẤP ĐỂ TIỆM CẬN 10/10

### 1) Thêm sơ đồ kiến trúc
- Vẽ HDFS architecture (NameNode, DataNode, block flow).
- Vẽ YARN execution flow (ResourceManager, NodeManager, job execution).

**Lợi ích:** Tăng độ chuyên nghiệp và tạo thiện cảm khi chấm.

### 2) Thêm mục “Lessons Learned” trong report
- Thành viên học được gì từ việc dựng cluster.
- Khó khăn lớn nhất và cách vượt qua.
- Bài học về phân tán, fault tolerance, permission model.

### 3) Nâng cấp phần debugging notes
- Phân loại lỗi theo nhóm:
  - Network/Hostname/SSH
  - Permission/HDFS owner
  - Configuration (`core-site.xml`, `hdfs-site.xml`, `yarn-site.xml`)
- Mỗi lỗi có: triệu chứng -> nguyên nhân -> cách fix -> kết quả sau fix.

---

## V. BẢNG ĐÁNH GIÁ TÓM TẮT

| Tiêu chí | Đánh giá |
| --- | --- |
| Độ đầy đủ yêu cầu | 5/5 |
| Tổ chức và quản trị | 5/5 |
| Tính thực tế triển khai | 4/5 |
| Kiểm soát rủi ro | 3/5 |

**Overall đề xuất: 8.8/10**

---

## VI. 3 VIỆC ƯU TIÊN PHẢI SỬA NGAY

1. Thêm timeline thực thi cụ thể theo ngày và người chịu trách nhiệm.
2. Bắt buộc mỗi thành viên hiểu và giải thích được code, không chỉ chạy theo mẫu.
3. Tránh vùng xám regex và thêm bước validate đầu ra WordCount bằng test nhỏ.

---

## VII. KẾT LUẬN CUỐI

Kế hoạch hiện tại đã rất mạnh và có nền tảng tổ chức tốt. Nếu bổ sung đúng các điểm rủi ro nêu trên, đặc biệt là timeline + cá nhân hóa report + kiểm định kết quả code, nhóm có khả năng tiến gần mức điểm tối đa và giảm đáng kể nguy cơ mất điểm do lỗi quy trình.