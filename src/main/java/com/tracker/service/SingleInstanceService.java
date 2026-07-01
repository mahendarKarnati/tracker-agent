
package com.tracker.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import org.springframework.stereotype.Service;

@Service
public class SingleInstanceService {
	
	private RandomAccessFile raf;

    private FileLock lock;

    public boolean lock() {

        try {

            File file =
                    new File(
                            System.getProperty("user.home"),
                            ".tracker.lock");

            raf =
                    new RandomAccessFile(file, "rw");

            lock =
                    raf.getChannel().tryLock();
            System.out.println("LOCK FILE = " + file.getAbsolutePath());

            System.out.println("LOCK RESULT = " + (lock != null));

            System.out.println("PID = " + ProcessHandle.current().pid());

            return lock != null;

        } catch (Exception e) {

            return false;
        }
    }
}
