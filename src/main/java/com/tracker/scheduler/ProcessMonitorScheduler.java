
package com.tracker.scheduler;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tracker.model.AuthSession;
import com.tracker.model.RunningProcess;
import com.tracker.service.ProcessScannerService;
import com.tracker.service.ProcessTrackingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessMonitorScheduler {

    private final ProcessScannerService scanner;

    private final ProcessTrackingService tracker;

    private final AuthSession authSession;

    @Scheduled(fixedDelay = 5000)
    public void scanProcesses() {

    	if (authSession.getToken() == null) {
    	    return;
    	}

    	if (authSession.getDeviceId() == null) {

    		log.info("Waiting for device registration...");

    	    return;
    	}

    	log.info("PROCESS SCHEDULER RUNNING");

        Map<Long, RunningProcess> current =
                scanner.getRunningProcesses();

        log.info(
                "PROCESSES FOUND = {}" ,
                current.size());

        tracker.monitor(current);
    }
}
