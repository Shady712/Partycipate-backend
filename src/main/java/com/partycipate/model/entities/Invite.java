package com.partycipate.model.entities;


import com.partycipate.model.enums.RequestStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "invites")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invites_seq")
    @SequenceGenerator(name = "invites_seq", sequenceName = "invites_seq")
    private Long id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
