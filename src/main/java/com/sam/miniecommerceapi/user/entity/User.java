package com.sam.miniecommerceapi.user.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.common.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@SQLRestriction("is_deleted = false")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
public class User extends BaseEntity {
    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "phone", unique = true)
    String phone;

    @Column(name = "password")
    String password;

    @Column(name = "display_name")
    String displayName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    Set<Role> roles = Set.of(Role.USER);

    @Column(name = "is_deleted")
    @Builder.Default
    boolean isDeleted = false;
}
