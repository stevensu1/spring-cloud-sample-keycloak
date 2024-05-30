/*
 * Copyright 2013-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demospringbootkeycloak.feign;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("feign/")
public class FeignController {


    @Resource
    BFeignClientsService bFeignClientsService;
    //http://192.168.0.11:18002/service-auth/web/index
    //http://192.168.0.11:18002/service-auth/feign/service-b/test-service-b
    @GetMapping("/service-b/test-service-b")
    public String testb() {
        System.out.println(bFeignClientsService.test1());
        return "This is service B.";
    }
    //http://192.168.0.11:18002/service-auth/feign/service-b/test-service-b/customers
    @GetMapping("/service-b/test-service-b/customers")
    public String customersb() {
        System.out.println(bFeignClientsService.customers());
        return "This is service B.";

    }

}
