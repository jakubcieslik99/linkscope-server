package com.jakubcieslik.linkscopeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginResDTO {
  private static String message = "Login successful.";
  private UserInfo userInfo;
  private String accessToken;
}
