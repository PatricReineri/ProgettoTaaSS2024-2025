package com.service.usermanagementservice.repository;

import com.service.usermanagementservice.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
}
