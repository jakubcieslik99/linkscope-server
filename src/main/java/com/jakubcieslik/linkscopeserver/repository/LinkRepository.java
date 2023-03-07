package com.jakubcieslik.linkscopeserver.repository;

import com.jakubcieslik.linkscopeserver.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

  Optional<Link> getLinkByIdAndUserLogin(Long id, String login);

  @Transactional
  void deleteById(Long id);
}
