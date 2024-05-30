package com.example.demospringbootkeycloak.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service-b", configuration = FeignClientConfig.class)
public interface BFeignClientsService {

    @GetMapping("/api/test-service-b")
    String test1();

    @GetMapping("/test-service-b/customers")
    String customers();
}
