package com.Img2Tags.controller;

import com.Img2Tags.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


//  用戶端圖檔上傳至伺服器資料夾
@Slf4j
@Controller
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/batchFileUpload")
    public ResponseEntity<String> batchFileUpload(@RequestParam("files") List<MultipartFile> files) {
        logger.info("上傳文件至伺服器, 文件数量：{}", files.size());
        return ResponseEntity.ok(fileUploadService.batchFileUpload(files));
    }
}