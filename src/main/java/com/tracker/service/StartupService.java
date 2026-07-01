//
//package com.tracker.service;
//
//import java.io.File;
//
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class StartupService {
//
//    private static final String REG_KEY =
//            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
//
//    public void enableStartup() {
//    	
//
//        try {
//
//            String exePath;
//
//            // Installed location (Setup.exe install)
//            File installedExe = new File(
//                    System.getenv("ProgramFiles")
//                            + "\\TrackerAgent\\TrackerAgent.exe");
//
//            if (installedExe.exists()) {
//
//                exePath = installedExe.getAbsolutePath();
//                log.info("Enabling startup...");
//                log.info("Startup path = {}", exePath);
//
//            } else {
//
//                // Development / Portable EXE
//                exePath = new File(
//                        System.getProperty("user.dir"),
//                        "TrackerAgent.exe")
//                        .getAbsolutePath();
//            }
//
//            log.info("Startup EXE = {}" , exePath);
//
//            String command =
//                    "reg add \"" + REG_KEY + "\" "
//                    + "/v TrackerAgent "
//                    + "/t REG_SZ "
//                    + "/d \"" + exePath + "\" "
//                    + "/f";
//
////            Process process =
////                    Runtime.getRuntime().exec(command);
//            
//            ProcessBuilder pb =
//                    new ProcessBuilder(
//                            "reg",
//                            "add",
//                            REG_KEY,
//                            "/v",
//                            "TrackerAgent",
//                            "/t",
//                            "REG_SZ",
//                            "/d",
//                            exePath,
//                            "/f");
//
//            Process process = pb.start();
//
//            int exit = process.waitFor();
//
//            log.info("REG EXIT CODE = {}", exit);
//
//           
//        } catch (Exception e) {
//
//            log.error(e.getMessage());
//        }
//    }
//
//    public void disableStartup() {
//
//        try {
//
//            String command =
//                    "reg delete \"" + REG_KEY + "\" "
//                    + "/v TrackerAgent "
//                    + "/f";
//
//            Process process =
//                    Runtime.getRuntime().exec(command);
//
//            process.waitFor();
//
//            log.info("Startup Disabled");
//
//        } catch (Exception e) {
//
//           log.error(e.getMessage());
//        }
//    }
//}


package com.tracker.service;

import java.io.File;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StartupService {

    private static final String REG_KEY =
            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";

//    public void enableStartup() {
//
//        try {
//
//            String exePath = getExePath();
//
//            if (exePath == null) {
//                log.error("Unable to locate TrackerAgent.exe");
//                return;
//            }
//
//            log.info("Startup EXE = {}", exePath);
//
//            ProcessBuilder pb = new ProcessBuilder(
//                    "reg",
//                    "add",
//                    REG_KEY,
//                    "/v",
//                    "TrackerAgent",
//                    "/t",
//                    "REG_SZ",
//                    "/d",
//                    exePath,
//                    "/f");
//
//            Process process = pb.start();
//
//            int exit = process.waitFor();
//            log.info("REG EXIT CODE = {}", exit);
//
//            if (exit == 0) {
//                log.info("Startup Enabled");
//            } else {
//                log.error("Failed to add startup entry. Exit Code = {}", exit);
//            }
//
//        } catch (Exception e) {
//
//            log.error("Startup Error", e);
//        }
//    }
    
    public void enableStartup() {

        try {

            String exePath = getExePath();

            if (exePath == null) {
                return;
            }

            log.info("Startup EXE = {}", exePath);

            ProcessBuilder pb = new ProcessBuilder(
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

            log.error("Unable to enable startup", e);
        }
    }

    public void disableStartup() {

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "reg",
                    "delete",
                    REG_KEY,
                    "/v",
                    "TrackerAgent",
                    "/f");

            Process process = pb.start();

            int exit = process.waitFor();

            if (exit == 0) {
                log.info("Startup Disabled");
            } else {
                log.error("Failed to remove startup entry. Exit Code = {}", exit);
            }

        } catch (Exception e) {

            log.error("Disable Startup Error", e);
        }
    }

    /**
     * Detect current executable path.
     */
//    private String getExePath() {
//
//        try {
//
//            File location = new File(
//                    StartupService.class
//                            .getProtectionDomain()
//                            .getCodeSource()
//                            .getLocation()
//                            .toURI());
//
//            // Running as EXE
//            if (location.getName().equalsIgnoreCase("TrackerAgent.exe")) {
//                return location.getAbsolutePath();
//            }
//
//            // Running from IDE / JAR
//            File devExe = new File(
//                    System.getProperty("user.dir"),
//                    "TrackerAgent.exe");
//
//            if (devExe.exists()) {
//                return devExe.getAbsolutePath();
//            }
//
//            // Installed by jpackage
//            File installedExe = new File(
//                    System.getenv("LOCALAPPDATA")
//                            + "\\TrackerAgent\\TrackerAgent.exe");
//
//            if (installedExe.exists()) {
//                return installedExe.getAbsolutePath();
//            }
//
//        } catch (URISyntaxException e) {
//
//            log.error("Unable to resolve executable path", e);
//        }
//
//        return null;
//    }
    
    
    
//    private String getExePath() {
//
//        try {
//
//            File location = new File(
//                    StartupService.class
//                            .getProtectionDomain()
//                            .getCodeSource()
//                            .getLocation()
//                            .toURI());
//
//            // If running from installed app
//            File exe = new File(location.getParentFile(), "TrackerAgent.exe");
//
//            if (exe.exists()) {
//                return exe.getAbsolutePath();
//            }
//
//            // Development
//            File devExe = new File(
//                    System.getProperty("user.dir"),
//                    "TrackerAgent.exe");
//
//            if (devExe.exists()) {
//                return devExe.getAbsolutePath();
//            }
//
//            // Fallback
//            File programFilesExe =
//                    new File("C:\\Program Files\\TrackerAgent\\TrackerAgent.exe");
//
//            if (programFilesExe.exists()) {
//                return programFilesExe.getAbsolutePath();
//            }
//
//        } catch (Exception e) {
//
//            log.error("Unable to resolve executable path", e);
//        }
//
//        return null;
//    }
    
    private String getExePath() {

        // 1. Installed location (Production)
        File installed = new File(
                "C:\\Program Files\\TrackerAgent\\TrackerAgent.exe");

        if (installed.exists()) {

            log.info("Using installed EXE : {}", installed.getAbsolutePath());

            return installed.getAbsolutePath();
        }

        // 2. LOCALAPPDATA (older installs)
        File local = new File(
                System.getenv("LOCALAPPDATA")
                        + "\\TrackerAgent\\TrackerAgent.exe");

        if (local.exists()) {

            log.info("Using LocalAppData EXE : {}", local.getAbsolutePath());

            return local.getAbsolutePath();
        }

        // 3. Development (IDE)
        File dev = new File(
                System.getProperty("user.dir"),
                "TrackerAgent.exe");

        if (dev.exists()) {

            log.info("Using Development EXE : {}", dev.getAbsolutePath());

            return dev.getAbsolutePath();
        }

        log.error("TrackerAgent.exe not found.");

        return null;
    }
    
}