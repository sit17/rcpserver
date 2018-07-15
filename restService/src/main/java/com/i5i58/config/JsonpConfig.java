package com.i5i58.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@Configuration
public class JsonpConfig {

	@ControllerAdvice(basePackages = "com.i5i58.service")
	public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
		public JsonpAdvice() {
			super("callback", "jsonp"); // 指定jsonpParameterNames
		}
	}
}
