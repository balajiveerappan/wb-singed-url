package com.wb.singedurl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sanrawat on 2/15/2017.
 */

@RestController
public class HomeController {

    @GetMapping(path = "/")
    public String helloFromSignedUrl(){
        return "Signed URL Micro-Service - " +
                "Please send a Get request with Authorization Bearer token to " +
                "\"http://ip-address:port/protected/pre-singed-url\"";
    }

}
