# KẾ HOẠCH THỰC THI CHI TIẾT DÀNH CHO TRƯỞNG NHÓM (THÀNH VIÊN A)

> Tài liệu đặc tả nhiệm vụ và hướng dẫn cài đặt chuyên sâu dành riêng cho Thành viên A (Trưởng nhóm).
> Thiết kế tuân thủ nghiêm ngặt mọi quy tắc từ tài liệu kế hoạch chung `plan.md`.

---

## I. THÔNG TIN CÁ NHÂN VÀ VAI TRÒ HỆ THỐNG

- **Mã định danh:** Thành viên A
- **Họ và Tên:** Lê Xuân Trí
- **Hệ điều hành:** Arch Linux (Native)
- **Vai trò chuyên môn:** Trưởng nhóm (Leader), Quản trị viên hệ thống mạng (Master Node) cho Cụm 1.
- **Trách nhiệm bao quát:** Quản trị chất lượng mã nguồn toàn nhóm, kiểm duyệt tập tin nén cuối cùng, dẫn dắt quá trình triển khai máy vật lý.

---

## II. GIAI ĐOẠN 1: THIẾT LẬP CỤM PSEUDO-DISTRIBUTED CÁ NHÂN
> **Hạn chót yêu cầu:** Ngày 24/03/2026.
> **Lưu ý quan trọng:** Toàn bộ ảnh chụp màn hình minh chứng bắt buộc phải ở chế độ toàn màn hình, hiển thị rõ ràng đồng hồ thời gian hệ thống tại thời điểm tác nghiệp và kết quả trực tiếp của dòng lệnh.

### Giai đoạn Tiền trạm: Cài đặt lõi thông qua Arch Linux (Yêu cầu lưu ảnh minh chứng)

Dựa trên quy định khắt khe của môn học ("All steps instructed... must be screenshotted"), bên cạnh các ảnh trong bảng quy chuẩn, bạn phải chụp màn hình toàn bộ các thao tác nền tảng thủ công sau đây trên Arch Linux:

1. **Cài đặt nền tảng Java, Scala và tiện ích mạng:** 
   - **Lệnh thực thi:** `sudo pacman -S jre11-openjdk jdk11-openjdk iproute2 net-tools` và `yay -S scala` (Scala hiện nằm ở kho lưu trữ AUR của Arch Linux).
   - **Thao tác minh chứng:** [Ảnh Tiền trạm 1] Chụp màn hình Terminal khi quá trình cài đặt Pacman và Yay đạt tiến trình 100% hoàn thành.

2. **Thu thập và giải nén phân phối Hadoop:**
   - **Lệnh thực thi:** `wget https://downloads.apache.org/hadoop/common/hadoop-3.4.0/hadoop-3.4.0.tar.gz` (hoặc phiên bản tương đương theo quy định), sau đó giải nén qua chỉ thị `sudo tar -xzvf hadoop-3.4.0.tar.gz -C /opt/` và thiết lập tên thư mục thành `/opt/hadoop`. Cấp quyền sở hữu thư mục cho người dùng hiện tại bằng lệnh `sudo chown -R $USER:$USER /opt/hadoop`.
   - **Thao tác minh chứng:** [Ảnh Tiền trạm 2] Chụp màn hình tiến trình tải `wget` và kết quả lệnh `tar` thành công.

3. **Khai báo biến môi trường hệ thống:**
   - **Thao tác:** Chạy các lệnh `echo` sau đây để ghi trực tiếp cấu hình vào cuối tệp `~/.bashrc`:
     ```bash
     echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk' >> ~/.bashrc
     echo 'export HADOOP_HOME=/opt/hadoop' >> ~/.bashrc
     echo 'export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin' >> ~/.bashrc
     ```
   - **Lệnh thực thi:** `source ~/.bashrc`
   - **Thao tác minh chứng:** [Ảnh Tiền trạm 3] Chụp màn hình lệnh `cat ~/.bashrc` hiển thị rõ các biến đã cấu trúc ở dòng cuối cùng. Thiết lập thêm tĩnh bằng cách gõ `echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk" | sudo tee -a $HADOOP_HOME/etc/hadoop/hadoop-env.sh`.

