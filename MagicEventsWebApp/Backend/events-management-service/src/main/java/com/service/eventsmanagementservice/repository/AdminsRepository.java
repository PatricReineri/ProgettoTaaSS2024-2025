package com.service.eventsmanagementservice.repository;

import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Partecipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminsRepository extends JpaRepository<Admin, Long> {
    Boolean existsByUser(Partecipant user);
    Admin findByUser(Partecipant user);
}
