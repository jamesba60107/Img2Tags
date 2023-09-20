package com.Img2Tags.exception;

import com.Img2Tags.dto.ApiExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    private static final Map<Integer, String> errorCodeToMessage;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        errorCodeToMessage = new HashMap<>();
        errorCodeToMessage.put(400, "HttpStatus.BAD_REQUEST, 請求格式有誤。請確保您提供了所有必需的參數。");
        errorCodeToMessage.put(401, "HttpStatus.UNAUTHORIZED, 請求授權有問題。請確保您的授權標頭正確格式化，並包含您的api key和secret。");
        errorCodeToMessage.put(403, "HttpStatus.FORBIDDEN, 您已達到訂閱上限，因此暫時不允許執行此類請求。");
        errorCodeToMessage.put(404, "HttpStatus.NOT_FOUND, api端點不存在或您請求的資源不存在。");
        errorCodeToMessage.put(405, "HttpStatus.METHOD_NOT_ALLOWED, 請求的端點不支援HTTP方法。");
        errorCodeToMessage.put(406, "HttpStatus.NOT_ACCEPTABLE, 您請求的格式不是json。");
        errorCodeToMessage.put(410, "HttpStatus.GONE, 您請求的資源已不再可用。");
        errorCodeToMessage.put(429, "HttpStatus.TOO_MANY_REQUESTS, 您已達到當前訂閱方案的發送限制，請稍等一下再重新嘗試請求。");
        errorCodeToMessage.put(500, "HttpStatus.INTERNAL_SERVER_ERROR, Immage伺服器出現問題。請稍後再試或寫信給我們：api@imagga.com");
        errorCodeToMessage.put(503, "HttpStatus.SERVICE_UNAVAILABLE, Immage因維護暫時下線。請稍後再試。");
    }

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {
        int errorCode = ex.getErrorCode();
        String errorMessage = errorCodeToMessage.getOrDefault(errorCode, "未知的錯誤");

        LocalDateTime dateTime = LocalDateTime.now();
        String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(dateTime);
        ApiExceptionDTO apiException = new ApiExceptionDTO(
                errorCode,
                errorMessage,
                timeStamp
        );
        logger.error(apiException.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.valueOf(errorCode));
    }
}
