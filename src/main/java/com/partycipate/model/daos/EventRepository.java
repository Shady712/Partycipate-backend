package com.partycipate.model.daos;

import com.partycipate.model.entities.Event;
import com.partycipate.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCreator(User creator);
    List<Event> findByNameStartingWith(String prefix);
}
