package com.farmguardian.farmguardian.exception.device;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class DeviceAlreadyConnectedException extends BusinessException {
    public DeviceAlreadyConnectedException() {
        super(ErrorCode.DEVICE_ALREADY_CONNECTED);
    }
}