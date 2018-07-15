package com.i5i58;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"com.i5i58"})
@EnableAutoConfiguration
public class GameRestServiceApplication implements EmbeddedServletContainerCustomizer {
	
	@Value("${rpc.controller.version}")
	String controllerVersion="";
	static int port;
	public static void main(String[] args) {
		SpringApplication.run(GameRestServiceApplication.class, args);
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:"+port+"/swagger-ui.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (controllerVersion != null && controllerVersion.equals("/v1")){
			port = 5980;
		}else{			
			port = 6980;
		}
		container.setPort(port);
	}
}
