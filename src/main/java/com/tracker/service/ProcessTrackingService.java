
package com.tracker.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.tracker.api.ProcessApiClient;
import com.tracker.entity.ProcessActivity;
import com.tracker.model.RunningProcess;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessTrackingService {

    private final ProcessApiClient processApiClient;

    private final Map<Long, ProcessActivity> activeProcesses =
            new ConcurrentHashMap<>();

    private final Map<Long, RunningProcess> previousProcesses =
            new ConcurrentHashMap<>();


    public void monitor(
            Map<Long, RunningProcess> currentProcesses) {

    	log.info("Current = {}" , currentProcesses.size());
    	log.info("Previous = {}" , previousProcesses.size());

        

        detectOpenedProcesses(currentProcesses);

        detectClosedProcesses(currentProcesses);
        previousProcesses.clear();

        previousProcesses.putAll(currentProcesses);
    }
    
    private void detectOpenedProcesses(
            Map<Long, RunningProcess> current) {

        current.values().forEach(process -> {

            // Already tracking this PID
            if (activeProcesses.containsKey(process.getPid())) {
                return;
            }

            ProcessActivity activity =
                    ProcessActivity.builder()
                            .pid(process.getPid())
                            .processName(process.getProcessName())
                            .startTime(LocalDateTime.now())
                            .status("RUNNING")
                            .build();

            activeProcesses.put(
                    process.getPid(),
                    activity);

//            processApiClient.send(activity);
            ProcessActivity saved =
                    processApiClient.create(activity);

            if (saved != null) {
                activity.setId(saved.getId());
            }

            log.info(
                    "PROCESS OPENED : {} PID = {}" ,process.getProcessName(),process.getPid());
        });
    }


    private void detectClosedProcesses(
            Map<Long, RunningProcess> current) {

        activeProcesses.entrySet()
                .removeIf(entry -> {

                    Long pid = entry.getKey();

                    if (current.containsKey(pid)) {
                        return false;
                    }

                    ProcessActivity activity =
                            entry.getValue();

                    LocalDateTime end =
                            LocalDateTime.now();

                    activity.setEndTime(end);

                    activity.setDurationSeconds(
                            Duration.between(
                                    activity.getStartTime(),
                                    end)
                                    .getSeconds());

                    activity.setStatus("CLOSED");

//                    processApiClient.send(activity);
                    processApiClient.update(activity);

                    log.info(
                            "PROCESS CLOSED : {} PID = {}"
                            ,activity.getProcessName(),pid);

                    return true;
                });
    }
    
    
    @PreDestroy
    public void shutdown() {

        activeProcesses.values()
                .forEach(activity -> {

                    activity.setEndTime(
                            LocalDateTime.now());

                    activity.setDurationSeconds(

                            Duration.between(
                                    activity.getStartTime(),
                                    LocalDateTime.now())
                                    .getSeconds());

                    activity.setStatus(
                            "CLOSED");

                    processApiClient.update(
                            activity);
                });

        activeProcesses.clear();
    }
}