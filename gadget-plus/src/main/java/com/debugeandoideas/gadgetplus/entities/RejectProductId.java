package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RejectProductId implements Serializable {

    private String productName;
    private String brandName;

}
