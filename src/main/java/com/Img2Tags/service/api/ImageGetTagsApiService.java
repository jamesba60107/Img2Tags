package com.Img2Tags.service.api;

import com.Img2Tags.config.ImaggaConfig;
import com.Img2Tags.dto.ImageGetTagsApiResponseDTO;
import com.Img2Tags.exception.ApiRequestException;
import com.Img2Tags.util.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class ImageGetTagsApiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定義 HTTP 請求
    private static final String BOUNDARY = "Image Upload";
    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String TAGS_ENDPOINT = "/tags";
    private final ImaggaConfig imaggaConfig;
    private final JsonConverter jsonConverter;

    public int errorCode = 0;


    @Autowired
    public ImageGetTagsApiService(ImaggaConfig imaggaConfig, JsonConverter jsonConverter) {
        this.imaggaConfig = imaggaConfig;
        this.jsonConverter = jsonConverter;
    }

    public ImageGetTagsApiResponseDTO tags(String imageName) throws ApiRequestException {
            String basicAuth = imaggaConfig.getAuthorization();

            // 上傳圖檔的資料夾路徑 要改
            File fileToUpload = new File("/Users/bajunsheng/project/DJ_project/UploadFile/" + imageName);

            String endpoint = "/tags";
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

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            System.out.println(response);

            responseStream.close();
            connection.disconnect();

        } catch (IOException e) {
                throw new ApiRequestException("API請求錯誤", responseCode);
        }
            // json轉換成java物件
            ImageGetTagsApiResponseDTO result =
                    jsonConverter.convertJsonToObj(stringBuilder.toString(), ImageGetTagsApiResponseDTO.class);
            result.getResult().setImageName(imageName);

            // 印出回傳log
            logger.info(result.toString());

            return result;

    }
}
