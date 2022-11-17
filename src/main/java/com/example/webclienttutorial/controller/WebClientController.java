package com.example.webclienttutorial.controller;

import com.example.webclienttutorial.client.CloudCredential;
import com.example.webclienttutorial.client.MakeSignature;
import com.example.webclienttutorial.client.NCloudClient;
import com.example.webclienttutorial.dto.ContractUsageList;
import com.example.webclienttutorial.dto.NCloudContractUsageListQueryString;
import com.example.webclienttutorial.dto.NCloudPartnerDemandCostListQueryString;
import com.example.webclienttutorial.dto.PartnerDemandCostListJSON;
import com.example.webclienttutorial.util.OpenApiUtil;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class WebClientController {

    private final NCloudClient nCloudApi;



    @GetMapping("/test")
    public String test() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

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

        WebClient webClient = WebClient.builder()
            .baseUrl("https://billingapi.apigw.ntruss.com")
//            .baseUrl("http://localhost:8090")
            .exchangeStrategies(exchangeStrategies)
            .clientConnector(connector)
            .build();

        MakeSignature ms = new MakeSignature();
        ms.setAccessKey("access");
        ms.setSecretKey("secret");


        NCloudPartnerDemandCostListQueryString queryString = NCloudPartnerDemandCostListQueryString.builder()
            .startMonth("202206")
            .endMonth("202206")
            .build();


        String uri = OpenApiUtil.getOpenApiUrl("/billing/v1/cost/getPartnerDemandCostList", queryString);
        System.out.println("uri = " + uri);

        HttpHeaders nCloudHttpHeaders = ms.getNCloudHttpHeaders(HttpMethod.GET, uri);


        Mono<ResponseEntity<PartnerDemandCostListJSON>> entityMono = webClient
            .get()
            .uri(uri)
            .headers(httpHeaders ->
                nCloudHttpHeaders.forEach((key, values) -> httpHeaders.addAll(key, values))
            )
            .retrieve()
            .onStatus(HttpStatus::is2xxSuccessful, response -> {
                System.out.println("2xx resopnse" + response);
                return null;
            })
//            .onStatus(HttpStatus::is3xxRedirection, response -> {
//                System.out.println("3xx resopnse" + response);
//                System.out.println("response.rawStatusCode() = " + response.rawStatusCode());
//                return response.bodyToMono(RuntimeException.class);
//            })
//            .onStatus(HttpStatus::is4xxClientError, response -> {
//                System.out.println("4xx resopnse" + response);
//                return null;
//            })
            .toEntity(PartnerDemandCostListJSON.class)
//            .bodyToMono(PartnerDemandCostListJSON.class)
            .log("ncloud");

        ResponseEntity<PartnerDemandCostListJSON> response = entityMono.block();
        System.out.println("response.hasBody() = " + response.hasBody());
        System.out.println("response.getBody() = " + response.getBody());
        System.out.println("response.getBody().getPartnerDemandCostList().get(0).getLoginId() = " + response.getBody().getPartnerDemandCostList().get(0).getLoginId());
        System.out.println("response.getHeaders() = " + response.getHeaders());
        System.out.println("response.getStatusCodeValue() = " + response.getStatusCodeValue());


        return "test Ok";

    }


    @GetMapping("/test2")
    public ResponseEntity<?> test2() {

        NCloudPartnerDemandCostListQueryString queryString = NCloudPartnerDemandCostListQueryString.builder()
            .startMonth("202206")
            .endMonth("202206")
            .build();

        ;

        CloudCredential credential = CloudCredential.builder()
            .accessKey("accessKey")
            .secretKey("secretKey")
            .build();

        ResponseEntity<PartnerDemandCostListJSON> responseEntity = nCloudApi.getPartnerDemandCostList(credential, queryString);
        List<PartnerDemandCostListJSON.PartnerDemandCost> partnerDemandCostList = responseEntity.getBody().getPartnerDemandCostList();
        partnerDemandCostList.stream().forEach(cost -> System.out.println("cost = " + cost));


        return ResponseEntity.ok(partnerDemandCostList);

    }

    @GetMapping("/test3")
    public ResponseEntity<?> test3() {

        NCloudContractUsageListQueryString queryString = NCloudContractUsageListQueryString.builder()
            .startMonth("202206")
            .endMonth("202206")
            .pageSize(10)
            .build();

        CloudCredential credential = CloudCredential.builder()
            .accessKey("accessKey")
            .secretKey("secretKey")
            .build();

        ResponseEntity<ContractUsageList> response = nCloudApi.getContractUsageList(credential, queryString);


        return response;
    }

    @GetMapping("/test4")
    public ResponseEntity<?> test4() {

        NCloudPartnerDemandCostListQueryString queryString = NCloudPartnerDemandCostListQueryString.builder()
            .startMonth("202206")
            .endMonth("202206")
            .build();


        CloudCredential credential = CloudCredential.builder()
            .accessKey("accessKey")
            .secretKey("secretKey")
            .build();

        ResponseEntity<PartnerDemandCostListJSON> responseEntity = nCloudApi.getPartnerDemandCostList(credential, queryString);
        List<PartnerDemandCostListJSON.PartnerDemandCost> partnerDemandCostList = responseEntity.getBody().getPartnerDemandCostList();
        partnerDemandCostList.stream().forEach(cost -> {
            System.out.println("cost = " + cost);
        });


        return ResponseEntity.ok(partnerDemandCostList);

    }

    @GetMapping("/test5")
    public ResponseEntity<?> test5() {

        CloudCredential credential = CloudCredential.builder()
            .accessKey("accessKey")
            .secretKey("secretKey")
            .build();

        nCloudApi.getResource(credential, null);

        return ResponseEntity.ok("213");

    }



}
