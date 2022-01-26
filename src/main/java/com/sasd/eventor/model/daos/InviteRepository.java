package com.sasd.eventor.model.daos;

import com.sasd.eventor.model.entities.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    List<Invite> findAllByReceiverId(Long receiverId);
    List<Invite> findAllByEventId(Long eventId);
}
