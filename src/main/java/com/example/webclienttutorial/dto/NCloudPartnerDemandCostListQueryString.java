package com.example.webclienttutorial.dto;

import com.example.webclienttutorial.client.CloudQueryString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class NCloudPartnerDemandCostListQueryString implements CloudQueryString {

    private Integer pageNo;
    private Integer pageSize;
    private String loginId;
    private String startMonth;
    private String endMonth;
    private String responseFormatType = "json";

    @Builder
    public NCloudPartnerDemandCostListQueryString(Integer pageNo, Integer pageSize, String loginId, String startMonth, String endMonth) {

        if(Objects.isNull(startMonth) && Objects.isNull(endMonth) ){
            throw new IllegalArgumentException("startMonth and endMonth is required");
        }

        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.loginId = loginId;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

    @Builder
    public NCloudPartnerDemandCostListQueryString(Integer pageNo, Integer pageSize, String loginId, String startMonth, String endMonth, String responseFormatType) {

        if(Objects.isNull(startMonth) && Objects.isNull(endMonth) ){
            throw new IllegalArgumentException("startMonth and endMonth is required");
        }

        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.loginId = loginId;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.responseFormatType = responseFormatType;
    }
}
