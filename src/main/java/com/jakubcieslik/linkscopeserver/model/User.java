package com.jakubcieslik.linkscopeserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "userIdSequenceGenerator")
  @GenericGenerator(
      name = "userIdSequenceGenerator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @Parameter(name = "sequence_name", value = "user_sequence"),
          @Parameter(name = "initial_value", value = "100000"),
          @Parameter(name = "increment_size", value = "1")
      }
  )
  private Long id;

  @Column(nullable = false, unique = true)
  @Size(max = 60)
  private String login;

  @Column(nullable = false)
  @Size(max = 255)
  private String password;

  @Column(nullable = false)
  @Size(max = 30)
  private String alias;

  @Column
  @Size(max = 60)
  private String title;

  @Column
  @Size(max = 255)
  private String bio;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private List<Link> links;
}
