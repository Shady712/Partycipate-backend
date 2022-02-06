package com.partycipate.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
public class EventCoordinates {
    @Digits(integer = 2, fraction = 12)
    @DecimalMin("-90")
    @DecimalMax("+90")
    private BigDecimal lat;
    @Digits(integer = 3, fraction = 12)
    @DecimalMin("-180")
    @DecimalMax("+180")
    private BigDecimal lng;
}
