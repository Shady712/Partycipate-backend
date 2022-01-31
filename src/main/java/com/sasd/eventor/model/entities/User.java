package com.sasd.eventor.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq")
    private Long id;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String login;
    @NotNull
    private String passwordHash;
    @Email
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    @NotEmpty
    private String name;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "first_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "second_user_id")}
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> friends;

    public String getSalt() {
        return passwordHash.substring(128);
    }
}
