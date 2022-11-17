package com.example.webclienttutorial.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30 * 1000) // Connection Timeout
            .doOnConnected(connection ->
                connection
                    .addHandlerLast(new ReadTimeoutHandler(10 * 1000, TimeUnit.MILLISECONDS)) // Read Timeout
                    .addHandlerLast(new WriteTimeoutHandler(10 * 1000, TimeUnit.MILLISECONDS))
            ); // Write Timeout

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        //Memory 조정: 2M (default 256KB)
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();


        return WebClient.builder()
            .baseUrl("http://localhost:8080/api")
//            .defaultHeader()
            .exchangeStrategies(exchangeStrategies)
            .clientConnector(connector)
            .build();
    }

}