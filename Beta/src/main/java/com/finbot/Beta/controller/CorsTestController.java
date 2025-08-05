package com.finbot.Beta.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class CorsTestController {

    @GetMapping("/cors")
    public Map<String, String> testCors() {
        System.out.println(">>> CORS TEST ENDPOINT HIT");
        return Map.of(
                "message", "CORS is working correctly!",
                "timestamp", String.valueOf(System.currentTimeMillis())
        );
    }
}