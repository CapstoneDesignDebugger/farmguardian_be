package com.farmguardian.farmguardian.exception.device;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class DeviceNotConnectedException extends BusinessException {
    public DeviceNotConnectedException() {
        super(ErrorCode.DEVICE_NOT_CONNECTED);
    }
}