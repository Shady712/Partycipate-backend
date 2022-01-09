package com.sasd.eventor.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
    private String name;
}