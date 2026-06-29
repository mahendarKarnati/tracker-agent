
package com.tracker.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.entity.ProcessActivity;
import com.tracker.model.AuthSession;
import com.tracker.service.OfflineStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessApiClient {

    private final AuthSession authSession;
    private final OfflineStorageService offlineStorageService;

    @Value("${server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();

    // CREATE (POST)
    public ProcessActivity create(
            ProcessActivity activity) {

        String url =
                serverUrl
                + "/api/process/"
                + authSession.getDeviceId();

        try {

            ProcessActivity saved =
                    restTemplate.postForObject(
                            url,
                            activity,
                            ProcessActivity.class);

            log.info(
                    "PROCESS CREATED");

            return saved;

        } catch (Exception ex) {

        	log.error(
                    "CREATE FAILED");

            offlineStorageService.save(activity);

            return activity;
        }
    }

    // UPDATE (PUT)
    public void update(
            ProcessActivity activity) {

        String url =
                serverUrl
                + "/api/process/"
                + activity.getId();

        try {

            restTemplate.put(
                    url,
                    activity);

            log.info(
                    "PROCESS UPDATED");

        } catch (Exception ex) {

        	log.error(
                    "UPDATE FAILED");

            offlineStorageService.save(activity);
        }
    }
}