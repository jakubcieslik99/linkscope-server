package com.jakubcieslik.linkscopeserver.dto;

import com.jakubcieslik.linkscopeserver.common.Constants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DetailsReqDTO {

  @NotNull(message = "Alias must be provided.")
  @NotEmpty(message = "Alias cannot be empty.")
  @Size(min = 3, max = 30, message = "Alias must be between 3 and 30 characters long.")
  @Pattern(regexp = Constants.ALIAS_REGEX, message = "Alias is not valid.")
  private String alias;

  @Size(max = 60, message = "Title cannot be longer than 60 characters.")
  private String title;

  @Size(max = 255, message = "Bio cannot be longer than 255 characters.")
  private String bio;
}
