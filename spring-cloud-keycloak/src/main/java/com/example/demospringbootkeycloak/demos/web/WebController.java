package com.example.demospringbootkeycloak.demos.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/web")
public class WebController {
    //http://192.168.0.11:18002/service-auth/web/index
    @ResponseBody
    @GetMapping(path = "/index")
    @PreAuthorize("hasRole('user-role1')")
    public String index() {
        return "index";
    }

    // http://192.168.0.11:18002/service-auth/web/logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

    // http://192.168.0.11:18002/service-auth/web/customers
    @GetMapping(path = "/customers")
    @ResponseBody
    public String customers() {
        return "customers";
    }
}
