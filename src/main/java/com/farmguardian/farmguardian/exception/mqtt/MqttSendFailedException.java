package com.farmguardian.farmguardian.exception.mqtt;

import com.farmguardian.farmguardian.exception.BusinessException;
import com.farmguardian.farmguardian.exception.ErrorCode;

public class MqttSendFailedException extends BusinessException {
    public MqttSendFailedException() {
        super(ErrorCode.MQTT_SEND_FAILED);
    }

    public MqttSendFailedException(String customMessage) {
        super(ErrorCode.MQTT_SEND_FAILED, customMessage);
    }
}