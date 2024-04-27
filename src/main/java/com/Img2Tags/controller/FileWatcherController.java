package com.Img2Tags.controller;

import com.Img2Tags.dto.ImageGetTagsApiResponseDTO;
import com.Img2Tags.dto.rabbitMQ.RabbitMQRequest;
import com.Img2Tags.exception.ApiRequestException;
import com.Img2Tags.service.FileWatcherService;
import com.Img2Tags.util.CSVFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

// 顯示資料夾內圖檔名稱
@Slf4j
@RestController
@RequestMapping("/fileWatcher")
public class FileWatcherController {

    private final FileWatcherService fileWatcherService;
    private final CSVFactory csvFactory;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FileWatcherController(
            FileWatcherService fileWatcherService,
            CSVFactory csvFactory ) {
        this.fileWatcherService = fileWatcherService;
        this.csvFactory = csvFactory;
    }

    // 圖片資料夾路徑
    @Value("${spring.servlet.multipart.location}")
    private String directoryPath;

    // 取得圖片資料夾下所有圖檔
    @GetMapping("/filenames")
    public ResponseEntity<List<String>> getFilenames() {
        List<String> fileNames = fileWatcherService
                .getFilenamesFromDirectory(directoryPath);
        return ResponseEntity.ok(fileNames);
    }

    @GetMapping("/imageGetTags")
    public ResponseEntity<Object> imageGetTags(RabbitMQRequest request) throws ApiRequestException {

        List<String> fileNameData = getFilenames().getBody();
        if (fileNameData == null || fileNameData.isEmpty()) {
            logger.warn("沒有取得檔案名稱資料");
            return ResponseEntity.badRequest().body("沒有取得檔案名稱資料");
        }
        String language = request.getFilePath();
        // 提供中英兩種版本
        language = language == null ? "zh_cht" : language;
        // 串接 Imagga API: /tags
        List<ImageGetTagsApiResponseDTO> getTagsResultList =
                fileWatcherService.getTagsToImagga(fileNameData , language);
        // 取得串接 Imagga API: /tags 成功清單
        List<ImageGetTagsApiResponseDTO> tagsSuccessResultList =
                fileWatcherService.tagsSuccessFilter(getTagsResultList);
        // 取得串接 Imagga API: /tags 失敗清單
        fileWatcherService.tagsFailFilter(getTagsResultList);
        //產出CSV檔案
        String csvContent = csvFactory.generateCSVFromDTO(tagsSuccessResultList);
        // 設定 headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image_tags.csv");

        return new ResponseEntity<>(csvContent.getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);

    }

    @DeleteMapping("/deleteFiles")
    public ResponseEntity<String> deleteFiles() {
        try {
            fileWatcherService.deleteAllFiles(directoryPath);
            return ResponseEntity.ok("所有文件已成功刪除");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("刪除文件時發生錯誤");
        }
    }
}