package com.service.eventsmanagementservice.repository;

import com.service.eventsmanagementservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Event, Long> {

}
