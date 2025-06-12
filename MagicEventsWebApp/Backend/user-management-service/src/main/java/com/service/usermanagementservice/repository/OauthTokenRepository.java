package com.service.usermanagementservice.repository;

import com.service.usermanagementservice.model.OauthToken;
import com.service.usermanagementservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {
    void deleteByUser(User user);
    OauthToken findByRefreshToken(String refreshToken);
}
