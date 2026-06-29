package com.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityScheduler {

    private final AppMonitorService appMonitorService;

    private String lastWindow = "";

    @Scheduled(fixedDelay = 1000)
    public void trackActivity() {

        String currentWindow =
                appMonitorService.getActiveWindowTitle();

        if (!currentWindow.equals(lastWindow)) {

        	log.info("Current App : {}" , currentWindow);

            lastWindow = currentWindow;
        }
    }
}