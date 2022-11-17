package com.example.webclienttutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDemandCostListJSON {

    private PartnerDemandCostListResponse getPartnerDemandCostListResponse;

    public String getTotalRows() {
        return getPartnerDemandCostListResponse.getTotalRows();
    }

    public List<PartnerDemandCost> getPartnerDemandCostList(){
        return getPartnerDemandCostListResponse.getPartnerDemandCostList();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerDemandCostListResponse extends CommonResponse{

        private String totalRows;

        private List<PartnerDemandCost> partnerDemandCostList = new ArrayList<>();

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerDemandCost {

        private String loginId;

        private String demandMonth;

        private Long useAmount;

        private Long promiseDiscountAmount;

        private Long promotionDiscountAmount;

        private Long etcDiscountAmount;

        private Long memberPromiseDiscountAddAmount;

        private Long memberPriceDiscountAmount;

        private Long productDiscountAmount;

        private Long creditDiscountAmount;

        private Long customerDiscountAmount;

        private Long under100DiscountAmount;

        private Long under1000DiscountAmount;

        private Long coinUseAmount;

        private Long defaultAmount;

        private Long totalDemandAmount;

        private Long totalMarginExceptProdcutUseAmount;

        private Long totalExceptProductMarginAmount;

        private Float marginRatio ;

        private Long marginAmount;

        private Long marginDiscountAmount;

        private Long incentiveAmount;

        private Long partnerDiscountAmount;

        private Long partnerTotalDemandAmount;

        private Long partnerTotalDemandVatAmount;

        private Long partnerTotalDemandAmountIncludingVat;

        private Date writeDate;

    }
}
