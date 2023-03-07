package com.jakubcieslik.linkscopeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DetailsResDTO {
  private final String message = "Details updated successfully.";
  private DetailsInfo detailsInfo;
}
