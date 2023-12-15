package org.demo.cloud.gatewayserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class GatewayFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Custom demo filter 'GatewayFilter' PRE-processing attributes: {}, request: {}, chain: {}",
                exchange.getAttributes(), exchange.getRequest(), chain);

        return chain.filter(exchange).then(Mono.fromRunnable(() ->
                log.info("Custom demo filter 'GatewayFilter' POST-processing attributes: {}, response: {}, chain: {}",
                        exchange.getAttributes(), exchange.getResponse(), chain)));
    }
}
