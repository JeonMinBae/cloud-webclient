package com.example.webclienttutorial.dto;

import com.example.webclienttutorial.client.CloudQueryString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NCloudContractUsageListQueryString implements CloudQueryString {

    private Integer pageNo;
    private Integer pageSize;
    private String loginId;
    private String startMonth;
    private String endMonth;
    private Boolean isOrganization;
    private Boolean isPartner;
    private String contractNo;
    private String contractTypeCode;
    private String productItemKindCode;
    private String regionCode;
    @Builder.Default
    private String responseFormatType = "json";

}
