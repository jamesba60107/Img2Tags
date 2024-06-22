# 使用官方 Java 運行環境作為基礎鏡像
FROM openjdk:17-jdk-slim

# 更新軟件包列表並安裝必要的語言包和設定 locale 以支援中文
RUN apt-get update \
    && apt-get install -y locales \
    && echo "zh_TW.UTF-8 UTF-8" > /etc/locale.gen \
    && locale-gen zh_TW.UTF-8 \
    && update-locale LANG=zh_TW.UTF-8

# 設定環境變量
ENV LANG zh_TW.UTF-8
ENV LANGUAGE zh_TW:zh
ENV LC_ALL zh_TW.UTF-8

# 設定容器內部工作目錄
WORKDIR /app

# 將應用的 war 文件從構建上下文複製到容器內
COPY target/Img2Tags-0.0.1-SNAPSHOT.war /app/Img2Tags.war

# 容器啟動時執行的命令，啟動 Spring Boot 應用
CMD ["java", "-Xmx512m", "-Dfile.encoding=UTF-8", "-jar", "Img2Tags.war"]

# 暴露容器的 8080 端口
EXPOSE 8080
