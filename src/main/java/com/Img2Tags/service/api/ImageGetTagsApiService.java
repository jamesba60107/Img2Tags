package com.Img2Tags.service.api;

import com.Img2Tags.config.ImaggaConfig;
import com.Img2Tags.dto.ImageGetTagsApiResponseDTO;
import com.Img2Tags.exception.ApiRequestException;
import com.Img2Tags.util.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ImageGetTagsApiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定義 HTTP 請求
    private final ImaggaConfig imaggaConfig;
    private final JsonConverter jsonConverter;
    // 圖片資料夾路徑
    @Value("${spring.servlet.multipart.location}")
    private String directoryPath;

    public int errorCode = 0;


    @Autowired
    public ImageGetTagsApiService(ImaggaConfig imaggaConfig, JsonConverter jsonConverter) {
        this.imaggaConfig = imaggaConfig;
        this.jsonConverter = jsonConverter;
    }

    public ImageGetTagsApiResponseDTO tags(String filePath, String imageName, String language) throws ApiRequestException {

        String basicAuth = imaggaConfig.getAuthorization();
        String response = null;
        // filePath 如果是null : 代表是本系統前台頁面上傳的, 需補上路徑"/images/"
        // filePath 如果非null : 代表是rabbit系統傳來的
        String uploadPath = filePath == null ? directoryPath + imageName : filePath + imageName;
        File fileToUpload = new File(uploadPath);

        String endpoint = "/tags?language=" + language;
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "Image Upload";
        StringBuilder stringBuilder = new StringBuilder();
        // 取得Response Code
        int responseCode = 0;

        try {
            URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
            request.writeBytes(crlf);

            InputStream inputStream = new FileInputStream(fileToUpload);
            int bytesRead;
            byte[] dataBuffer = new byte[1024];
            while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
                request.write(dataBuffer, 0, bytesRead);
            }

            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();

            //取得Response Status
            responseCode = connection.getResponseCode();

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(
                    new InputStreamReader(responseStream, StandardCharsets.UTF_8));

            String line = "";

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            // 把回傳 json 的 字串 換成 tagName
            response = response.replace("\"zh_cht\"", "\"tagName\"");
            response = response.replace("\"en\"", "\"tagName\"");

            responseStream.close();
            connection.disconnect();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            logger.error("API request failed with response code: " + responseCode, e);
            throw new ApiRequestException("API請求錯誤", responseCode);
        }
        // json轉換成java物件
        ImageGetTagsApiResponseDTO result =
                jsonConverter.convertJsonToObj(response, ImageGetTagsApiResponseDTO.class);
        result.getResult().setImageName(imageName);

        // 印出回傳log
        logger.info(result.toString());

        return result;
    }
}
