package com.farmguardian.farmguardian.exception.auth;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class ClientUuidMismatchException extends BusinessException {
    public ClientUuidMismatchException() {
        super(ErrorCode.CLIENT_UUID_MISMATCH);
    }
}