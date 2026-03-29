#!/bin/bash

# HƯỚNG DẪN: Chạy script này bằng lệnh: bash setup_hadoop_env.sh

# 1. Xác định thông tin hệ thống
OS_TYPE=$(cat /etc/os-release | grep ^ID= | cut -d'=' -f2 | tr -d '"')
USER_HOME=$HOME
HADOOP_DIR="$USER_HOME/hadoop"

echo "Detected OS: $OS_TYPE"

# 2. Xác định JAVA_HOME dựa trên OS
if [ "$OS_TYPE" == "arch" ]; then
    JAVA_PATH="/usr/lib/jvm/java-8-openjdk"
elif [ "$OS_TYPE" == "ubuntu" ] || [ "$OS_TYPE" == "debian" ]; then
    JAVA_PATH="/usr/lib/jvm/java-8-openjdk-amd64"
else
    echo "OS lạ, vui lòng nhập đường dẫn JAVA_HOME tay:"
    read JAVA_PATH
fi

# 3. Cấu hình .bashrc
echo "--- Configuring .bashrc ---"
cat <<EOF >> ~/.bashrc

# Hadoop Environment Variables
export JAVA_HOME=$JAVA_PATH
export HADOOP_HOME=$HADOOP_DIR
export HADOOP_CONF_DIR=\$HADOOP_HOME/etc/hadoop
export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin
EOF

# 4. Cấu hình hadoop-env.sh (Fix lỗi SSH JAVA_HOME)
echo "--- Configuring hadoop-env.sh ---"
if [ -d "$HADOOP_DIR/etc/hadoop" ]; then
    echo "export JAVA_HOME=$JAVA_PATH" >> "$HADOOP_DIR/etc/hadoop/hadoop-env.sh"
    echo "Hadoop configuration found and patched."
else
    echo "Error: Hadoop directory not found at $HADOOP_DIR. Hãy cài Hadoop trước!"
fi

# 5. Tạo thư mục dữ liệu
echo "--- Creating Data Directories ---"
mkdir -p "$USER_HOME/hadoop_data/namenode"
mkdir -p "$USER_HOME/hadoop_data/datanode"

echo "Xong! Hãy chạy lệnh 'source ~/.bashrc' để áp dụng thay đổi."
