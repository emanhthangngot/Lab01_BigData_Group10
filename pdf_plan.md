# KẾ HOẠCH THIẾT KẾ FILE BÁO CÁO (Report.pdf)

> Tài liệu đặc tả chi tiết cho AI Agent: từ file này có thể sinh ra một template LaTeX hoàn chỉnh, biên dịch trực tiếp thành `Report.pdf`.

---

## I. THÔNG TIN CÁ NHÂN (Placeholder — Agent cần điền)

| Trường             | Giá trị                                         |
| :----------------- | :----------------------------------------------- |
| **Họ và Tên**      | `\VAR{studentName}` — mặc định: Lê Xuân Trí     |
| **MSSV**           | `\VAR{studentId}` — mặc định: 23120099           |
| **Hệ điều hành**   | `\VAR{osName}` — mặc định: Arch Linux (native)   |
| **Vai trò nhóm**   | `\VAR{role}` — mặc định: Leader (A)              |
| **Ngày báo cáo**   | `\VAR{reportDate}` — mặc định: 30/03/2026        |
| **Thư mục ảnh**    | `\VAR{imgDir}` — mặc định: `image/`              |

---

## II. QUY CÁCH FORMAT CHUNG

### 1. Trang giấy & Font

| Thuộc tính         | Giá trị                                |
| :------------------ | :-------------------------------------- |
| Khổ giấy           | A4                                      |
| Lề (trên/dưới)     | 2.5 cm                                  |
| Lề (trái/phải)     | 2.5 cm                                  |
| Font chính          | Times New Roman (hoặc `times` trong TeX)|
| Cỡ chữ body        | 12pt                                    |
| Giãn dòng          | 1.25                                    |
| Ngôn ngữ           | Tiếng Việt (UTF-8, gói `babel` hoặc `fontspec` + `polyglossia`) |
| Engine biên dịch    | **XeLaTeX** hoặc **LuaLaTeX** (hỗ trợ Unicode tiếng Việt tốt nhất) |

### 2. Heading Hierarchy

| Level | LaTeX command       | Cỡ chữ  | Style          | Đánh số |
| :---: | :------------------ | :------: | :------------- | :-----: |
| H1    | `\section`          | 16pt     | **Bold**       | Có (I, II, ...) |
| H2    | `\subsection`       | 14pt     | **Bold**       | Có (1, 2, ...) |
| H3    | `\subsubsection`    | 13pt     | **Bold Italic** | Có (a, b, ...) |

### 3. Code Block & Listing

- Dùng package `minted` (ưu tiên) hoặc `listings`.
- Font monospace: `Fira Code`, `JetBrains Mono`, hoặc `Cascadia Code`.
- Highlight syntax cho ngôn ngữ: `bash`, `scala`, `xml`.
- Nền code: màu xám nhạt (`\definecolor{codebg}{RGB}{245,245,245}`).
- Có đánh số dòng cho code Scala dài.

### 4. Hình ảnh / Screenshot

- Tất cả ảnh nằm trong thư mục `image/` tương đối so với file `.tex`.
- Dùng `\includegraphics[width=\textwidth]{image/<filename>}`.
- Mỗi hình **BẮT BUỘC** có `\caption{...}` và `\label{fig:...}`.
- Chiều rộng mặc định: `\textwidth` (toàn trang) hoặc `0.85\textwidth`.
- Ảnh được wrap trong `figure` environment, vị trí `[H]` (package `float`).

### 5. Bảng

- Dùng `tabularx` hoặc `longtable` cho bảng dài.
- Header đậm, có đường kẻ ngang `\hline` hoặc `\toprule`/`\midrule`/`\bottomrule` (package `booktabs`).

### 6. Trang bìa

- Tên trường: **ĐẠI HỌC QUỐC GIA TP.HCM — TRƯỜNG ĐẠI HỌC KHOA HỌC TỰ NHIÊN**
- Tên môn: **Nhập môn Dữ liệu lớn (Introduction to Big Data)**
- Tiêu đề: **BÁO CÁO LAB 01: GIỚI THIỆU HỆ SINH THÁI HADOOP**
- Thông tin: Họ tên, MSSV, Ngày nộp.
- Logo trường (nếu có) hoặc để trống.

---

## III. CẤU TRÚC NỘI DUNG BÁO CÁO

> **Quy tắc vàng:** Mỗi bước phải có (1) mô tả bằng lời, (2) lệnh terminal, (3) ảnh chụp minh chứng.
> Agent khi render phải chèn đúng file ảnh theo mapping ở Mục IV.

---

