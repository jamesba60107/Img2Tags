package com.Img2Tags.service.rabbitMQ;

import com.Img2Tags.dto.ImageGetTagsApiResponseDTO;
import com.Img2Tags.dto.rabbitMQ.RabbitMQResponse;
import com.Img2Tags.service.FileWatcherService;
import com.Img2Tags.util.CSVFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

@Slf4j
@Service
public class RabbitMQListener {

    private final ObjectMapper mapper;
    private final CSVFactory csvFactory;
    private final FileWatcherService fileWatcherService;
    private final RabbitMQService rabbitMQService;

    public RabbitMQListener(ObjectMapper mapper,
                            CSVFactory csvFactory,
                            FileWatcherService fileWatcherService,
                            RabbitMQService rabbitMQService){
        this.mapper = mapper;
        this.csvFactory = csvFactory;
        this.fileWatcherService = fileWatcherService;
        this.rabbitMQService = rabbitMQService;
    }
    /**
    *example message:
    *{
    *  "requestId": "123456",
    *  "userId": "78910",
    *  "imageCount": 3,
    *  “language": "en",
    *  "filePath": "/images/78910/",
    *  "fileName": [ "image1.jpg", "image2.jpg", "image3.jpg" ]
    *}
    **/
    @RabbitListener(queues = "IDJ")
    public void receiveMessage(String message) {
        try {
            log.info("從RabbitMQ接收到的message: {}", message);
            JsonNode node = mapper.readTree(message);
            String requestId = node.has("requestId") ? node.get("requestId").asText() : "未接收到requestId";
            String language = node.has("language") ? node.get("language").asText() : "zh_cht";
            String userId = node.has("userId") ? node.get("userId").asText() : "未接收到userId";
            String filePath = node.get("filePath").asText();
            filePath = filePath.endsWith("/") ? filePath : filePath + "/";

            List<ImageGetTagsApiResponseDTO> getTagsResultList = fileWatcherService.getTagsToImagga(
                        filePath,
                        fileWatcherService.getFilenamesFromDirectory(filePath),
                        language);
            List<ImageGetTagsApiResponseDTO> tagsSuccessResultList =
                    fileWatcherService.tagsSuccessFilter(getTagsResultList);
            List<ImageGetTagsApiResponseDTO> tagsFailureResultList =
                    fileWatcherService.tagsFailFilter(getTagsResultList);
            String csvContent = csvFactory.generateCSVFromDTO(tagsSuccessResultList);

            File directory = new File(filePath);
            if (!directory.exists() && !directory.mkdirs()) {
                log.error("無法創建目錄" + filePath);
                return;
            }
            String csvName = requestId + "-" + language + "-img2tag.csv";
            String csvFilePath = filePath + csvName;
            try (FileWriter writer = new FileWriter(csvFilePath)) {
                writer.write(csvContent);

                RabbitMQResponse response = new RabbitMQResponse();
                response.setRequestId(requestId);
                response.setLanguage(language);
                response.setUserId(userId);
                response.setSuccessCount(tagsSuccessResultList.size());
                response.setFailCount(tagsFailureResultList.size());
                response.setCsvName(csvName);
                response.setCsvPath(filePath);
                rabbitMQService.sendImg2TagCSVCompleteToQueue(response);
                log.info("CSV 檔案已成功寫入到: " + csvFilePath);
            } catch (Exception e) {
                log.error("寫入CSV時出錯: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("處理消息時出錯: {}", e.getMessage(), e);
        }
    }
}
