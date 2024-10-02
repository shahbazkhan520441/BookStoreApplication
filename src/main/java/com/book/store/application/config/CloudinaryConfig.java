package com.book.store.application.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.HashMap;

@Configuration
public class CloudinaryConfig {
    static{
        System.out.println("hello in cloud config");
    }

    @Value("${cloudinary.cloud.name}")
    private String cloudinaryCloudName;

    @Value("${cloudinary.api.key}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.api.secret}")
    private String cloudinaryApiSecret;

    @Bean
    Cloudinary cloudinary() {
        System.out.println("hello in cloud config");

        Map<String, String> config = new HashMap<String, String>();
        config.put("cloud_name", cloudinaryCloudName);
        config.put("api_key", cloudinaryApiKey);
        config.put("api_secret", cloudinaryApiSecret);
        System.out.println("hello out cloud config");
        return new Cloudinary(config);
    }
}