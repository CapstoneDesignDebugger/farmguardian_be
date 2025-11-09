package com.farmguardian.farmguardian.exception.mqtt;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class NightTimeCaptureForbiddenException extends BusinessException {
    public NightTimeCaptureForbiddenException() {
        super(ErrorCode.NIGHT_TIME_CAPTURE_NOT_ALLOWED);
    }
}