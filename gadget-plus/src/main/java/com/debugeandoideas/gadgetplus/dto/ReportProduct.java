package com.debugeandoideas.gadgetplus.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportProduct {

    private String brandName;
    private Double averagePrices;
    private BigDecimal sumPrices;

}
