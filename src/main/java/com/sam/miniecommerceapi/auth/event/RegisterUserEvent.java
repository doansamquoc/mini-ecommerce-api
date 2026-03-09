package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.WelcomeMailData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterUserEvent extends ApplicationEvent {
    WelcomeMailData data;

    public RegisterUserEvent(Object source, WelcomeMailData data) {
        super(source);
        this.data = data;
    }
}
