package com.tracker.service;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.springframework.stereotype.Service;

@Service
public class AppMonitorService {

	public String getActiveWindowTitle() {

	    char[] windowText = new char[512];

	    HWND hwnd = User32.INSTANCE.GetForegroundWindow();

	    User32.INSTANCE.GetWindowText(hwnd, windowText, 512);

	    return Native.toString(windowText);
	}
}