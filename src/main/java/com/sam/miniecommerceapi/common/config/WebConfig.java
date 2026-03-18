package com.sam.miniecommerceapi.common.config;

import com.sam.miniecommerceapi.common.resolver.ClientIpArgumentResolver;
import com.sam.miniecommerceapi.common.resolver.CurrentUserIdArgumentResolver;
import com.sam.miniecommerceapi.common.resolver.UserAgentArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ClientIpArgumentResolver());
        resolvers.add(new UserAgentArgumentResolver());
        resolvers.add(new CurrentUserIdArgumentResolver());
    }
}
