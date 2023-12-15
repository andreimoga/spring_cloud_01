package org.demo.cloud.microservice01;

import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

//using Eureka and Feign
//@FeignClient("MICROSERVICE02")
// using Eureka, Feign and Gateway (spring.application.name: "gatewayservice")
//@FeignClient("GATEWAYSERVICE")
//@ReactiveFeignClient(name = "MICROSERVICE02", configuration = ReactiveFeignClientConfig.class)
//@ReactiveFeignClient(name = "MICROSERVICE02")
@ReactiveFeignClient(name = "GATEWAYSERVICE")
public interface MicroService02Client {

    static final String MICROSERVICE2_START_PATH = "/ms2";

    //doesn't returns reactor.core.publisher.Mono or reactor.core.publisher.Flux
    //@GetMapping(MICROSERVICE2_DATA_CONTROLLER_MAPPING_START_PATH + "/data_list")
    //List<String> getDemoDataAsList();

    @GetMapping(MICROSERVICE2_START_PATH + "/data_flux")
    Flux<String> getRemoteData();

    @GetMapping(MICROSERVICE2_START_PATH + "/stream_flux")
    Flux<String> getRemoteStreamFlux();
}
