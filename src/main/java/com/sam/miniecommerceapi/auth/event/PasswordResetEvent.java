package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.PasswordResetMailData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetEvent extends ApplicationEvent {
    PasswordResetMailData data;

    public PasswordResetEvent(Object source, PasswordResetMailData data) {
        super(source);
        this.data = data;
    }
}
