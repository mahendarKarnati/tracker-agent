package com.tracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.model.RegisterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterService {
//	private final ConfigService configService;
	@Value("${server.url}")
    private String serverUrl;
    private final RestTemplate restTemplate =
            new RestTemplate();

    public boolean register(
            String username
//            ,
//            String password
            ) {

        try {

            RegisterRequest request =
                    new RegisterRequest();

            request.setUsername(username);
//            request.setPassword(password);

            restTemplate.postForObject(
                    serverUrl+"/api/auth/register",
                    request,
                    String.class);

            return true;

        } catch (Exception e) {

           log.error(e.getMessage());

            return false;
        }
    }
}