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
public class LinksResDTO {
  private List<Link> links;
}
