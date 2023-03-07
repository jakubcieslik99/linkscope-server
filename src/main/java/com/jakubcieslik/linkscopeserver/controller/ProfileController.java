package com.jakubcieslik.linkscopeserver.controller;

import com.jakubcieslik.linkscopeserver.dto.ProfileResDTO;
import com.jakubcieslik.linkscopeserver.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping("/getProfile/{alias}")
  @ResponseBody
  public ResponseEntity<ProfileResDTO> getProfile(@PathVariable String alias) {
    return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfile(alias));
  }
}
