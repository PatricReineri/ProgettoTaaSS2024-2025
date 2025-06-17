package com.service.eventsmanagementservice.repository;

import com.service.eventsmanagementservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
