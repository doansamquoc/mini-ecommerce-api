package com.sam.miniecommerceapi.auth.event;

import com.sam.miniecommerceapi.mail.dto.PasswordChangedMailData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordChangedEvent extends ApplicationEvent {
    PasswordChangedMailData data;

    public PasswordChangedEvent(Object source, PasswordChangedMailData data) {
        super(source);
        this.data = data;
    }
}