4. **Cấu hình định tuyến cụm Pseudo-distributed:**
   - **Thao tác:** Chỉnh sửa tệp `$HADOOP_HOME/etc/hadoop/core-site.xml`: bổ sung thuộc tính `fs.defaultFS` trỏ về `hdfs://localhost:9000`. 
   - **Thao tác:** Chỉnh sửa tệp `$HADOOP_HOME/etc/hadoop/hdfs-site.xml`: bổ sung thuộc tính `dfs.replication` bằng `1`.
   - **Thao tác minh chứng:** [Ảnh Tiền trạm 4] Chụp màn hình thao tác in (`cat`) hai tập tin XML kể trên để chứng minh hệ thống đã cấu hình.

### Các bước thực thi chính thức và danh mục cấu trúc hình ảnh (Chuẩn plan.md):

1. **Khảo sát mạng và thu thập IP gốc:**
   - **Lệnh thực thi:** `ip addr`
   - **Thao tác minh chứng:** [Ảnh 0] Chụp màn hình hiển thị toàn bộ khối giao tiếp mạng, làm nổi bật địa chỉ IP Private. Báo cáo hình ảnh này về trung tâm điều phối của nhóm ngay lập tức để rà soát xung đột mạng.

2. **Khởi tạo dữ liệu NameNode:**
   - **Lệnh thực thi:** `hdfs namenode -format`
   - **Thao tác minh chứng:** [Ảnh 1] Chụp màn hình phần thông điệp xuất ra có chứa khối văn bản thông báo dập khuôn "successfully formatted".

3. **Khởi động các dịch vụ Hadoop:**
   - **Lệnh thực thi:** `start-dfs.sh` tiếp nối bởi `start-yarn.sh`
   - **Thao tác minh chứng:** [Ảnh 2] Chụp màn hình quá trình khởi chạy dịch vụ theo thứ tự mà không hiển thị lỗi kết nối (Connection Refused).

4. **Kiểm định tiến trình Java nền:**
   - **Lệnh thực thi:** `jps`
   - **Thao tác minh chứng:** [Ảnh 3] Chụp màn hình liệt kê tối thiểu đầy đủ các tiến trình cơ bản: NameNode, DataNode, ResourceManager, NodeManager. 

5. **Xác nhận qua giao diện giao tiếp người dùng (WebUI):**
   - **Lệnh thực thi:** Mở trình duyệt và truy cập liên kết `http://localhost:9870`, di chuyển sang thẻ "Datanodes".
   - **Thao tác minh chứng:** [Ảnh 4] Chụp màn hình tài liệu HTML ban hành từ Hadoop cho thấy có ít nhất 1 DataNode trong trạng thái Live.

6. **Khải báo không gian lưu trữ và người dùng máy chủ:**
   - **Lệnh thực thi:** Thực thi lệnh phân tán `hdfs dfs -mkdir /hcmus`. Đăng ký tài khoản hệ thống qua `sudo useradd -m khtn_<StudentID_A>` (Lưu ý: Nền tảng Arch Linux sử dụng `useradd` và tham số `-m` để cấp phát thư mục gốc, thay vì lệnh `adduser` trên Ubuntu).
   - **Thao tác minh chứng:** [Ảnh 5] Chụp màn hình xác nhận trình tự các lệnh diễn ra suôn sẻ.

7. **Khai báo không gian lưu trữ cá nhân:**
   - **Lệnh thực thi:** `hdfs dfs -mkdir /hcmus/<StudentID_A>`
   - **Thao tác minh chứng:** [Ảnh 6] Chụp màn hình thao tác lệnh và thời gian hoàn thành.

