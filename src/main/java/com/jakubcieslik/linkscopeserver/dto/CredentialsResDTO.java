package com.jakubcieslik.linkscopeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CredentialsResDTO {
  private final String message = "Credentials updated successfully.";
  private UserInfo userInfo;
  private String accessToken;
}
