package com.sam.miniecommerceapi.auth.service;

import com.sam.miniecommerceapi.auth.entity.SocialAccount;
import com.sam.miniecommerceapi.common.constant.SocialProvider;
import com.sam.miniecommerceapi.user.entity.User;

import java.util.Optional;

public interface SocialAccountService {
    Optional<SocialAccount> findSocialAccount(SocialProvider provider, String providerId);

    void createSocialAccount(User user, SocialProvider provider, String providerId);

    boolean existsBySocialAccount(SocialProvider provider, String providerId);
}
