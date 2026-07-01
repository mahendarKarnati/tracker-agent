package com.tracker.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tracker.model.AuthSession;
import com.tracker.service.DeviceRegistrationService;
import com.tracker.service.OfflineStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceRegistrationRetryScheduler {

    private final AuthSession authSession;
    private final DeviceRegistrationService deviceRegistrationService;
    private final OfflineStorageService offlineStorageService;

    @Scheduled(fixedDelay = 30000)
    public void retryRegistration() {

        // User not logged in
        if (authSession.getToken() == null) {
            return;
        }

        // Already registered
        if (authSession.getDeviceId() != null) {
            return;
        }

        log.info("Retrying device registration...");
        log.info("Token = {}", authSession.getToken());

        log.info("DeviceId = {}", authSession.getDeviceId());

        try {

        	deviceRegistrationService.registerDevice();

        	if (authSession.getDeviceId() != null) {

        	    offlineStorageService.syncProcesses(authSession);
        	    offlineStorageService.syncSessions(authSession);

        	    log.info("Offline Sync Completed");
        	}
        } catch (Exception e) {

            log.error("Retry failed", e);
        }
    }
}