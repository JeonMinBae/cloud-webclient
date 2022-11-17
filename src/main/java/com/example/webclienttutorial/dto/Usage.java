package com.example.webclienttutorial.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usage {

    private CommonCode meteringType;
    private String useMonth;
    private BigDecimal usageQuantity;
    private CommonCode unit;
}
