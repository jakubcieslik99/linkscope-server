package com.jakubcieslik.linkscopeserver.repository;

import com.jakubcieslik.linkscopeserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLogin(String login);

  Optional<User> findByAlias(String alias);
}
