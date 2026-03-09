package com.sam.miniecommerceapi.mail.dto;

import java.time.Clock;


public record LoginMailData(String email, String username, String ip, String agent, String token, Clock clock) {
}
