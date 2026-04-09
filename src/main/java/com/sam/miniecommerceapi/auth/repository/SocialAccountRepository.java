package com.sam.miniecommerceapi.auth.repository;

import com.sam.miniecommerceapi.auth.entity.SocialAccount;
import com.sam.miniecommerceapi.common.constant.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findByProviderAndProviderId(SocialProvider provider, String providerId);

    boolean existsByProviderAndProviderId(SocialProvider provider, String providerId);
}
