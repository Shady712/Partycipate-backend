package com.sasd.eventor.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
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
    private String password;
    @NotNull
    @NotEmpty
    private String name;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = { @JoinColumn(name = "first_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "second_user_id") }
    )
    private List<User> friends;
}
