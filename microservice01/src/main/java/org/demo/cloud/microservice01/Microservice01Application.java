package org.demo.cloud.microservice01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableReactiveFeignClients
public class Microservice01Application {

    public static void main(String[] args) {
        SpringApplication.run(Microservice01Application.class, args);
    }
}
