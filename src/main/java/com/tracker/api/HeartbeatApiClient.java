package com.tracker.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.model.AuthSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeartbeatApiClient {

    private final AuthSession session;

    @Value("${server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void send() {

        if (session.getDeviceId() == null) {
            return;
        }

        try {

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(session.getToken());

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            restTemplate.exchange(
                    serverUrl
                    + "/api/device/heartbeat/"
                    + session.getDeviceId(),
                    HttpMethod.POST,
                    entity,
                    Void.class);

            log.info("Heartbeat Success");

        } catch (Exception e) {

            log.info(e.getMessage());
        }
    }
}


