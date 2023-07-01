package ru.clevertec.news_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NewsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsSystemApplication.class, args);
    }
}
