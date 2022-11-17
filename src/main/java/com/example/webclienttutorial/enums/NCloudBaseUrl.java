package com.example.webclienttutorial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NCloudBaseUrl {
    BILLING_URL("https://billingapi.apigw.ntruss.com"),
    RESOURCE_URL("https://resourcemanager.apigw.ntruss.com"),
    ;

    private final String url;
}
