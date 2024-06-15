#!/bin/bash

# 檢查是否有名為 img2tag 的容器正在運行
if docker ps | grep -q "img2tag"; then
    echo "停止運行中的容器..."
    docker stop img2tag

fi

# 檢查是否存在名為 img2tag 的容器（包括已停止的）
if docker ps -a | grep -q "img2tag"; then
    echo "刪除現有容器..."
    docker rm img2tag
fi

# 檢查是否存在名為 img2tag 的 image
if docker image ls -a | grep -q "img2tag"; then
    echo "刪除現有容器..."
    docker image rm img2tag
fi

# 從 .tar 檔案載入 Docker 映像
echo "從 .tar 檔案載入 Docker 映像..."
docker load -i img2tag.tar

# 運行新的容器
echo "運行新的容器..."
docker run -idt --name img2tag --network="host" -e RABBITMQ_HOST=192.168.2.232 -e RABBITMQ_PORT=5672 -e RABBITMQ_USER=james -e RABBITMQ_PASSWORD=Imagedj89684152 -v /var/www/html/rabbit/uploads:/app/uploads -v /var/www/Img2Tag/log:/app/data/log img2tag:latest

# 清理不需要的 Docker 映像和容器
echo "清理 Docker..."
docker system prune -f

echo "部署完成！"
