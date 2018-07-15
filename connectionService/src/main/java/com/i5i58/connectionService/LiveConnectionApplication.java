package com.i5i58.connectionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveConnectionApplication {
    public static void main(String[] args) throws Exception {
    	//ConfigurableApplicationContext context = SpringApplication.run(LiveConnectionApplication.class, args);    	
    	SpringApplication.run(LiveConnectionApplication.class, args);
    	
//    	Thread thread = new Thread(new Runnable() {
//			
//			public void run() {
//				while (true) {
//					System.out.println("thread is running");
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//			}
//		});
//    	
//    	
//    	thread.start();
    }
	
}
