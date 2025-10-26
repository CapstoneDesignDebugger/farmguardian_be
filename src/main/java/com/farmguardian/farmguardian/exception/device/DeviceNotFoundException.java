package com.farmguardian.farmguardian.exception.device;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class DeviceNotFoundException extends BusinessException {
    public DeviceNotFoundException() {
        super(ErrorCode.DEVICE_NOT_FOUND);
    }
}