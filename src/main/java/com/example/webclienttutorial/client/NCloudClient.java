package com.example.webclienttutorial.client;


import com.example.webclienttutorial.dto.ContractUsageList;
import com.example.webclienttutorial.dto.NCloudPartnerDemandCostListQueryString;
import com.example.webclienttutorial.dto.PartnerDemandCostListJSON;
import com.example.webclienttutorial.enums.NCloudBaseUrl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NCloudClient extends BaseNCloudClient {
    public NCloudClient(WebClient webClient, MakeSignature makeSignature) {
        super(webClient, makeSignature);
    }


    public ResponseEntity<PartnerDemandCostListJSON> getPartnerDemandCostList(CloudCredential credential, CloudQueryString queryString) {

        return baseUrl(NCloudBaseUrl.BILLING_URL.getUrl())
            .credential(credential)
            .get()
            .uri("/billing/v1/cost/getPartnerDemandCostList")
            .queryString(queryString)
            .retrieve()
            .on4xxStatus(clientResponse -> {
                System.out.println("clientResponse = " + clientResponse.bodyToMono(String.class));
                return null;
            })
            .request(PartnerDemandCostListJSON.class)
            .block();
    }

    public ResponseEntity<ContractUsageList> getContractUsageList(CloudCredential credential, CloudQueryString queryString) {

        ResponseEntity<String> response = baseUrl(NCloudBaseUrl.BILLING_URL.getUrl())
            .credential(credential)
            .get()
            .uri("/billing/v1/cost/getContractUsageList")
            .queryString(queryString)
            .retrieve()
            .request(String.class)
            .block();
        System.out.println("response = " + response);

        return baseUrl(NCloudBaseUrl.BILLING_URL.getUrl())
            .credential(credential)
            .get()
            .uri("/billing/v1/cost/getContractUsageList")
            .queryString(queryString)
            .retrieve()
            .request(ContractUsageList.class)
            .block();
    }

    public void getResource(CloudCredential credential, CloudQueryString queryString) {

        ResponseEntity<String> block = baseUrl(NCloudBaseUrl.RESOURCE_URL.getUrl())
            .credential(credential)
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new Object())
            .uri("/api/v1/resources")
            .queryString(queryString)
//            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .request(String.class)
            .log("resource")
            .block();

        System.out.println("block.getBody() = " + block.getBody());

    }


}
