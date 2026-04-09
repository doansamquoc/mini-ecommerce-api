package com.sam.miniecommerceapi.common.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MailTemplate {
	PASSWORD_CHANGED("Your Mini Ecommerce password has been changed", AppConstant.PASSWORD_CHANGED_TEMPLATE),
	PASSWORD_RESET("Password reset request", AppConstant.PASSWORD_RESET_TEMPLATE),
	LOGIN_ALERT("New login to Mini Ecommerce", AppConstant.LOGIN_ALERT_TEMPLATE),
	WELCOME("Welcome to Mini Ecommerce", AppConstant.WELCOME_TEMPLATE);

	String subject;
	String template;
}
