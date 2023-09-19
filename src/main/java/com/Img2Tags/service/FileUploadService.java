package com.Img2Tags.service;

import com.Img2Tags.config.FileUploadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class FileUploadService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 獲取圖片資料夾路徑
    private final FileUploadConfig fileUploadConfig;

    @Autowired
    public FileUploadService(FileUploadConfig fileUploadConfig) {
        this.fileUploadConfig = fileUploadConfig;
    }

    public String batchFileUpload(List<MultipartFile> files) {
        if (files.isEmpty()) {
            logger.info("上傳的文件不應該為空");
            throw new IllegalArgumentException("上傳的文件不應該為空。");
        }

        StringBuilder responseMessage = new StringBuilder();

        for (MultipartFile file : files) {
            try {
                // 儲存文件
                Path savedPath = saveUploadedFile(file);
                responseMessage.append("文件[").append(file.getOriginalFilename()).append("]成功上傳至: ").append(savedPath).append("\n");
            } catch (IOException e) {
                // 如果遇到異常, 將錯誤訊息添加到回應中, 但仍繼續處理其他文件
                responseMessage.append("文件[").append(file.getOriginalFilename()).append("]上傳檔案重複：").append(e.getMessage()).append("\n");
            }
        }

        logger.info(String.valueOf(responseMessage));
        return responseMessage.toString();
    }

    // 儲存由使用者上傳至伺服器的圖檔
    private Path saveUploadedFile(MultipartFile file) throws IOException {
        // 確定上傳目錄存在, 如果不存在則建立該目錄
        Path uploadDirPath = Paths.get(fileUploadConfig.getUPLOAD_DIR());
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }

        // 解析並保存文件
        Path filePath = uploadDirPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.copy(file.getInputStream(), filePath);

        return filePath;
    }
}