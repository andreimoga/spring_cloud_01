package org.example.eurekaserver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class EurekaDiscoveryClientRestController {

    private final DiscoveryClient discoveryClient;

    @GetMapping("/services")
    public List<String> getAllServicesName() {
        List<String> services = discoveryClient.getServices(); //0
        log.info("Services {}", services);
        return services;
    }

}
