package org.demo.cloud.microservice02;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/ms2")
public class RestDataController {

    @Value("${app.instance.id}")
    private String instanceId;

    @GetMapping(path = "/data_flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getDemoDataAsFlux() {
        return Flux.fromIterable(Arrays.asList(
                "µ-service-2 instanceId '" + instanceId + "' - current value 123",
                "µ-service-2 instanceId '" + instanceId + "' - current value 456",
                "µ-service-2 instanceId '" + instanceId + "' - current value 789"));
    }

    @GetMapping(path = "/random_data_flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getRandomDataAsFlux() {
        return Flux.fromIterable(Arrays.asList(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()));
    }

    @GetMapping(path = "/data_list")
    public List<String> getDemoDataAsList() {
        return Arrays.asList("abc", "abd", "abe");
    }

    @GetMapping(path = "/random_data_list")
    public List<String> getRandomData() {
        return Arrays.asList(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    @GetMapping(path = "/stream_flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "µ-service-2 instanceId '" + instanceId
                        + "' has localtime - " + LocalTime.now());
                //.take(15);
    }
}
