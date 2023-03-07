package com.jakubcieslik.linkscopeserver.dto;

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
public class LinkReqDTO {

  @NotNull(message = "Title must be provided.")
  @NotEmpty(message = "Title cannot be empty.")
  @Size(min = 1, max = 60, message = "Title must be between 1 and 60 characters long.")
  private String title;

  @NotNull(message = "Link must be provided.")
  @NotEmpty(message = "Link cannot be empty.")
  @Size(min = 8, max = 255, message = "Link must be between 8 and 255 characters long.")
  private String link;
}
