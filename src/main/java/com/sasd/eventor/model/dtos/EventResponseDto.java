package com.sasd.eventor.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class EventResponseDto {
    private Long id;
    private String name;
    private LocalDateTime date;
    private String location;
    private String description;
    private Integer price;
    private UserResponseDto creator;
    private List<String> guests;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventResponseDto that = (EventResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date)
                && Objects.equals(location, that.location) && Objects.equals(description, that.description)
                && Objects.equals(price, that.price) && Objects.equals(creator.getId(), that.creator.getId())
                && Objects.equals(guests, that.guests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, location, description, price, creator.getId(), guests);
    }
}
