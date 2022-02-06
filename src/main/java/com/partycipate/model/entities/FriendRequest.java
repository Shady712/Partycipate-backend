package com.partycipate.model.entities;

import com.partycipate.model.enums.RequestStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_requests_seq")
    @SequenceGenerator(name = "friend_requests_seq", sequenceName = "friend_requests_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
