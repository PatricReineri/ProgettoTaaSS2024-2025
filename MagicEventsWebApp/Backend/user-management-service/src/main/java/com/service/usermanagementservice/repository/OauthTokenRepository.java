package com.service.usermanagementservice.repository;

import com.service.usermanagementservice.model.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {

}
