package com.example.webclienttutorial.entity;


import com.example.webclienttutorial.dto.PartnerDemandCostListJSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Entity
@Table(name = "PARTNER_DEMAND_COST")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDemandCost {

    @Id
    @GeneratedValue
    @Column(name = "partner_demand_cost_index")
    private Long index;
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
    private Float marginRatio;
    private Long marginAmount;
    private Long marginDiscountAmount;
    private Long incentiveAmount;
    private Long partnerDiscountAmount;
    private Long partnerTotalDemandAmount;
    private Long partnerTotalDemandVatAmount;
    private Long partnerTotalDemandAmountIncludingVat;
    private Date writeDate;


    public static PartnerDemandCost from (PartnerDemandCostListJSON.PartnerDemandCost cost){

        return PartnerDemandCost.builder()
        .loginId(cost.getLoginId())
        .demandMonth(cost.getDemandMonth())
        .useAmount(cost.getUseAmount())
        .promiseDiscountAmount(cost.getPromiseDiscountAmount())
        .promotionDiscountAmount(cost.getPromotionDiscountAmount())
        .etcDiscountAmount(cost.getEtcDiscountAmount())
        .memberPromiseDiscountAddAmount(cost.getMemberPromiseDiscountAddAmount())
        .memberPriceDiscountAmount(cost.getMemberPriceDiscountAmount())
        .productDiscountAmount(cost.getProductDiscountAmount())
        .creditDiscountAmount(cost.getCreditDiscountAmount())
        .customerDiscountAmount(cost.getCustomerDiscountAmount())
        .under100DiscountAmount(cost.getUnder100DiscountAmount())
        .under1000DiscountAmount(cost.getUnder1000DiscountAmount())
        .coinUseAmount(cost.getCoinUseAmount())
        .defaultAmount(cost.getDefaultAmount())
        .totalDemandAmount(cost.getTotalDemandAmount())
        .totalMarginExceptProdcutUseAmount(cost.getTotalMarginExceptProdcutUseAmount())
        .totalExceptProductMarginAmount(cost.getTotalExceptProductMarginAmount())
        .marginRatio(cost.getMarginRatio())
        .marginAmount(cost.getMarginAmount())
        .marginDiscountAmount(cost.getMarginDiscountAmount())
        .incentiveAmount(cost.getIncentiveAmount())
        .partnerDiscountAmount(cost.getPartnerDiscountAmount())
        .partnerTotalDemandAmount(cost.getPartnerTotalDemandAmount())
        .partnerTotalDemandVatAmount(cost.getPartnerTotalDemandVatAmount())
        .partnerTotalDemandAmountIncludingVat(cost.getPartnerTotalDemandAmountIncludingVat())
        .writeDate(cost.getWriteDate())
        .build();

    }

}
