package com.jakubcieslik.linkscopeserver.model;

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
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "linkIdSequenceGenerator")
  @GenericGenerator(
      name = "linkIdSequenceGenerator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
          @org.hibernate.annotations.Parameter(name = "initial_value", value = "100000000"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
      }
  )
  private Long id;

  @Column(nullable = false)
  @Size(max = 60)
  private String title;

  @Column(nullable = false)
  @Size(max = 255)
  private String link;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
