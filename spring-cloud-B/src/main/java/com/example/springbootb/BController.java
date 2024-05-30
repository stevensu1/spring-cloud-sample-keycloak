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

package com.example.springbootb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BController {

    //http://192.168.0.11:18002/service-b/api/test-service-b
    @GetMapping("/api/test-service-b")
    //@PreAuthorize("hasRole('user-role1')")
    public String test1() {

        return "This is service B.api";
    }

    //http://192.168.0.11:18002/service-b/test-service-b
    @GetMapping("/test-service-b")
    public String test() {

        return "This is service B.";
    }

    @GetMapping("/test-service-b/customers")
    public String customers() {

        return "This is service B.customers";

    }

}
