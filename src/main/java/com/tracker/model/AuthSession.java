package com.tracker.model;

import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@Service
public class AuthSession {

    private String token;

    private Long userId;

    private Long deviceId;

    private String username;
    private String role;
}