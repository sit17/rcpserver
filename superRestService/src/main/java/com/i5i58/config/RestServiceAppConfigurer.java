package com.i5i58.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.i5i58.security.SuperAuthorizationInterceptor;

@Configuration
public class RestServiceAppConfigurer extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		registry.addInterceptor(getSuperAuthorizationInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	@Bean
	public SuperAuthorizationInterceptor getSuperAuthorizationInterceptor() {
		return new SuperAuthorizationInterceptor();
	}
}