### TRANG BÌA (1 trang riêng, không đánh số)

Nội dung: xem Mục II.6.

---

### MỤC LỤC (tự sinh bởi `\tableofcontents`)

---

### PHẦN 1: CÀI ĐẶT HADOOP PSEUDO-DISTRIBUTED (≈ 8–12 trang)

Phần này chiếm **4.5 / 10 điểm** — phải trình bày kỹ lưỡng.

#### 1.1. Môi trường hệ thống

- Mô tả ngắn: OS, phiên bản kernel, lý do chọn OS.
- **Screenshot:** `ip addr` hoặc `ifconfig` → chứng minh IP riêng biệt.

#### 1.2. Cài đặt Java và Scala

- Lệnh cài: `pacman -S jdk11-openjdk scala` (Arch) hoặc tương đương.
- Xác nhận: `java -version`, `scala -version`.
- **Screenshot:** kết quả cài đặt thành công.

#### 1.3. Tải và giải nén Hadoop

- Lệnh: `wget https://...hadoop-X.Y.Z.tar.gz`, `tar -xzf ...`
- Xác nhận thư mục giải nén.
- **Screenshot:** kết quả giải nén.

#### 1.4. Cấu hình biến môi trường

- Nội dung thêm vào `~/.bashrc` hoặc `/etc/environment`:
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
  export HADOOP_HOME=/opt/hadoop
  export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
  ```
- **Screenshot:** `cat ~/.bashrc` (phần liên quan) + `echo $HADOOP_HOME`.

#### 1.5. Cấu hình XML (core-site.xml, hdfs-site.xml)

- In nội dung 2 file XML đã sửa (dùng code block `xml`).
- Giải thích tham số: `fs.defaultFS`, `dfs.replication`, `dfs.namenode.name.dir`, `dfs.datanode.data.dir`.
- **Screenshot:** `cat core-site.xml` và `cat hdfs-site.xml`.

#### 1.6. Format NameNode

- Lệnh: `hdfs namenode -format`
- Giải thích: tại sao cần format, khi nào cần format lại.
- **Screenshot:** kết quả "successfully formatted".

#### 1.7. Khởi động Cluster

- Lệnh: `start-dfs.sh`, `start-yarn.sh`
- **Screenshot:** output của từng lệnh.

#### 1.8. Kiểm tra tiến trình (`jps`)

- Lệnh: `jps`
- Liệt kê 4 daemon bắt buộc: NameNode, DataNode, ResourceManager, NodeManager.
- **Screenshot:** output `jps` hiển thị đủ 4 tiến trình.

#### 1.9. WebUI — Giao diện quản trị

- Truy cập: `http://localhost:9870`
- **Screenshot:** tab Datanodes hiển thị Live Nodes.

#### 1.10. Tạo thư mục HDFS & User

- Lệnh:
  ```bash
  hdfs dfs -mkdir /hcmus
  sudo useradd -m khtn_23120099
  hdfs dfs -mkdir /hcmus/23120099
  ```
- **Screenshot:** kết quả từng lệnh.

#### 1.11. Tải dữ liệu lên HDFS

- Lệnh:
  ```bash
  hdfs dfs -put words.txt /hcmus/23120099/
  hdfs dfs -ls /hcmus/23120099/
  ```
- **Screenshot:** xác nhận file đã tải lên.

#### 1.12. Phân quyền và chuyển Owner

- Lệnh:
  ```bash
  hdfs dfs -chmod 744 /hcmus/23120099
  hdfs dfs -chown khtn_23120099 /hcmus/23120099
  hdfs dfs -ls /hcmus/
  ```
- **Screenshot:** kết quả quyền mới.

#### 1.13. Chạy file JAR xác thực

- Lệnh: `java -jar hadoop-test.jar <PORT> /hcmus/23120099`
- Xác nhận file `23120099_verification.txt` được sinh ra.
- In nội dung file verification.
- **Screenshot:** quá trình chạy + kết quả.

#### 1.14. Xử lý sự cố (nếu có)

- Mỗi lỗi trình bày theo format 3 phần:
  1. **Hiện tượng:** mô tả lỗi + screenshot lỗi.
  2. **Phân tích:** nguyên nhân, nguồn tham khảo (Google, StackOverflow).
  3. **Giải pháp:** lệnh/thao tác sửa + screenshot sau khi sửa thành công.

---

### PHẦN 2: WORDCOUNT BẰNG SCALA (≈ 4–6 trang)

Phần này chiếm **5.0 / 10 điểm**.

#### 2.1. Giới thiệu bài toán

