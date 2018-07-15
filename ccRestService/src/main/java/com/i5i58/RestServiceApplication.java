package com.i5i58;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class RestServiceApplication implements EmbeddedServletContainerCustomizer {

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:8082/swagger-ui.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8084);

	}
}
