package com.jakubcieslik.linkscopeserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "link")
public class Link {
  @Id
  @GenericGenerator(
      name = "linkIdSequenceGenerator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "link_sequence"),
          @org.hibernate.annotations.Parameter(name = "initial_value", value = "100000000"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
      }
  )
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "linkIdSequenceGenerator")
  @Column(name = "id", updatable = false)
  private Long id;

  @Column(nullable = false)
  @Size(max = 60)
  private String title;

  @Column(nullable = false)
  @Size(max = 255)
  private String link;

  //relations
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;
}
