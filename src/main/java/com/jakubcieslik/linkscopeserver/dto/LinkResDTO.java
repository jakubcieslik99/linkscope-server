package com.jakubcieslik.linkscopeserver.dto;

import com.jakubcieslik.linkscopeserver.model.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LinkResDTO {
  private String message;
  private Link link;
}
