package com.Img2Tags.util;

import com.Img2Tags.dto.ImageGetTagsApiResponseDTO;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

@Component
public class CSVFactory {
    public String generateCSVFromDTO(List<ImageGetTagsApiResponseDTO> dataList) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // 寫入 CSV 標頭
        writer.write("圖片編號");
        for (int i = 1; i <= 99; i++) {
            writer.write("\t標籤" + i + "\t分數" + i);
        }
        writer.write("\r\n");

        // 處理每一筆數據
        for (ImageGetTagsApiResponseDTO data : dataList) {
            writer.write(data.getResult().getImageName());
            for (int i = 0; i < 99; i++) {
                if (i < data.getResult().getTags().size()) {
                    ImageGetTagsApiResponseDTO.Tag tag = data.getResult().getTags().get(i);
                    double confidence = tag.getConfidence();
                    String formattedConfidence = decimalFormat.format(confidence);
                    writer.write("\t" + tag.getTag().getEn() + "\t" + formattedConfidence);
                } else {
                    writer.write("\t\t");  // 如果沒有資料，則留空
                }
            }
            writer.write("\r\n");
        }

        return stringWriter.toString();
    }
}
