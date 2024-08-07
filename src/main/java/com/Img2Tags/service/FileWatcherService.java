package com.Img2Tags.service;

import com.Img2Tags.dto.ImageGetTagsApiResponseDTO;
import com.Img2Tags.exception.ApiRequestException;
import com.Img2Tags.service.api.ImageGetTagsApiService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
public class FileWatcherService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ImageGetTagsApiService imageGetTagsApiService;

    // 取得 傳入參數之資料夾路徑下 所有檔案名稱
    public List<String> getFilenamesFromDirectory(String directoryPath) {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(listOfFiles)
                .filter(File::isFile)
                .filter(file -> {
                    String filename = file.getName().toLowerCase();
                    return filename.endsWith(".jpg") || filename.endsWith(".png");
                })
                .map(File::getName)
                .collect(Collectors.toList());
    }

    //串接 Imagga API: tags
    public List<ImageGetTagsApiResponseDTO> getTagsToImagga(String filePath, List<String> fileNameData, String language) throws ApiRequestException{

        List<ImageGetTagsApiResponseDTO> resultList;
            resultList = fileNameData.stream()
                    .map(data -> imageGetTagsApiService.tags(filePath, data, language))
                    .collect(Collectors.toList());
        return ResponseEntity.ok(resultList).getBody();
    }

    // 取得upload_id 成功清單
    public List<ImageGetTagsApiResponseDTO> tagsSuccessFilter(List<ImageGetTagsApiResponseDTO> tagsResultList) {

        List<ImageGetTagsApiResponseDTO> tagsSuccessResultList =
                tagsResultList.stream()
                        .filter(data -> "success".equals((data.getStatus().getType())))
                        .toList();

        if (!tagsSuccessResultList.isEmpty()) {
            logger.info("串接Imagga API: upload, 成功數量: " + tagsSuccessResultList.size());
            logger.info(tagsSuccessResultList.toString());
        }

        return tagsSuccessResultList;
    }

    // 取得upload_id 失敗清單
    public List<ImageGetTagsApiResponseDTO> tagsFailFilter(List<ImageGetTagsApiResponseDTO> tagsResultList) {

        List<ImageGetTagsApiResponseDTO> tagsFailResultList =
                tagsResultList.stream()
                        .filter(data -> !"success".equals((data.getStatus().getType())))
                        .toList();

        if (!tagsFailResultList.isEmpty()) {
            logger.info("串接Imagga API: upload, 失敗數量: " + tagsFailResultList.size());
            logger.info(tagsFailResultList.toString());
        }

        return tagsFailResultList;
    }

    public void deleteAllFiles(String directoryPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath), FileVisitOption.FOLLOW_LINKS)) {
            paths.sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        log.error("刪除檔案失敗: " + e.getMessage());
                        throw new UncheckedIOException(e);
                    }
                 });
        }
    }
}