8. **Đẩy tập tin dữ liệu thử nghiệm:**
   - **Lệnh thực thi:** Đưa dữ liệu lên hệ thống thông qua `hdfs dfs -put words.txt /hcmus/<StudentID_A>/` và thẩm định lại qua `hdfs dfs -ls /hcmus/<StudentID_A>/`.
   - **Thao tác minh chứng:** [Ảnh 7] Chụp màn hình lệnh danh sách (`ls`) liệt kê chi tiết dung lượng và sự hiện diện của tệp `words.txt`.

9. **Ủy quyền và phân phối đặc quyền:**
   - **Lệnh thực thi:** Nâng cấp quyền truy cập dạng octal `hdfs dfs -chmod 744 /hcmus/<StudentID_A>`. Bàn giao thẩm quyền cá nhân `hdfs dfs -chown khtn_<StudentID_A> /hcmus/<StudentID_A>`. Tiến hành kiểm tra xác thức `hdfs dfs -ls /hcmus`.
   - **Thao tác minh chứng:** [Ảnh 8] Chụp màn hình danh sách `/hcmus` xác nhận thông tin chủ sở hữu (`khtn_<StudentID_A>`) và danh sách quyền tương ứng (rwxr--r--).

10. **Thực thi quy trình kiểm định sinh viên dự án:**
    - **Lệnh thực thi:** `java -jar hadoop-test.jar <PORT> /hcmus/<StudentID_A>` (xác định cấu hình `<PORT>` tương thích với `core-site.xml`, thông thường là `9000`).
    - **Thao tác minh chứng:** [Ảnh 9] Chụp màn hình thời điểm chạy Java và danh sách liệt kê tập tin nội bộ bằng lệnh `ls -la`, thể hiện tập tin kết quả `<StudentID_A>_verification.txt` đã được hệ thống kết xuất.

11. **Giám sát sự cố lưu động (Chỉ áp dụng khi khởi sinh hỏng hóc):**
    - Nếu có sự cố bất kỳ (ví dụ: `NullPointerException... Main getMac`):
    - [Ảnh 12]: Chụp màn hình Stack Trace báo lỗi nguyên vẹn.
    - [Ảnh 13]: Ghi nhận tiến trình tìm kiếm tài liệu học thuật / tra cứu công cụ hoặc các sửa đổi mã cấu hình nội bộ.
    - [Ảnh 14]: Chụp màn hình trình trạng tái kích hoạt hệ thống thành công.

---

## III. GIAI ĐOẠN 2: THIẾT LẬP CỤM FULLY-DISTRIBUTED TRÊN MÁY VẬT LÝ (CỤM 1)
> **Lịch làm việc nhóm:** Ngày 25/03/2026.
> **Vai trò:** Trung tâm điều hành (Master Node - NameNode + ResourceManager).

### Chuẩn bị thiết bị và ứng dụng lõi (Thực hiện trước thời điểm gặp):
- Xác nhận dịch vụ Secure Shell Protocol (SSH) đã được vận hành hoàn chỉnh trên Arch Linux: `sudo pacman -S openssh` và khởi lệnh dịch vụ `sudo systemctl enable --now sshd`.
- Điều tiết cấu hình Tường lửa (Firewall) đảm bảo các cổng phân tán (`9870`, `9000`, `8088`) không bị bế tắc do nguyên lý an ninh tự động định hình từ chối gói tin.

### Hướng dẫn lắp ráp kiến trúc ngay tại buổi làm việc:
1. **Liên kết cấu trúc tô-pô mạng:**
   - Chỉ định một Modem định tuyến không dây cho tập thể sử dụng (Router Quán Cà phê / Mobile Hotspot do các thành viên cung cấp).
   - Truy vấn IP vật lý của máy Arch Linux: `ip addr`.
   - Chuyển giao địa chỉ IP vừa nhận đến toàn bộ Worker Nodes (Các thành viên giả lập trên nền WSL/VM Windows bằng chế độ Bridged Networking).
   - Vận hành lệnh rà soát mạng đối xứng (`ping <IP_Worker>`) với từng cá thể Worker để bảo đảm gói dữ liệu có thể hội tụ.

