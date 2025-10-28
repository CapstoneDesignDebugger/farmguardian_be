package com.farmguardian.farmguardian.config;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient fastApiRestClient(
            @Value("${fastapi.base-url}") String baseUrl,
            @Value("${fastapi.timeout.connect}") int connectTimeout,
            @Value("${fastapi.timeout.read}") int readTimeout) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(clientHttpRequestFactory(connectTimeout, readTimeout))
                .build();
    }

    /*
    private ClientHttpRequestFactory clientHttpRequestFactory(int connectTimeout, int readTimeout) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
     */

    private ClientHttpRequestFactory clientHttpRequestFactory(int connectTimeout, int readTimeout) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(connectTimeout))  // 연결 타임아웃을 5초로 설정
                .withReadTimeout(Duration.ofSeconds(readTimeout)); // 읽기 타임아웃을 5초로 설정
        return ClientHttpRequestFactories.get(settings);
    }
}

