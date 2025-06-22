package com.service.eventsmanagementservice.repository;

import com.service.eventsmanagementservice.model.Admin;
import com.service.eventsmanagementservice.model.Partecipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminsRepository extends JpaRepository<Admin, Long> {

}
