package com.example.webclienttutorial.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractProduct {

    private String contractProductSequence;
    private String beforeContractProductSequence;
    private String productCode;
    private String priceNo;
    private String instanceNo;
    private CommonCode productItemKind;
    private CommonCode productRatingType;
    private CommonCode serviceStatus;
    private Date serviceStartDate;
    private Date serviceEndDate;
    private Long productSize;
    private Integer productCount;
    private CommonCode productSizeType;
    private List<Usage> usageList;
}
