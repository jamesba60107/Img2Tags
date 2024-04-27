#!/bin/bash

# 檢查是否有名為 tg-game 的容器正在運行

if docker ps | grep -q "img2tag"; then
echo "停止運行中的容器..."
docker stop img2tag
fi

# 檢查是否存在名為 img2tag 的容器（包括已停止的）

if docker ps -a | grep -q "img2tag"; then
echo "刪除現有容器..."
docker rm img2tag
fi

# 構建新的 Docker 映像

echo "構建新的 Docker 映像..."
docker build -t img2tag:latest .

# 運行新的容器

echo "運行新的容器..."

docker run -idt --name img2tag -p 8080:8080 -v /var/www/html/rabbit/uploads:/Img2Tags/uploads img2tag:latest

# 清理不需要的 Docker 映像和容器

echo "清理 Docker..."
docker system prune -f

echo "部署完成！"