package com.farmguardian.farmguardian.exception.fcm;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class FcmTokenNotFoundException extends BusinessException {
    public FcmTokenNotFoundException() {
        super(ErrorCode.FCM_TOKEN_NOT_FOUND);
    }
}