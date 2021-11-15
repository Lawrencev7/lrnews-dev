package com.lrnews.exception;

import com.lrnews.graceresult.ResponseStatusEnum;

public class LrCustomException extends RuntimeException{
    private ResponseStatusEnum status;

    public LrCustomException(ResponseStatusEnum status) {
        super("Exception Code: " + status.status() + " - Message: " + status.msg());
        this.status = status;
    }

    public ResponseStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ResponseStatusEnum status) {
        this.status = status;
    }
}
