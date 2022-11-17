package com.example.webclienttutorial.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractUsageList {

    private ContractUsageListResponse getContractUsageListResponse;

    private List<Contract> getContractUsageList(){
        return getContractUsageListResponse.getContractList();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractUsageListResponse extends CommonResponse {

        private Integer totalRows;
        private List<Contract> contractList = new ArrayList<>();

    }


}