2. **Cập nhật danh bạ máy chủ nội bộ:**
   - Can thiệp và chỉnh sửa tệp quản lý IP gốc trên Arch Linux: `sudo nano /etc/hosts`.
   - Cập nhật bản đồ thực của mạng:
     ```text
     <IP_Master_A> master
     <IP_Worker_B> worker1
     <IP_Worker_C> worker2
     ```
   - Hỗ trợ và giám sát trực tiếp đồng sự D tiến hành thủ tục ánh xạ địa chỉ này cho từng môi trường Worker Nodes.

3. **Thiết lập đặc quyền uỷ nhiệm phi mật mã thuật (Passwordless SSH):**
   - Biến đổi cặp từ khóa bảo mật RSA trên môi trường Master: `ssh-keygen -t rsa -P ""`.
   - Bơm khóa công khai vào danh sách thẩm quyền của Worker: `ssh-copy-id username@worker1` và `ssh-copy-id username@worker2`.
   - Kiểm định lệnh truy cập vòng: `ssh worker1` cần truy cập thẳng mà không yêu cầu khóa chặn bí mật.

4. **Biến đổi đặc tả tệp cấu hình (XML Configuration):**
   - Khai báo lại đặc tính máy chủ của tệp `core-site.xml` trên mọi thiết bị, định tuyến đích phân giải HDFS về thiết bị Master (Ví dụ: `hdfs://master:9000`). Tránh mắc lỗi giữ nguyên `localhost`.
   - Nạp chuỗi phân hệ Worker vào tập tin `workers` (hoặc `slaves` đối với bản Hadoop phân kỳ cũ hơn) thuộc máy Master thiết kế ban đầu:
     ```text
     worker1
     worker2
     ```

5. **Khởi động mạng lưới tính toán phân tán:**
   - Việc định dạng lại NameNode (`hdfs namenode -format`) cần thiết phải tuân theo sự đồng nhất thư mục `datadir` nhằm loại thải mâu thuẫn NamespaceID.
   - Thao tác độc quyền bởi thiết bị Master, khơi mào kích hoạt diện rộng: `start-dfs.sh` và `start-yarn.sh`. 
   - Điều động báo cáo tiến trình của Cụm: Master khởi hiện NameNode và ResourceManager, yêu cầu mỗi Worker rà quét đảm bảo sự hiện hữu của cấu trúc DataNode và NodeManager.
   - Trực tiếp đánh giá giao diện `http://<IP_Master_A>:9870`, di chuyển tab Datanodes. Chụp hình cảnh thu hoạch khi Live Nodes phải đạt giá trị tối thiểu là 2 máy trở lên. Đây là minh chứng cốt lõi của bài toán cài đặt Vật lý thực thể thành công.

---

## IV. GIAI ĐOẠN 3: LẬP TRÌNH BÀI TOÁN SCALA WORDCOUNT
> **Hạn chót yêu cầu:** Ngày 29/03/2026.

1. **Giai đoạn kiến thiết thuật toán:**
   - Mã nguồn `WordCount.scala` phải do Leader A tự định hướng các quy luật, tuân lệnh nghiêm ngặt bộ tiêu chí chung: Không có biểu thức chính quy (Regex). Lợi dụng logic lọc (`filter`), đối chiếu tiền tố dạng chuỗi kí tự đặc biệt nhỏ và loại trừ sự phân cách dạng chữ cái (Chuyển tiếp thành các chữ thường `lowercase` để so sánh logic prefix: f, i, t, h, c, m, u, s).

