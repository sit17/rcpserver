package com.i5i58;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages={"com.i5i58"})
@EnableScheduling
public class BroadcastService {
	public static void main(String[] args) throws IOException, TimeoutException {
		SpringApplication.run(BroadcastService.class, args);
	}
}
