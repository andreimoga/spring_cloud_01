package org.demo.cloud.gatewayserver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;


@AllArgsConstructor
@Slf4j
@RestController
public class ServicesRestController {

    private final DiscoveryClient discoveryClient;

    @GetMapping(value = "/services", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getAllRegisteredServices() {
        List<String> services = discoveryClient.getServices();
        log.info("current register services {}", services);
        return Flux.fromIterable(services);
    }
}
