# 使用 OpenJDK 17 官方映像作為基礎映像
FROM openjdk:17

# 設定容器內部工作目錄
WORKDIR /app

# 將 Maven 打包的目標 WAR 檔案複製到容器內
COPY Img2Tags.war /app/Img2Tags.war

# 暴露容器的 8080 端口
EXPOSE 8080

# 運行 Java 應用
CMD ["java", "-jar", "Img2Tags.war"]