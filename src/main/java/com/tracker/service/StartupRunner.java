
package com.tracker.service;

import javax.swing.SwingUtilities;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tracker.ui.LoginFrame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupRunner {

    private final LoginService loginService;
    private final DeviceRegistrationService deviceRegistrationService;
    private final RegisterService registerService;
    private final StartupService startupService;
    private final TrayManager trayManager;

//    @EventListener(ApplicationReadyEvent.class)
//    public void init() {
//    	  log.info("StartupRunner started");
//        if (loginService.autoLogin()) {
//        	startupService.enableStartup();
//
//            deviceRegistrationService.registerDevice();
//
//            log.info("AUTO LOGIN SUCCESS");
//
//            return;
//        }
//
//        SwingUtilities.invokeLater(() -> {
//
//            LoginFrame frame =
//                    new LoginFrame(
//                            loginService,
//                            deviceRegistrationService,registerService,startupService,trayManager);
//
//            frame.setVisible(true);
//        });
//    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
    	log.info("PID = {}", ProcessHandle.current().pid());
        log.info("StartupRunner started");

        boolean auto = loginService.autoLogin();

        log.info("Auto Login = {}", auto);

        if (auto) {

            log.info("Calling enableStartup()");

            startupService.enableStartup();

            log.info("Calling registerDevice()");

            deviceRegistrationService.registerDevice();

            return;
        }

        log.info("Showing Login Screen");

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame(
                    loginService,
                    deviceRegistrationService,
                    registerService,
                    startupService,
                    trayManager);
            frame.setVisible(true);
        });
    }
    
}