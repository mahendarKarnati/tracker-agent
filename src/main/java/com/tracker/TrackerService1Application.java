package com.tracker;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tracker.api.SessionApiClient;
import com.tracker.service.SingleInstanceService;

@SpringBootApplication
@EnableScheduling
public class TrackerService1Application {

    public static void main(String[] args) {

        System.out.println("PID = " + ProcessHandle.current().pid());

        System.setProperty("java.awt.headless", "false");

        File logDir = new File(
                System.getenv("APPDATA"),
                "TrackerAgent\\logs");

        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        SingleInstanceService single = new SingleInstanceService();

        if (!single.lock()) {

            System.out.println("Tracker Agent is already running.");

            return;
        }

        SpringApplication app =
                new SpringApplication(
                        TrackerService1Application.class);

        app.setHeadless(false);

        ConfigurableApplicationContext context =
                app.run(args);

        SessionApiClient sessionApiClient =
                context.getBean(SessionApiClient.class);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {

                    System.out.println("Shutdown Hook Called");

                    try {

                        sessionApiClient.endSession();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }));
    }
}