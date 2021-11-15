package com.lrnews.exception;

import com.lrnews.graceresult.ResponseStatusEnum;

public class CustomExceptionFactory {
    public static void onException(ResponseStatusEnum status) {
        throw new LrCustomException(status);
    }
}
