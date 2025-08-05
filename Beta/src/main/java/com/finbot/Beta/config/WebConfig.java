//package com.finbot.Beta.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    public WebConfig() {
//        System.out.println(">>> WebConfig with CORS support is loaded");
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        System.out.println(">>> Configuring CORS mappings");
//        registry.addMapping("/**")
//                .allowedOrigins(
//                        "http://127.0.0.1:5500",
//                        "http://localhost:5500",
//                        "http://10.30.22.61:5500",
//                        "http://172.20.240.1:5500"
//                )
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//        System.out.println(">>> CORS mappings configured successfully");
//    }
//}