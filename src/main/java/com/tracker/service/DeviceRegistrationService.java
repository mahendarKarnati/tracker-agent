//
//package com.tracker.service;
//
//import java.net.InetAddress;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.tracker.api.SessionApiClient;
//import com.tracker.entity.Device;
//import com.tracker.model.AuthSession;
//import com.tracker.model.DeviceRequest;
//import com.tracker.util.NetworkUtil;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class DeviceRegistrationService {
//
//    private final AuthSession session;
//    
//    private final SessionApiClient sessionApiClient;
//    
////    private final ConfigService configService;
//    @Value("${server.url}")
//    private String serverUrl;
//
//    private final RestTemplate restTemplate =
//            new RestTemplate();
//
////    private static final String SERVER_URL =configService.getUrl();
//
//    
//    
//    
//    public void registerDevice() {
//
//        try {
//
//        	log.info("REGISTER DEVICE START");
//
//        	log.info("USER ID = {}" , session.getUserId());
//
//            if (session.getUserId() == null) {
//
//            	log.error("User not logged in");
//
//                return;
//            }
//
//            DeviceRequest request = new DeviceRequest();
//
//
//            request.setMachineName(
//                    InetAddress.getLocalHost().getHostName());
//
//            request.setOsName(
//                    System.getProperty("os.name"));
//
//            request.setMacAddress(
//                    NetworkUtil.getMacAddress());
//
//            request.setLastIpAddress(
//                    NetworkUtil.getLocalIpAddress());
//
//            log.info("Calling Register API...");
//
//            Device device =
//                    restTemplate.postForObject(
////                            SERVER_URL
//                    		serverUrl
//                            + "/api/device/register/"
//                            + session.getUserId(),
//                            request,
//                            Device.class);
//
//            log.info("API RESPONSE = {}" , device);
//
//            if (device == null) {
//
//            	log.error("DEVICE IS NULL");
//
//                return;
//            }
//
//            session.setDeviceId(device.getId());
//
//            log.info("DEVICE ID SET = {}"
//                    , session.getDeviceId());
//
//            sessionApiClient.startSession();
//
//            log.info("SESSION STARTED");
//
//        } catch (Exception ex) {
//
//        	log.error(ex.getMessage());
//        }
//    }
//}



package com.tracker.service;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tracker.api.SessionApiClient;
import com.tracker.entity.Device;
import com.tracker.model.AuthSession;
import com.tracker.model.DeviceRequest;
import com.tracker.util.NetworkUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceRegistrationService {

    private final AuthSession session;

    private final SessionApiClient sessionApiClient;

    private final OfflineStorageService offlineStorageService;

    @Value("${server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();

    public boolean registerDevice() {

        try {

            log.info("REGISTER DEVICE START");

            if (session.getUserId() == null) {

                log.warn("User not logged in.");

                return false;
            }

            DeviceRequest request = new DeviceRequest();

            request.setMachineName(
                    InetAddress.getLocalHost().getHostName());

            request.setOsName(
                    System.getProperty("os.name"));

            request.setMacAddress(
                    NetworkUtil.getMacAddress());

            request.setLastIpAddress(
                    NetworkUtil.getLocalIpAddress());

            log.info("Calling Device Registration API...");

            Device device =
                    restTemplate.postForObject(
                            serverUrl
                                    + "/api/device/register/"
                                    + session.getUserId(),
                            request,
                            Device.class);

            if (device == null) {

                log.error("Device Registration Failed (NULL response)");

                session.setDeviceId(null);

                return false;
            }

            session.setDeviceId(device.getId());

            log.info("DEVICE REGISTERED : {}", device.getId());

            sessionApiClient.startSession();

            log.info("SESSION STARTED");

            // Sync offline records after successful registration
            offlineStorageService.syncProcesses(session);

            log.info("Offline Sync Completed");

            return true;

        } catch (Exception ex) {

            session.setDeviceId(null);

            log.error("Device Registration Failed", ex);

            return false;
        }
    }
}