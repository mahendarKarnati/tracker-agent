package com.tracker.service;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;

import org.springframework.stereotype.Service;

@Service
public class IdleTimeService {

	public long getIdleTimeSeconds() {

	    WinUser.LASTINPUTINFO info =
	            new WinUser.LASTINPUTINFO();

	    info.cbSize = info.size();

	    User32.INSTANCE.GetLastInputInfo(info);

	    long tickCount =
	            Kernel32.INSTANCE.GetTickCount();

	    return (tickCount - info.dwTime) / 1000;
	}
}