package com.sam.miniecommerceapi.auth.entity;

import com.sam.miniecommerceapi.common.entity.BaseEntity;
import com.sam.miniecommerceapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetToken extends BaseEntity {
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	User user;

	@Column(name = "token", nullable = false, unique = true)
	String token;

	@Column(name = "expires_at", nullable = false)
	Instant expiresAt;

	@Column(name = "is_used", nullable = false)
	boolean isUsed;

	public boolean isExpired() {
		return Instant.now().isAfter(this.expiresAt);
	}
}
