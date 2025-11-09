package com.farmguardian.farmguardian.exception.image;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class FastApiCallFailedException extends BusinessException {
    public FastApiCallFailedException() {
        super(ErrorCode.FASTAPI_CALL_FAILED);
    }

    public FastApiCallFailedException(String customMessage) {
        super(ErrorCode.FASTAPI_CALL_FAILED, customMessage);
    }
}