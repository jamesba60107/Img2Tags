# 使用 Ubuntu 最新版本作為基礎映像
FROM ubuntu:latest

# 更新軟件包列表
RUN apt-get update

# 安裝必要的語言包和設定 locale
RUN apt-get install -y locales \
    && locale-gen en_US.UTF-8

# 環境變量設定
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# 安裝 OpenJDK 17
RUN apt-get install -y openjdk-17-jdk

# 設定環境變量，指定 JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64

# 設定容器內部工作目錄
WORKDIR /app

# 將 Maven 打包的目標 WAR 檔案複製到容器內
COPY /target/Img2Tags-0.0.1-SNAPSHOT.war /app/Img2Tags.war

# 暴露容器的 8080 端口
EXPOSE 8080

# 運行 Java 應用
CMD ["java", "-Xmx512m", "-jar", "Img2Tags.war"]
