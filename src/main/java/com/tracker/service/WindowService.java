package com.tracker.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import com.tracker.model.RunningProcess;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WindowService {

    public String getActiveWindowTitle() {

        char[] windowText = new char[512];

        HWND hwnd = User32.INSTANCE.GetForegroundWindow();

        User32.INSTANCE.GetWindowText(
                hwnd,
                windowText,
                512);

        return Native.toString(windowText);
    }
    
    public RunningProcess getActiveApplication() {

        char[] windowText = new char[512];

        HWND hwnd = User32.INSTANCE.GetForegroundWindow();

        if (hwnd == null) {
            return null;
        }

        User32.INSTANCE.GetWindowText(
                hwnd,
                windowText,
                512);

        String title =
                Native.toString(windowText);

        IntByReference pidRef =
                new IntByReference();

        User32.INSTANCE.GetWindowThreadProcessId(
                hwnd,
                pidRef);

        long pid =
                pidRef.getValue();

        String processName =
                ProcessHandle.of(pid)
                        .flatMap(ph -> ph.info().command())
                        .map(path -> new File(path).getName())
                        .orElse("Unknown");

        return RunningProcess.builder()
                .pid(pid)
                .processName(processName)
//                .windowTitle(title)
                .build();
    }
}