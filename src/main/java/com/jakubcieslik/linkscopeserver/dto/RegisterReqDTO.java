package com.jakubcieslik.linkscopeserver.dto;

import com.jakubcieslik.linkscopeserver.common.Constants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegisterReqDTO {

  @NotNull(message = "Email must be provided.")
  @NotEmpty(message = "Email cannot be empty.")
  @Size(max = 60, message = "Email cannot be longer than 60 characters.")
  @Email(message = "Email is not valid.")
  private String login;

  @NotNull(message = "Password must be provided.")
  @NotEmpty(message = "Password cannot be empty.")
  @Size(min = 8, max = 60, message = "Password must be between 8 and 60 characters long.")
  private char[] password;

  @NotNull(message = "Repeated password must be provided.")
  private char[] repassword;

  @NotNull(message = "Alias must be provided.")
  @NotEmpty(message = "Alias cannot be empty.")
  @Size(min = 3, max = 30, message = "Alias must be between 3 and 30 characters long.")
  @Pattern(regexp = Constants.ALIAS_REGEX, message = "Alias is not valid.")
  private String alias;

  @NotNull(message = "Terms and conditions acceptation must be provided.")
  @AssertTrue(message = "You must accept terms and conditions.")
  private boolean terms;
}
