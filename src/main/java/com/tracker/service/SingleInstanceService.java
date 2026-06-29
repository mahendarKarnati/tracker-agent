
package com.tracker.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import org.springframework.stereotype.Service;

@Service
public class SingleInstanceService {

    private FileLock lock;

    public boolean lock() {

        try {

            File file =
                    new File(
                            System.getProperty("user.home"),
                            ".tracker.lock");

            RandomAccessFile raf =
                    new RandomAccessFile(file, "rw");

            lock =
                    raf.getChannel().tryLock();

            return lock != null;

        } catch (Exception e) {

            return false;
        }
    }
}
