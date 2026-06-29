
package com.tracker.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracker.model.AuthSession;
import com.tracker.service.ActiveWindowTrackerService;
import com.tracker.service.WindowService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActiveWindowScheduler {

    private final WindowService windowService;
    private final ActiveWindowTrackerService trackerService;
    private final AuthSession authSession;

    @Scheduled(fixedDelay = 1000)
    public void trackWindow() {

        if (authSession.getToken() == null) {
            return;
        }

        if (authSession.getDeviceId() == null) {
            return;
        }

        String currentWindow =
                windowService.getActiveWindowTitle();

        trackerService.monitor(currentWindow);
    }
}