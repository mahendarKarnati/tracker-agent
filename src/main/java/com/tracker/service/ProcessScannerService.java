

package com.tracker.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.ptr.IntByReference;
import com.tracker.model.RunningProcess;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessScannerService {

    @Value("${tracker.process.mode:VISIBLE}")
    private String processMode;

    private static final Set<String> IGNORED =
            Set.of(
                    "explorer.exe",
                    "TextInputHost.exe",
                    "ApplicationFrameHost.exe",
                    "RuntimeBroker.exe",
                    "SearchHost.exe",
                    "Widgets.exe",
                    "LockApp.exe",
                    "ShellHost.exe",
                    "ShellExperienceHost.exe",
                    "backgroundTaskHost.exe",
                    "CrossDeviceResume.exe",
                    "StartMenuExperienceHost.exe",
                    "msedgewebview2.exe"
            );

    public Map<Long, RunningProcess> getRunningProcesses() {

        if ("ALL".equalsIgnoreCase(processMode)) {

        	log.info("Tracking ALL Processes");

            return getAllProcesses();
        }

        log.info("Tracking Visible Applications");

        return getVisibleApplications();
    }
    
    
    private Map<Long, RunningProcess> getVisibleApplications() {

        Map<Long, RunningProcess> result = new HashMap<>();

        User32.INSTANCE.EnumWindows((hwnd, data) -> {

            // Hidden windows skip
            if (!User32.INSTANCE.IsWindowVisible(hwnd)) {
                return true;
            }

            // Window title
            char[] title = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, title, 512);

            String windowTitle = Native.toString(title);

            if (windowTitle == null || windowTitle.isBlank()) {
                return true;
            }

            // PID
            IntByReference pidRef = new IntByReference();

            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pidRef);

            long pid = pidRef.getValue();

            // Process name
            String processName =
                    ProcessHandle.of(pid)
                            .flatMap(ph -> ph.info().command())
                            .map(path -> new File(path).getName())
                            .orElse(getProcessName(pid));

            if (processName == null || processName.isBlank()) {
                return true;
            }
            
            if (processName.equalsIgnoreCase("explorer.exe")) {
                return true;
            }
            
            String command =
                    ProcessHandle.of(pid)
                            .flatMap(ph -> ph.info().command())
                            .orElse("");

            
            if (command.startsWith("C:\\Windows\\")) {
                return true;
            }

            // Skip Program Files\Common Files
            if (command.contains("\\Common Files\\")) {
                return true;
            }

            result.putIfAbsent(
                    pid,
                    new RunningProcess(
                            pid,
                            processName));
            // Skip SYSTEM account processes
            try {

                ProcessHandle.of(pid).ifPresent(ph -> {

                    ph.info().user().ifPresent(user -> {

                        if ("SYSTEM".equalsIgnoreCase(user)
                                || "LOCAL SERVICE".equalsIgnoreCase(user)
                                || "NETWORK SERVICE".equalsIgnoreCase(user)) {

                            // nothing
                        }

                    });

                });

            } catch (Exception e) {
            }

            result.putIfAbsent(
                    pid,
                    new RunningProcess(
                            pid,
                            processName));

            return true;

        }, null);

        return result;
    }
    
    
    

    /**
     * Track every running process.
     */
    private Map<Long, RunningProcess> getAllProcesses() {

        Map<Long, RunningProcess> result =
                new HashMap<>();

        ProcessHandle.allProcesses()
                .forEach(ph -> {

                    try {

                        long pid = ph.pid();

                        String processName =
                                ph.info()
                                        .command()
                                        .map(path -> new File(path).getName())
                                        .orElse(null);

                        if (processName == null || processName.isBlank()) {
                            processName = getProcessName(pid);
                        }

                        if (processName == null || processName.isBlank()) {
                            return;
                        }

                        result.put(
                                pid,
                                new RunningProcess(
                                        pid,
                                        processName));

                    } catch (Exception ignored) {
                    }

                });

        return result;
    }

    /**
     * Fallback for processes whose executable path is unavailable.
     */
    private String getProcessName(long pid) {

        try {

            Process process =
                    Runtime.getRuntime().exec(
                            "cmd /c wmic process where processid="
                                    + pid
                                    + " get name");

            try (java.util.Scanner sc =
                         new java.util.Scanner(
                                 process.getInputStream())) {

                if (sc.hasNextLine()) {
                    sc.nextLine();
                }

                if (sc.hasNextLine()) {
                    return sc.nextLine().trim();
                }
            }

        } catch (Exception ignored) {
        }

        return null;
    }
}
