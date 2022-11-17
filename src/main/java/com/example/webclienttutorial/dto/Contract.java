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
public class Contract {
    private String loginId;
    private String contractNo;
    private String conjunctionContractNo;
    private CommonCode contractType;
    private CommonCode contractStatus;
    private Date contractStartDate;
    private Date contractEndDate;
    private String instanceName;
    private String regionCode;
    private CommonCode platformType;
    private List<ContractProduct> contractProductList;
}
