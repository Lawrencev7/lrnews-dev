package com.lrnews.exception;

import com.lrnews.graceresult.JsonResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Exception -> Json result
 */
public class CustomExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(LrCustomException.class)
    @ResponseBody
    public JsonResultObject getExceptionMsg(LrCustomException e){
        logger.error("Catch exception: " + e.getMessage());
        return JsonResultObject.exception(e.getStatus());
    }
}
