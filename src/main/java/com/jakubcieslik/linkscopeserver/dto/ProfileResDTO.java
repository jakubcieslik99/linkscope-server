package com.jakubcieslik.linkscopeserver.dto;

import com.jakubcieslik.linkscopeserver.model.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProfileResDTO {
  private String alias;
  private String title;
  private String bio;
  private List<Link> links;
}
