
package com.tracker.service;

import java.io.File;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StartupService {

    private static final String REG_KEY =
            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";

    public void enableStartup() {
    	

        try {

            String exePath;

            // Installed location (Setup.exe install)
            File installedExe = new File(
                    System.getenv("ProgramFiles")
                            + "\\TrackerAgent\\TrackerAgent.exe");

            if (installedExe.exists()) {

                exePath = installedExe.getAbsolutePath();
                log.info("Enabling startup...");
                log.info("Startup path = {}", exePath);

            } else {

                // Development / Portable EXE
                exePath = new File(
                        System.getProperty("user.dir"),
                        "TrackerAgent.exe")
                        .getAbsolutePath();
            }

            log.info("Startup EXE = {}" , exePath);

            String command =
                    "reg add \"" + REG_KEY + "\" "
                    + "/v TrackerAgent "
                    + "/t REG_SZ "
                    + "/d \"" + exePath + "\" "
                    + "/f";

//            Process process =
//                    Runtime.getRuntime().exec(command);
            
            ProcessBuilder pb =
                    new ProcessBuilder(
                            "reg",
                            "add",
                            REG_KEY,
                            "/v",
                            "TrackerAgent",
                            "/t",
                            "REG_SZ",
                            "/d",
                            exePath,
                            "/f");

            Process process = pb.start();

            int exit = process.waitFor();

            log.info("REG EXIT CODE = {}", exit);

           
        } catch (Exception e) {

            log.error(e.getMessage());
        }
    }

    public void disableStartup() {

        try {

            String command =
                    "reg delete \"" + REG_KEY + "\" "
                    + "/v TrackerAgent "
                    + "/f";

            Process process =
                    Runtime.getRuntime().exec(command);

            process.waitFor();

            log.info("Startup Disabled");

        } catch (Exception e) {

           log.error(e.getMessage());
        }
    }
}