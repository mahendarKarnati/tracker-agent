
package com.tracker.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.entity.ActiveWindowActivity;
import com.tracker.model.AuthSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WindowApiClient {

    private final AuthSession authSession;

    @Value("${server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();

    // CREATE
    public ActiveWindowActivity create(
            ActiveWindowActivity activity) {

        if (authSession.getDeviceId() == null) {
            return activity;
        }

        String url =
                serverUrl
                + "/api/window/"
                + authSession.getDeviceId();

        try {

            return restTemplate.postForObject(
                    url,
                    activity,
                    ActiveWindowActivity.class);

        } catch (Exception ex) {

        	log.info(
                    "Window CREATE Failed: {}",ex.getMessage());

            return activity;
        }
    }

    // UPDATE
    public void update(
            ActiveWindowActivity activity) {

        if (activity.getId() == null) {
            return;
        }

        String url =
                serverUrl
                + "/api/window/"
                + activity.getId();

        try {

            restTemplate.put(
                    url,
                    activity);

        } catch (Exception ex) {

        	log.error(
                    "Window UPDATE Failed: {}",ex.getMessage());
        }
    }
}