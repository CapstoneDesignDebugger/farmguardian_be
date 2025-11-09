package com.farmguardian.farmguardian.exception.image;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class ImageNotFoundException extends BusinessException {
    public ImageNotFoundException() {
        super(ErrorCode.IMAGE_NOT_FOUND);
    }
}