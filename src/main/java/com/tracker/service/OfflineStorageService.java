
package com.tracker.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tracker.entity.ProcessActivity;
import com.tracker.model.AuthSession;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OfflineStorageService {

//    private final ConfigService configService;
	@Value("${server.url}")
    private String serverUrl;

    private final ObjectMapper mapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final File folder;
    private final File processFile;

    public OfflineStorageService() {

       

        folder = new File("offline");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        processFile = new File(folder, "processes.json");
    }

    public synchronized void save(ProcessActivity activity) {

        try {

            List<ProcessActivity> list = new ArrayList<>();

            if (processFile.exists()) {

                try {

                    list = mapper.readValue(
                            processFile,
                            new TypeReference<List<ProcessActivity>>() {});

                } catch (Exception ex) {

                    processFile.delete();
                    list = new ArrayList<>();
                }
            }

            boolean exists =
                    list.stream()
                        .anyMatch(p ->

                                p.getPid().equals(activity.getPid())

                                &&

                                p.getStatus().equals(activity.getStatus())

                                &&

                                p.getStartTime().equals(activity.getStartTime()));

            if (!exists) {

                list.add(activity);
            }

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(processFile, list);

            log.info("Saved Offline : {}" , activity.getProcessName());

        } catch (Exception e) {

            log.error(e.getMessage());
        }
    }

    public void syncProcesses(AuthSession authSession) {

        try {

            if (!processFile.exists()) {
                return;
            }

            List<ProcessActivity> list =
                    mapper.readValue(
                            processFile,
                            new TypeReference<List<ProcessActivity>>() {});

            RestTemplate restTemplate = new RestTemplate();

            String url =
            		serverUrl
                    + "/api/process/"
                    + authSession.getDeviceId();

            int count = 0;

            for (ProcessActivity activity : list) {

            	if (activity.getId() == null) {

            	    restTemplate.postForObject(
            	            url,
            	            activity,
            	            String.class);

            	} else {

            	    restTemplate.put(
            	            serverUrl
            	            + "/api/process/"
            	            + activity.getId(),
            	            activity);
            	}

                count++;
            }

            processFile.delete();

            log.info(
                    "Offline Sync Completed : {} records",count);

        } catch (Exception e) {

           log.error(e.getMessage());
        }
    }

    public void clear() {

        if (processFile.exists()) {
            processFile.delete();
        }
    }
}