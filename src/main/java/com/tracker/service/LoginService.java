
package com.tracker.service;

import java.io.File;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.model.AuthSession;
import com.tracker.model.LoginCache;
import com.tracker.model.LoginRequest;
import com.tracker.model.LoginResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthSession session;
//    private final ConfigService configService;
    @Value("${server.url}")
    private String serverUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();
    private final ObjectMapper mapper =
            new ObjectMapper();

    private final File appFolder =
            new File(
                    System.getenv("APPDATA"),
                    "TrackerAgent");

    private final File loginFile =
            new File(
                    appFolder,
                    "login.json");
//    public LoginService(AuthSession session) {
//
//        this.session = session;
//
//        if (!appFolder.exists()) {
//
//            appFolder.mkdirs();
//        }
//    }
    public boolean login(
            String username
//            ,
//            String password
            ) {
    	log.info(
    	        serverUrl
    	        + "/api/auth/login");
        try {

            LoginRequest request =
                    new LoginRequest();

            request.setUsername(username);
//            request.setPassword(password);

            LoginResponse response =
                    restTemplate.postForObject(
                            serverUrl + "/api/auth/login",
                            request,
                            LoginResponse.class);

            session.setToken(response.getToken());
            session.setUserId(response.getUserId());
            session.setUsername(response.getUsername());
            session.setRole(response.getRole());

            LoginCache cache =
                    LoginCache.builder()
                            .token(response.getToken())
                            .userId(response.getUserId())
                            .username(response.getUsername())
                            .role(response.getRole())
                            .build();
            if (!appFolder.exists()) {
                appFolder.mkdirs();
            }
            log.info("LOGIN FILE = " + loginFile.getAbsolutePath());
            mapper.writeValue(loginFile, cache);

            log.info("LOGIN SUCCESS");

            return true;

        } catch (HttpClientErrorException ex) {

        	log.info("Invalid Username");

            return false;

        } catch (ResourceAccessException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Cannot connect to Tracker Server.\nPlease check if the server is running.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);

            return false;

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Unexpected Error : " + ex.getMessage());

            return false;
        }
    }
    
    
    public boolean autoLogin() {

        try {

            if (!loginFile.exists()) {
                return false;
            }

            LoginCache cache =
                    mapper.readValue(
                            loginFile,
                            LoginCache.class);

            session.setToken(
                    cache.getToken());

            session.setUserId(
                    cache.getUserId());

            session.setUsername(
                    cache.getUsername());

            session.setRole(
                    cache.getRole());

            log.info(
                    "AUTO LOGIN SUCCESS");

            log.info(
                    "USER ID = {}"
                    , session.getUserId());

            return true;

        } catch (Exception ex) {

        	log.error(ex.getMessage());

            return false;
        }
    }
    

    
    public void logout() {

        try {

            if (loginFile.exists()) {

                loginFile.delete();
            }

        } catch (Exception ex) {

            log.error(ex.getMessage());
        }

        session.setToken(null);
        session.setUserId(null);
        session.setUsername(null);
        session.setRole(null);
        session.setDeviceId(null);

        log.info("LOGOUT SUCCESS");
    }
}