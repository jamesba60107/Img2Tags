# 使用 Ubuntu 最新版本作為基礎映像
FROM ubuntu:latest

# 更新軟件包列表
RUN apt-get update

# 安裝 OpenJDK 17
RUN apt-get install -y openjdk-17-jdk

# 設定環境變量，指定 JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64

# 設定容器內部工作目錄
WORKDIR /app

# 將 Maven 打包的目標 WAR 檔案複製到容器內
COPY target/Img2Tags.war /app/Img2Tags.war

# 暴露容器的 80 端口
EXPOSE 80

# 運行 Java 應用
CMD ["java", "-jar", "Img2Tags.war"]
