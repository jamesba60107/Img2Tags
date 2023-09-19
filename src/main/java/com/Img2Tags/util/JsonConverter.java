package com.Img2Tags.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class JsonConverter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper mapper;

    @Autowired
    public JsonConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }



    public <T> T convertJsonToObj(String data, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            logger.error("convertJsonToObj失敗: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
