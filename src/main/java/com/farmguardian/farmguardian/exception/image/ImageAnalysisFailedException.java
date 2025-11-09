package com.farmguardian.farmguardian.exception.image;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class ImageAnalysisFailedException extends BusinessException {
    public ImageAnalysisFailedException() {
        super(ErrorCode.IMAGE_ANALYSIS_FAILED);
    }

    public ImageAnalysisFailedException(String customMessage) {
        super(ErrorCode.IMAGE_ANALYSIS_FAILED, customMessage);
    }
}