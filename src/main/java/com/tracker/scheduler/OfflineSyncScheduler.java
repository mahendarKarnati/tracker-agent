
package com.tracker.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracker.model.AuthSession;
import com.tracker.service.OfflineStorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfflineSyncScheduler {

    private final OfflineStorageService offlineStorageService;
    private final AuthSession authSession;

    @Scheduled(fixedDelay = 60000)
    public void sync() {

        if (authSession.getToken() == null ||
            authSession.getDeviceId() == null) {
            return;
        }

        offlineStorageService.syncProcesses(
                authSession);
    }
}