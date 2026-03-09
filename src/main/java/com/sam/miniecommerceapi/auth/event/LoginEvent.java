package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.LoginMailData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginEvent extends ApplicationEvent {
    LoginMailData data;

    public LoginEvent(Object source, LoginMailData data) {
        super(source);
        this.data = data;
    }
}
