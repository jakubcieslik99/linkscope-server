package com.jakubcieslik.linkscopeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountResDTO {
  private Long id;
  private String alias;
  private String title;
  private String bio;
}
