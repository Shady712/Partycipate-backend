package com.sasd.eventor.event;

import com.sasd.eventor.controllers.EventController;
import com.sasd.eventor.model.daos.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public abstract class EventTest {
    @Autowired
    protected EventController eventController;
    @Autowired
    protected EventRepository eventRepository;

    protected static final LocalDateTime VALID_DATE = LocalDateTime.parse("2022-01-15T10:15:30");
    protected static final String VALID_DESCRIPTION = "test description";
    protected static final String VALID_LOCATION = "Saint-Petersburg, hookah way club";
    protected static final Integer VALID_PRICE = 2000;

    protected void clearDb() {
        eventRepository.deleteAll();
    }
}
