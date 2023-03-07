package com.jakubcieslik.linkscopeserver.service;

import com.jakubcieslik.linkscopeserver.dto.ProfileResDTO;
import com.jakubcieslik.linkscopeserver.error.AppError;
import com.jakubcieslik.linkscopeserver.model.User;
import com.jakubcieslik.linkscopeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {

  private final UserRepository userRepository;

  public ProfileResDTO getProfile(String alias) {
    Optional<User> profile = userRepository.findByAlias(alias);
    if (profile.isEmpty())
      throw new AppError("Profile doesn't exist.", HttpStatus.NOT_FOUND);

    return ProfileResDTO.builder()
        .alias(profile.get().getAlias())
        .title(profile.get().getTitle())
        .bio(profile.get().getBio())
        .links(profile.get().getLinks())
        .build();
  }
}
