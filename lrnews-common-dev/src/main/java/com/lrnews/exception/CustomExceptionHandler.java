package com.lrnews.exception;

import com.lrnews.graceresult.JsonResultObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception -> Json result
 */
@ControllerAdvice
public class CustomExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(LrCustomException.class)
    @ResponseBody
    public JsonResultObject getExceptionMsg(LrCustomException e) {
        logger.error("Catch exception: " + e.getMessage());
        return JsonResultObject.exception(e.getStatus());
    }

    /**
     * Use this exception handler to replace BindingResult in controller param.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonResultObject handleInvalidArgument(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = getErrors(bindingResult);
        return JsonResultObject.errorMap(errors);
    }

    /**
     * Unboxing errors in result when caught a MethodArgumentNotValidException
     *
     * @param result BindingResult from http request
     */
    @NotNull
    private Map<String, String> getErrors(@NotNull BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        result.getFieldErrors().forEach(e -> errorMap.put(e.getField(), e.getDefaultMessage()));
        return errorMap;
    }
}
