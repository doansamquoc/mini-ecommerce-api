package com.sam.miniecommerceapi.user.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
  @Column(name = "username", unique = true, nullable = false)
  String username;

  @Column(name = "email", unique = true)
  String email;

  @Column(name = "phone", unique = true)
  String phone;

  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "display_name")
  String displayName;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  Set<Role> roles;
}
