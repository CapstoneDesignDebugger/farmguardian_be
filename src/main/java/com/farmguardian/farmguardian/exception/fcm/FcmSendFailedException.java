package com.farmguardian.farmguardian.exception.fcm;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class FcmSendFailedException extends BusinessException {
    public FcmSendFailedException() {
        super(ErrorCode.FCM_SEND_FAILED);
    }
}