package com.tracker.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.entity.OfflineSession;
import com.tracker.model.AuthSession;
import com.tracker.service.OfflineStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionApiClient {

    private final AuthSession authSession;
    private final OfflineStorageService offlineStorageService;
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

        log.info("END SESSION CALLED");

        if (authSession.getDeviceId() == null) {

            log.warn("DeviceId is null");

            return;
        }

        log.info("Ending session for device {}", authSession.getDeviceId());

        try {

            restTemplate.postForObject(
                    serverUrl + "/api/session/end/" + authSession.getDeviceId(),
                    null,
                    String.class);

            log.info("END SESSION SUCCESS");

        } catch (Exception ex) {

            log.error("END SESSION FAILED", ex);

            offlineStorageService.saveSession(
                    OfflineSession.builder()
                            .deviceId(authSession.getDeviceId())
                            .endTime(LocalDateTime.now())
                            .status("END")
                            .build());
        }
    }
}
