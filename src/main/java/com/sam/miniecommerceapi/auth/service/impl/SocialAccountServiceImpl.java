package com.sam.miniecommerceapi.auth.service.impl;

import com.sam.miniecommerceapi.auth.entity.SocialAccount;
import com.sam.miniecommerceapi.auth.repository.SocialAccountRepository;
import com.sam.miniecommerceapi.auth.service.SocialAccountService;
import com.sam.miniecommerceapi.shared.constant.SocialProvider;
import com.sam.miniecommerceapi.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocialAccountServiceImpl implements SocialAccountService {
    SocialAccountRepository repository;

    /**
     * Find social account by Provider and ProviderId
     * @param provider SocialProvider Enum
     * @param providerId String
     * @return Optional SocialAccount
     */
    @Override
    public Optional<SocialAccount> findSocialAccount(SocialProvider provider, String providerId) {
        return repository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public void createSocialAccount(User user, SocialProvider provider, String providerId) {
        repository.save(new SocialAccount(user, provider, providerId));
    }

    @Override
    public boolean existsBySocialAccount(SocialProvider provider, String providerId) {
        return repository.existsByProviderAndProviderId(provider, providerId);
    }
}
