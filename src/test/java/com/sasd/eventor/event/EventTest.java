package com.sasd.eventor.event;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.model.daos.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
public abstract class EventTest {
    @Autowired
    protected EventController eventController;
    @Autowired
    protected EventRepository eventRepository;

    protected static final Instant VALID_DATE = Instant.parse("2022-06-21T19:34:50.63Z");
    protected static final String VALID_DESCRIPTION = "test description";
    protected static final String VALID_LOCATION = "Saint-Petersburg, hookah way club";
    protected static final Integer VALID_PRICE = 2000;

    protected void clearDb() {
        eventRepository.deleteAll();
    }
}