2. **Chạy đối chứng trên HDFS:**
   - Dùng lệnh giao tiếp Spark / Scalac độc lập để khởi chạy tệp tin chương trình qua biến cấu trúc tập dữ liệu thu nhận và tải kết quả đầu ra.
   - **Thao tác minh chứng:** [Ảnh 10] Chụp màn hình khi đoạn lệnh không vướng các cảnh báo chết (Fatal Errors), hiển thị các RDD/MapReduce hoạt động xử lý JobID rõ ràng.

3. **Trích xuất chuỗi đầu ra TSV:**
   - In ấn thành phần dữ liệu xử lý theo định dạng chỉ định tab-separated (TSV) vào biến tệp `results.txt`.
   - **Thao tác minh chứng:** [Ảnh 11] Chụp màn hình lệnh truy vấn in kết quả (`cat results.txt` hoặc truy xuất từ hệ HDFS) chứng thực sự tuân thủ định dạng.

4. **Hoạt động giám thị logic (Cross-checking):**
   - Đảm nhiệm vai trò Chủ tọa cuộc họp kiểm toán vào ngày 28/03/2026.
   - Có thẩm quyền cao nhất chỉ đạo các cá nhân đọc mã nguồn, đối chiếu tập tin đáp số và phát hiện nếu có nhân sự gian lận thuật toán không tuân thủ MapReduce (Sử dụng biểu thức Regex hoặc xử lý ngoài cụm, sử dụng ngôn ngữ Python).

---

## V. GIAI ĐOẠN 4: THẨM ĐỊNH CHẤT LƯỢNG (QA) VÀ PHÂN ĐÓNG GÓI GIAO THỨC
> **Hạn chót yêu cầu nộp bài:** Ngày 01/04/2026.

1. **Nhiệm vụ kiểm soát luồng Git (Phiên bản hệ thống):**
   - Chỉ được kích hoạt sát nhập (Merge) vào cấu trúc nhánh rễ `main` khi nhánh con bộ phận (`member/<StudentID>`) qua được sự đánh giá chuyên môn toàn diện. Trách nghiệm sát nhập là cố hữu dành cho Trưởng nhóm.

2. **Lọc dữ liệu sai sót địa chỉ IP phân biệt:**
   - Điều phối cùng đồng sự D sử dụng bảng thông tin IP tập trung. Phán xét ngay lập tức, đình chỉ một phần các phân đoạn cài báo cáo cá nhân nếu bị phát hiện có trùng lặp địa chỉ IP chung tuyến liên kết, yêu sách báo cáo cài đặt bắt buộc phải tái lập trình với một cổng truy cập định tuyến hoàn toàn biệt lập (Ví dụ: Mobile 4G LTE/5G nội vi).

3. **Hoàn thể báo cáo và thông tin cá nhân A:**
   - Đảm bảo tập tài liệu `Report.pdf` mô tả quy trình mang màu sắc phân tích độc lập. Ghi nhận tính chính xác, không văn phong dập khuôn sao chép.
   - Hệ thống hóa đầy đủ tối thiểu 12 hình ảnh minh chứng. Bổ khuyết thêm ảnh giải trừ sự cố (Nếu có).
   - Tự hoàn thiện tập tài liệu `README.md` cung cấp sự hỗ trợ chạy khối lượng MapReduce Scala cá nhân cho hệ thống bình duyệt học thuật.

4. **Quy định bao gói cuối cùng:**
   - Khảo tra nghiêm ngặt kết cấu hệ sinh thái phân cấp tuyệt đối theo đúng điều lệnh trong `plan.md`:
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
         ├── <StudentID_B>/
         ...
     ```
   - Nhào nặn dữ liệu toàn hệ thống qua phần mềm nén Zip một thư tiệp cuối cùng và nộp ấn bản lên Moodle đúng thời hạn chỉ định quy củ. Tránh vi phạm định dạng nén khác biệt.
