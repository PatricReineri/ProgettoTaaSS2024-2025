package com.service.guestgameservice.repository;

import com.service.guestgameservice.model.GuestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestGameRepository extends JpaRepository<GuestInfo, Long> {
}
