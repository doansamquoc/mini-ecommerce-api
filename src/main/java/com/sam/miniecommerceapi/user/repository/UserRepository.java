package com.sam.miniecommerceapi.user.repository;

import com.sam.miniecommerceapi.user.entity.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
	Optional<User> findByIdentifier(@Param("identifier") String identifier);

	/**
	 * Find any user ignore is_deleted = true with native query
	 *
	 * @param id user ID
	 * @return User
	 */
	@Query(value = "SELECT * FROM users WHERE id = ?", nativeQuery = true)
	Optional<User> findAnyById(Long id);

	/**
	 * Restore user with native query
	 *
	 * @param id user ID
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE users SET is_deleted = false WHERE id = ?", nativeQuery = true)
	void restoreById(Long id);

	@NullMarked
	Page<User> findAll(Pageable pageable);

	@EntityGraph(attributePaths = {"roles"})
	Optional<User> findWithRolesByEmail(String email);

	Optional<User> findByEmail(String email);

	@NullMarked
	Optional<User> findById(Long id);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}
