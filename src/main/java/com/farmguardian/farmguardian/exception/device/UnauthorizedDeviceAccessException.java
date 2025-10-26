package com.farmguardian.farmguardian.exception.device;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class UnauthorizedDeviceAccessException extends BusinessException {
    public UnauthorizedDeviceAccessException() {
        super(ErrorCode.UNAUTHORIZED_DEVICE_ACCESS);
    }
}