package com.jakubcieslik.linkscopeserver.repository;

import com.jakubcieslik.linkscopeserver.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface TokenRepository extends JpaRepository<Token, Long> {

  @Transactional
  void deleteByToken(String token);

  @Transactional
  void deleteByExpirationBefore(LocalDateTime expiration);
}
