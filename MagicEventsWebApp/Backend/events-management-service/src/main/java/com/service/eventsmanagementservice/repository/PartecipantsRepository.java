package com.service.eventsmanagementservice.repository;

import com.service.eventsmanagementservice.model.Partecipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartecipantsRepository extends JpaRepository<Partecipant, Long> {

}