- Yêu cầu: đếm số từ bắt đầu bằng `f, i, t, h, c, m, u, s` (case-insensitive).
- Ràng buộc: Scala, đọc từ HDFS, không dùng regex, output TSV.

#### 2.2. Mã nguồn Scala (WordCount.scala)

- Chèn toàn bộ code bằng `minted` hoặc `listings`, ngôn ngữ `scala`.
- Đánh số dòng, highlight syntax.
- Giải thích từng phần logic:
  - Đọc file từ HDFS: `sc.textFile("hdfs://...")`
  - Tách từ: `split(" ")` + filter chuỗi rỗng
  - Chuẩn hóa: `toLowerCase`
  - Lọc ký tự đầu: `startsWith`
  - Đếm: `map` + `reduceByKey`
  - Xuất TSV: `\t` separator

#### 2.3. Hướng dẫn biên dịch và chạy

- Cách build: `sbt package` hoặc compile trực tiếp.
- Lệnh chạy: `spark-submit --class WordCount ... hdfs://...`
- **Screenshot:** terminal chạy thành công (không lỗi).

#### 2.4. Kết quả

- In nội dung `results.txt` (TSV):
  ```
  f	<count>
  i	<count>
  ...
  ```
- **Screenshot:** `cat results.txt` hiển thị output.

#### 2.5. Kiểm thử

- Mô tả file test nhỏ dùng để kiểm tra logic.
- So sánh kết quả chương trình vs đáp án tính tay.

---

### PHẦN 3: KẾT LUẬN (≈ 0.5–1 trang)

- Tóm tắt những gì đã học được.
- Khó khăn gặp phải và bài học rút ra.
- Đề xuất cải thiện (nếu có).

---

### PHỤ LỤC (tuỳ chọn)

- Bảng đối chiếu IP toàn nhóm.
- Cấu hình đầy đủ các file XML khác (`mapred-site.xml`, `yarn-site.xml`).

---

## IV. MAPPING ẢNH CHỤP → VỊ TRÍ TRONG BÁO CÁO

> Agent khi sinh LaTeX phải dùng mapping này để chèn `\includegraphics` đúng chỗ.
> **Quy ước tên file ảnh:** giữ nguyên tên gốc trong thư mục `image/`.

| File ảnh                                       | Mục   | Caption gợi ý                                            |
| :--------------------------------------------- | :---: | :-------------------------------------------------------- |
| `Screenshot From 2026-03-22 10-36-24.png`      | 1.2   | Cài đặt Java và Scala thành công                          |
| `Screenshot From 2026-03-22 10-45-42.png`      | 1.3   | Tải và giải nén Hadoop                                    |
| `Screenshot From 2026-03-22 10-53-22.png`      | 1.4   | Cấu hình biến môi trường                                  |
| `Screenshot From 2026-03-22 10-54-18.png`      | 1.5   | Cấu hình core-site.xml và hdfs-site.xml                   |
| `Screenshot From 2026-03-22 10-59-08.png`      | 1.6   | Format NameNode thành công                                 |
| `Screenshot From 2026-03-22 11-04-52.png`      | 1.7   | Khởi động DFS và YARN                                     |
| `Screenshot From 2026-03-22 11-07-01.png`      | 1.8   | Kiểm tra tiến trình bằng jps                              |
| `Screenshot From 2026-03-22 11-09-31.png`      | 1.9   | WebUI Hadoop — tab Datanodes                               |
| `Screenshot From 2026-03-22 11-13-37.png`      | 1.10  | Tạo thư mục HDFS và user Linux                            |
| `Screenshot From 2026-03-22 11-17-36.png`      | 1.11  | Tải file dữ liệu lên HDFS                                 |
| `Screenshot From 2026-03-22 11-19-35.png`      | 1.12  | Phân quyền chmod 744 và chown                              |
| `Screenshot From 2026-03-22 11-22-20.png`      | 1.13  | Chạy file JAR xác thực                                    |
| `Screenshot From 2026-03-22 11-23-13.png`      | 1.13  | Nội dung file verification.txt                             |
| `Screenshot From 2026-03-22 11-38-28.png`      | 1.14  | Xử lý sự cố — hiện tượng lỗi                             |
| `Screenshot From 2026-03-22 11-41-52.png`      | 1.14  | Xử lý sự cố — sau khi sửa thành công                     |
| `Screenshot From 2026-03-22 11-54-29.png`      | 1.1   | Minh chứng IP — lệnh ip addr                              |

> **Lưu ý:** Mapping trên là **dự kiến** dựa trên thứ tự thời gian chụp. Agent khi render cần xác nhận lại nội dung thực tế của từng ảnh nếu có thể (qua view_file binary), hoặc cho phép user re-order.

