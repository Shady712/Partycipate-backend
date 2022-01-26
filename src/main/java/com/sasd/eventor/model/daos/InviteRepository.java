package com.sasd.eventor.model.daos;

import com.sasd.eventor.model.entities.Event;
import com.sasd.eventor.model.entities.Invite;
import com.sasd.eventor.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    List<Invite> findAllByReceiver(User receiver);
    List<Invite> findAllByEvent(Event event);
}
