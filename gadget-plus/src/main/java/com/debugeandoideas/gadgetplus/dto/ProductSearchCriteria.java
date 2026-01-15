package com.debugeandoideas.gadgetplus.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductSearchCriteria {

    private String brand;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minRating;
    private Boolean hasDiscount;
    private String categoryCode;
    private LocalDate launchedAfter;

}
