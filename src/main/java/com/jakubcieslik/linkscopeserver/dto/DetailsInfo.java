package com.jakubcieslik.linkscopeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DetailsInfo {
  private String alias;
  private String title;
  private String bio;
}
