package com.tracker.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracker.api.HeartbeatApiClient;
import com.tracker.model.AuthSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeartbeatScheduler {

    private final HeartbeatApiClient apiClient;

    private final AuthSession session;

    @Scheduled(fixedDelay = 30000)
    public void heartbeat() {
    	log.info("HEARTBEAT SCHEDULER");

        if (session.getToken() == null) {
        	log.error("TOKEN NULL");
            return;
        }
        
        if (session.getDeviceId() == null) {
        	log.error("DEVICE ID NULL");
            return;
        }
        log.info("Sending heartbeat for device = {}" , session.getDeviceId());
        apiClient.send();
    }
}