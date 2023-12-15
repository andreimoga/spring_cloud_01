package org.demo.cloud.microservice01;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/ms1")
public class RestControllerUsingReactiveFeignForMicroService2AndRedirectThroughAGatewayService {

    private final MicroService02Client microService02Client;
    private final DiscoveryClient discoveryClient;

//    private final io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreakerService;
//    private final io.github.resilience4j.retry.Retry retryService;
//    private final io.github.resilience4j.timelimiter.TimeLimiter timeLimiterService;

    //not the ok in reactive programming but it works
    @GetMapping(path = "/services_list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getDemoDataAsList() {
        List<String> services = discoveryClient.getServices();
        log.info("current registered services are: {}", services);
        return services;
    }

    @GetMapping(path = "/data_flux_remote", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Retry(name = "microservice02_call", fallbackMethod = "callMicroservice2ErrorHandler")
    public Flux<String> getDemoDataAsFlux() {
        return microService02Client.getRemoteData()
                .map(value -> "µ-service-1 at localtime: " + LocalTime.now() +
                        " receiving live data from µ-service-2 -> (" + value + ")");
    }

    @GetMapping(path = "/stream_flux_local", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "µ-service-1 localtime: " + LocalTime.now());
        //.take(15);
    }

    @GetMapping(path = "/stream_flux_remote", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Retry(name = "microservice02_call", fallbackMethod = "callMicroservice2ErrorHandler")
    /*
        works ok if multiple instances are started and one of them goes down and sync with Eureka is done for all instances
        data:µ-service-1 at localtime: 13:11:19.289015500 receiving a flux of liva data from µ-service-2 ->
                (µ-service-2 instanceId 'inst_nr_01' has localtime - 13:11:19.287028700)

        data:µ-service-1 at localtime: 13:11:21.369707500 receiving a flux of liva data from µ-service-2 ->
                (µ-service-2 instanceId 'inst_default' has localtime - 13:11:21.362705300)

        if sync is not fully made an error will occur
            data:µ-service-1 received and error while calling µ-service-2
            [500 Internal Server Error] during [GET] to [http://ANDREW-BIG-PC:9333/ms2/stream_flux]
            [MicroService02Client#getRemoteStreamFlux()]:
            [{"timestamp":"2023-12-12T11:13:09.737+00:00","path":"/ms2/stream_flux","status":500,
            "error":"Internal Server Error","requestId":"06e53306-6"}]
    */
    public Flux<String> streamFluxRemote() {
        return microService02Client.getRemoteStreamFlux()
                .map(value -> "µ-service-1 at localtime: " + LocalTime.now() +
                        " receiving a flux of liva data from µ-service-2 -> (" + value + ")");
    }

    public Flux<String> callMicroservice2ErrorHandler(Exception exception) {
        log.error("Error calling ", exception);

        Throwable exc = exception;
        if (exception.getCause() != null) {
            exc = exception.getCause();
        }

        return Flux.just("µ-service-1 received and error while calling µ-service-2 " + exc.getMessage());
    }
}
