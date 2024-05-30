package com.example.demospringbootkeycloak.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Autowired
    private OAuth2TokenService oAuth2TokenService;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = oAuth2TokenService.getAccessTokenForClientCredentials();
            System.out.println();
            System.out.println("Authorization:"+ "Bearer " + token);
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
