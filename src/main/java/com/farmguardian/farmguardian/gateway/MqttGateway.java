package com.farmguardian.farmguardian.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    /**
     * MQTT 메시지 발행
     * @param topic 발행할 토픽
     * @param payload 발행할 메시지 내용
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Payload String payload);
}