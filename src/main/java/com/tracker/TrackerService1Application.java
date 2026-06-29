//package com.tracker;
////
////import org.springframework.boot.CommandLineRunner;
////import org.springframework.boot.SpringApplication;
////import org.springframework.boot.autoconfigure.SpringBootApplication;
////import org.springframework.context.annotation.Bean;
////import org.springframework.scheduling.annotation.EnableScheduling;
////
////import com.tracker.service.SessionApiClient;
////
////@SpringBootApplication
////@EnableScheduling
////public class TrackerService1Application {
////
////	public static void main(String[] args) {
////		SpringApplication.run(TrackerService1Application.class, args);
////	}
////	@Bean
////	CommandLineRunner runner(
////	        SessionApiClient sessionApiClient) {
////
////	    return args -> {
////
////	        Runtime.getRuntime()
////	                .addShutdownHook(
////	                        new Thread(
////	                                sessionApiClient::endSession));
////	    };
////	}
////}
//
//import javax.swing.SwingUtilities;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//import com.tracker.ui.LoginFrame;
//
//@SpringBootApplication
//@EnableScheduling
//public class TrackerService1Application {
//
//    public static void main(String[] args) {
//
//        ConfigurableApplicationContext context =
//                SpringApplication.run(
//                        TrackerService1Application.class,
//                        args);
//
//        SwingUtilities.invokeLater(() -> {
//
//            LoginFrame frame =
//                    context.getBean(LoginFrame.class);
//
//            frame.setVisible(true);
//        });
//    }
//}


package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tracker.service.SingleInstanceService;

//import javax.swing.JOptionPane;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//import com.tracker.service.SingleInstanceService;
//
//@SpringBootApplication
//@EnableScheduling
//public class TrackerService1Application {
//
////    public static void main(String[] args) {
////
////        SpringApplication app =
////                new SpringApplication(
////                        TrackerService1Application.class);
////
////        app.setHeadless(false);
////
////        app.run(args);
////    }
//	
//	
////	public static void main(String[] args) {
////
////	    SpringApplication app =
////	            new SpringApplication(
////	                    TrackerService1Application.class);
////
////	    app.setHeadless(false);
////
////	    ConfigurableApplicationContext context =
////	            app.run(args);
////
////	    SingleInstanceService single =
////	            context.getBean(
////	                    SingleInstanceService.class);
////
////	    if (!single.lock()) {
////
////	        JOptionPane.showMessageDialog(
////	                null,
////	                "Tracker Agent is already running.");
////
////	        System.exit(0);
////	    }
////	}
//	
//	private static ConfigurableApplicationContext context;
//
//	public static void main(String[] args) {
//
//	    context = SpringApplication.run(
//	            TrackerService1Application.class,
//	            args);
//
//	    SingleInstanceService single =
//	            context.getBean(
//	                    SingleInstanceService.class);
//
//	    if (!single.lock()) {
//
//	        System.exit(0);
//	    }
//	}
//	
//	
//}



@SpringBootApplication
@EnableScheduling
public class TrackerService1Application {

    private static ConfigurableApplicationContext context;

//    public static void main(String[] args) {
//    	System.out.println(
//    		    "Headless = "
//    		    + java.awt.GraphicsEnvironment.isHeadless());
//        SpringApplication app =
//                new SpringApplication(
//                        TrackerService1Application.class);
//
//        app.setHeadless(false);
//
//        context = app.run(args);
//
//        SingleInstanceService single =
//                context.getBean(
//                        SingleInstanceService.class);
//
//        if (!single.lock()) {
//            System.exit(0);
//        }
//    }
    
    
//    public static void main(String[] args) {
//
//        SingleInstanceService single =
//                new SingleInstanceService();
//
//        if (!single.lock()) {
//            return;
//        }
//
//        SpringApplication app =
//                new SpringApplication(
//                        TrackerService1Application.class);
//
//        app.setHeadless(false);
//
//        app.run(args);
//    }
    
    
//    public static void main(String[] args) {
//
//        System.setProperty("java.awt.headless", "false");
//
//        SpringApplication app =
//                new SpringApplication(
//                        TrackerService1Application.class);
//
//        app.setHeadless(false);
//
//        ConfigurableApplicationContext context =
//                app.run(args);
//
//        SingleInstanceService single =
//                context.getBean(
//                        SingleInstanceService.class);
//
//        if (!single.lock()) {
//            System.exit(0);
//        }
//    }
    
    
    public static void main(String[] args) {

        System.setProperty("java.awt.headless", "false");

        SingleInstanceService single =
                new SingleInstanceService();

        if (!single.lock()) {

            System.out.println("Tracker already running");

            return;
        }

        SpringApplication app =
                new SpringApplication(
                        TrackerService1Application.class);

        app.setHeadless(false);

        app.run(args);
    }
    
}