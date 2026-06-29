package com.tracker.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.entity.IdleActivity;
import com.tracker.model.AuthSession;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdleApiClient {

    private final AuthSession authSession;
    @Value("${server.url}")
    private String serverUrl;
    private final RestTemplate restTemplate =
            new RestTemplate();


    public IdleActivity start(
            IdleActivity activity) {

        String url =
                serverUrl
                        + "/api/idle/start/"
                        + authSession.getDeviceId();

        return restTemplate.postForObject(
                url,
                activity,
                IdleActivity.class);
    }
    
    public void end(IdleActivity activity) {

        String url = serverUrl +
                "/api/idle/end/" +
                activity.getId();

        restTemplate.put(url, activity);
    }
}