package com.partycipate.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_seq")
    @SequenceGenerator(name = "events_seq", sequenceName = "events_seq")
    private Long id;
    private String name;
    private LocalDateTime date;
    private String location;
    private String description;
    private Integer price;
    @ManyToOne
    private User creator;
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "events_users",
            joinColumns = { @JoinColumn(name = "event_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @EqualsAndHashCode.Exclude
    private List<User> guests;
    private BigDecimal lat;
    private BigDecimal lng;
    @URL
    @Pattern(regexp = "^https://t.me/joinchat/[^/]+")
    private String telegramUrl;
}