---

## V. ĐẶC TẢ LATEX TEMPLATE CHO AGENT

> Phần này mô tả chính xác cấu trúc file `.tex` mà agent cần sinh ra.

### 5.1. Danh sách Package bắt buộc

```latex
% === Engine: XeLaTeX hoặc LuaLaTeX ===
\documentclass[12pt,a4paper]{article}

% --- Ngôn ngữ & Font ---
\usepackage{fontspec}           % Font hệ thống (XeLaTeX/LuaLaTeX)
\usepackage{polyglossia}        % Hỗ trợ đa ngôn ngữ
\setdefaultlanguage{vietnamese}
\setmainfont{Times New Roman}   % Font chính
\setmonofont{JetBrains Mono}[Scale=0.85]  % Font code

% --- Layout ---
\usepackage[margin=2.5cm]{geometry}
\usepackage{setspace}           % Giãn dòng
\onehalfspacing                 % 1.5 line spacing (gần 1.25 Word)

% --- Hình ảnh ---
\usepackage{graphicx}
\usepackage{float}              % Vị trí [H]
\graphicspath{{image/}}         % Đường dẫn ảnh

% --- Bảng ---
\usepackage{booktabs}           % \toprule, \midrule, \bottomrule
\usepackage{tabularx}
\usepackage{longtable}

% --- Code ---
\usepackage{minted}             % Syntax highlighting
\usemintedstyle{friendly}
\definecolor{codebg}{RGB}{245,245,245}
\setminted{
  bgcolor=codebg,
  fontsize=\footnotesize,
  breaklines=true,
  frame=single,
  framesep=2mm,
}

% --- Tiện ích ---
\usepackage{hyperref}           % Link nội bộ + bookmark
\usepackage{xcolor}
\usepackage{enumitem}           % Tuỳ chỉnh list
\usepackage{fancyhdr}           % Header/Footer
\usepackage{titlesec}           % Tuỳ chỉnh heading
\usepackage{caption}            % Tuỳ chỉnh caption
```

### 5.2. Cấu trúc file `.tex`

```
report.tex                 ← File chính (master)
├── image/                 ← Thư mục chứa screenshot (16 file .png)
└── (không cần split thành nhiều file .tex nếu report < 30 trang)
```

### 5.3. Template Skeleton (Agent sinh toàn bộ nội dung trong 1 file)

```latex
\begin{document}

% === TRANG BÌA ===
\begin{titlepage}
  \centering
  {\large ĐẠI HỌC QUỐC GIA TP.HCM}\\
  {\large TRƯỜNG ĐẠI HỌC KHOA HỌC TỰ NHIÊN}\\[1cm]
  {\Large\bfseries NHẬP MÔN DỮ LIỆU LỚN}\\[0.5cm]
  \rule{\textwidth}{0.4pt}\\[1cm]
  {\LARGE\bfseries BÁO CÁO LAB 01}\\[0.3cm]
  {\Large Giới thiệu Hệ sinh thái Hadoop}\\[2cm]
  % Thông tin sinh viên
  \begin{tabular}{ll}
    \textbf{Họ và Tên:}  & \VAR{studentName} \\
    \textbf{MSSV:}        & \VAR{studentId} \\
    \textbf{Hệ điều hành:}& \VAR{osName} \\
    \textbf{Ngày nộp:}    & \VAR{reportDate} \\
  \end{tabular}
\end{titlepage}

% === MỤC LỤC ===
\newpage
\tableofcontents
\newpage

% === PHẦN 1: HADOOP CLUSTER ===
\section{Cài đặt Hadoop Pseudo-Distributed}

\subsection{Môi trường hệ thống}
% Nội dung + screenshot ip addr

\subsection{Cài đặt Java và Scala}
% Lệnh + screenshot

\subsection{Tải và giải nén Hadoop}
% Lệnh + screenshot

\subsection{Cấu hình biến môi trường}
% Code bash + screenshot

\subsection{Cấu hình XML}
% Code xml + screenshot
% Giải thích tham số

\subsection{Format NameNode}
% Lệnh + screenshot

\subsection{Khởi động Cluster}
% Lệnh + screenshot

\subsection{Kiểm tra tiến trình (jps)}
% Lệnh + screenshot

\subsection{WebUI Hadoop}
% URL + screenshot

\subsection{Tạo thư mục HDFS và User}
% Chuỗi lệnh + screenshot

\subsection{Tải dữ liệu lên HDFS}
% Lệnh + screenshot

\subsection{Phân quyền và chuyển Owner}
% Lệnh + screenshot

\subsection{Chạy file JAR xác thực}
% Lệnh + output + screenshot

\subsection{Xử lý sự cố}
% Nếu có: 3 phần (Hiện tượng / Phân tích / Giải pháp)

% === PHẦN 2: WORDCOUNT ===
\section{WordCount bằng Scala}

\subsection{Giới thiệu bài toán}
% Yêu cầu, ràng buộc

\subsection{Mã nguồn Scala}
% \inputminted{scala}{../src/WordCount/WordCount.scala}
% Giải thích logic

\subsection{Hướng dẫn biên dịch và chạy}
% Build + spark-submit + screenshot

\subsection{Kết quả}
% results.txt (TSV) + screenshot

\subsection{Kiểm thử}
% File test + so sánh kết quả

% === PHẦN 3: KẾT LUẬN ===
\section{Kết luận}
% Tóm tắt + khó khăn + bài học

\end{document}
```

