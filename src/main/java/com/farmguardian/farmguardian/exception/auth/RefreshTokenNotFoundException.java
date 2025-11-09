package com.farmguardian.farmguardian.exception.auth;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException {
    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}