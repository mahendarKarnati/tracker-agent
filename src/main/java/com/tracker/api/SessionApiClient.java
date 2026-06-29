package com.tracker.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.model.AuthSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionApiClient {

    private final AuthSession authSession;
//    private final ConfigService configService;
    @Value("${server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();

   
    
    public void startSession() {

    	log.info("START SESSION CALLED");

        if (authSession.getDeviceId() == null) {
        	log.info("DEVICE ID NULL");
            return;
        }

        log.info("DEVICE ID = {}"  ,authSession.getDeviceId());

        restTemplate.postForObject(
               serverUrl + "/api/session/start/" + authSession.getDeviceId(),
                null,
                String.class);
    }
    
    public void endSession() {

        if (authSession.getDeviceId() == null)
            return;

        restTemplate.postForObject(
                serverUrl
                        + "/api/session/end/"
                        + authSession.getDeviceId(),
                null,
                String.class);
    }
}
