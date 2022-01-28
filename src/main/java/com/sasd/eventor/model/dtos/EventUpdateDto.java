package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventUpdateDto {
    @NotNull
    private Long id;
    @NotNull
    @NotEmpty
    @Size(min = 4, max = 100)
    private String name;
    private LocalDateTime date;
    @Size(max = 2050)
    private String location;
    @Size(max = 65000)
    @Lob
    private String description;
    @Min(value = 0)
    private Integer price;
    @NotNull
    private String jwt;
    @Digits(integer = 2, fraction = 12)
    @DecimalMin("-90")
    @DecimalMax("+90")
    private BigDecimal latitude;
    @Digits(integer = 3, fraction = 12)
    @DecimalMin("-180")
    @DecimalMax("+180")
    private BigDecimal longitude;
}
