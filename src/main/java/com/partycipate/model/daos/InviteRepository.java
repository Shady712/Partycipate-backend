package com.partycipate.model.daos;

import com.partycipate.model.entities.Event;
import com.partycipate.model.entities.Invite;
import com.partycipate.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    List<Invite> findAllByReceiver(User receiver);
    List<Invite> findAllByEvent(Event event);
}
