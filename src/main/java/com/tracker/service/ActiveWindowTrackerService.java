
package com.tracker.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.tracker.api.WindowApiClient;
import com.tracker.entity.ActiveWindowActivity;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActiveWindowTrackerService {

    private final WindowApiClient windowApiClient;

    private String lastWindow = "";

    private ActiveWindowActivity currentActivity;

    public synchronized void monitor(String currentWindow) {

        if (currentWindow == null ||
                currentWindow.isBlank()) {

            closeCurrentActivity();

            lastWindow = "";

            return;
        }

        if (currentWindow.equals(lastWindow)) {
            return;
        }

        closeCurrentActivity();

        currentActivity =
                ActiveWindowActivity.builder()
                        .windowTitle(currentWindow)
                        .startTime(LocalDateTime.now())
                        .status("RUNNING")
                        .build();

        ActiveWindowActivity saved =
                windowApiClient.create(currentActivity);

        if (saved != null) {
            currentActivity.setId(saved.getId());
        }

        lastWindow = currentWindow;

        log.info(
                "ACTIVE WINDOW : {}"
                , currentWindow);
    }

    private void closeCurrentActivity() {

        if (currentActivity == null) {
            return;
        }

        LocalDateTime end =
                LocalDateTime.now();

        currentActivity.setEndTime(end);

        currentActivity.setDurationSeconds(
                Duration.between(
                        currentActivity.getStartTime(),
                        end)
                        .getSeconds());

        currentActivity.setStatus("CLOSED");

        windowApiClient.update(currentActivity);

        currentActivity = null;
    }

    @PreDestroy
    public void shutdown() {

        closeCurrentActivity();

        lastWindow = "";

        log.info(
                "Window tracking stopped.");
    }
}