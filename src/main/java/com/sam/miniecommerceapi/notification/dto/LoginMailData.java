package com.sam.miniecommerceapi.notification.dto;

import java.time.Clock;


public record LoginMailData(String email, String username, String ip, String agent, String resetLink, Clock clock) {
}
