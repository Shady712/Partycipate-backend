package com.sasd.eventor.model.entities;

import lombok.Data;

import javax.persistence.*;
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
}