### 5.4. Quy tắc nhất quán cho Agent khi sinh nội dung

| # | Quy tắc |
|---|---------|
| 1 | **Mỗi subsection** phải có ít nhất 1 `\begin{figure}[H]...\end{figure}` chèn screenshot tương ứng (theo mapping Mục IV). |
| 2 | **Mỗi lệnh terminal** phải nằm trong `\begin{minted}{bash}...\end{minted}`. |
| 3 | **Mỗi file XML/Scala** phải nằm trong `\begin{minted}{xml}` hoặc `\begin{minted}{scala}`. |
| 4 | **Caption** ảnh phải mô tả rõ nội dung, bắt đầu bằng "Hình X.Y: ..." (LaTeX tự đánh số qua `\caption`). |
| 5 | **Không để placeholder** — nếu thiếu thông tin, ghi `% TODO: ...` rồi tiếp tục. |
| 6 | Phần **Xử lý sự cố** (1.14) chỉ xuất hiện nếu có ảnh lỗi. Nếu không có thì bỏ qua hoặc ghi "Quá trình cài đặt diễn ra thuận lợi, không gặp lỗi." |
| 7 | **Verification file** nội dung `23120099_verification.txt` phải được in nguyên văn trong mục 1.13. |
| 8 | Viết bằng **lời văn cá nhân**, nghiêm cấm copy-paste từ báo cáo thành viên khác. |
| 9 | Khi giải thích code Scala, dùng **inline code** `\mintinline{scala}{...}` cho tên hàm/biến. |
| 10 | Tổng report **15–20 trang** (kể cả trang bìa và mục lục). |

---

## VI. CHECKLIST TRƯỚC KHI BIÊN DỊCH

Agent sau khi sinh xong file `.tex` phải tự kiểm tra:

- [ ] Tất cả 16 file ảnh đều được `\includegraphics` ít nhất 1 lần?
- [ ] Mỗi `\begin{figure}` đều có `\caption` và `\label`?
- [ ] Nội dung `23120099_verification.txt` được chèn đúng mục 1.13?
- [ ] Code Scala WordCount có highlight đúng ngôn ngữ `scala`?
- [ ] Trang bìa có đủ: tên trường, tên môn, tiêu đề, tên + MSSV?
- [ ] `\tableofcontents` sinh đúng mục lục?
- [ ] Không còn `\VAR{...}` placeholder nào chưa thay thế?
- [ ] File biên dịch không lỗi bằng `xelatex -shell-escape report.tex`?
- [ ] Output PDF ≤ 20 trang?

---

## VII. LỆNH BIÊN DỊCH

```bash
# Lần 1: sinh .aux
xelatex -shell-escape report.tex

# Lần 2: sinh mục lục đúng
xelatex -shell-escape report.tex

# (Tuỳ chọn) Lần 3: nếu có cross-reference
xelatex -shell-escape report.tex
```

> Flag `-shell-escape` bắt buộc khi dùng `minted` (gọi Pygments bên ngoài).

---

## VIII. THỨ TỰ ƯU TIÊN KHI AGENT GEN FILE .TEX

1. **Sinh preamble** (packages, colors, settings) — copy từ Mục V.1.
2. **Sinh trang bìa** — thay `\VAR{...}` bằng giá trị thực.
3. **Sinh nội dung Phần 1** (14 subsections) — chèn lệnh bash + ảnh.
4. **Sinh nội dung Phần 2** (5 subsections) — chèn code Scala + ảnh.
5. **Sinh Phần 3** (kết luận).
6. **Chạy checklist** Mục VI.
7. **Biên dịch thử** nếu có terminal access.
