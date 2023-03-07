package com.jakubcieslik.linkscopeserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CredentialsReqDTO {

  @NotNull(message = "Email must be provided.")
  @NotEmpty(message = "Email cannot be empty.")
  @Size(max = 60, message = "Email cannot be longer than 60 characters.")
  @Email(message = "Email is not valid.")
  private String login;

  @NotNull(message = "Password must be provided.")
  @NotEmpty(message = "Password cannot be empty.")
  @Size(min = 8, max = 60, message = "Password must be between 8 and 60 characters long.")
  private char[] password;

  private char[] newpassword;
}
