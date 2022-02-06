package com.partycipate.model.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    private EventCoordinates coordinates = new EventCoordinates();
    @URL
    @Pattern(regexp = "^https://t.me/joinchat/[^/]+")
    private String telegramUrl;
}
