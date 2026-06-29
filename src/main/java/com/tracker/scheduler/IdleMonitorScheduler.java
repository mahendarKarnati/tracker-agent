
package com.tracker.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tracker.api.IdleApiClient;
import com.tracker.entity.IdleActivity;
import com.tracker.model.AuthSession;
import com.tracker.service.IdleTimeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdleMonitorScheduler {

    private final IdleTimeService idleTimeService;
    private final IdleApiClient idleApiClient;
    private final AuthSession authSession;

    private boolean idleStarted = false;
    private IdleActivity currentIdleActivity;

    @Scheduled(fixedDelay = 5000)
    public void checkIdle() {
    	if (authSession.getToken() == null) {
    	    return;
    	}

        if (authSession.getDeviceId() == null) {
            return;
        }

        long idleSeconds =
                idleTimeService.getIdleTimeSeconds();

        log.info(
                "Current Idle Seconds : {}"
                        ,idleSeconds);

        if (idleSeconds >= 5 &&
                !idleStarted) {

            idleStarted = true;

            currentIdleActivity =
                    new IdleActivity();

            currentIdleActivity.setIdleStart(
                    LocalDateTime.now()
                            .minusSeconds(
                                    idleSeconds));

            currentIdleActivity.setStatus(
                    "ACTIVE");

            currentIdleActivity = idleApiClient.start(
                    currentIdleActivity);

            log.info(
                    "IDLE STARTED");
        }

        if (idleSeconds < 5 &&
                idleStarted) {

            idleStarted = false;

            LocalDateTime endTime =
                    LocalDateTime.now();

            currentIdleActivity.setIdleEnd(
                    endTime);

            currentIdleActivity.setIdleSeconds(
                    Duration.between(
                            currentIdleActivity.getIdleStart(),
                            endTime)
                            .getSeconds());

            currentIdleActivity.setStatus(
                    "CLOSED");

            idleApiClient.end(
                    currentIdleActivity);

            log.info(
                    "IDLE ENDED");
        }
    }
}

