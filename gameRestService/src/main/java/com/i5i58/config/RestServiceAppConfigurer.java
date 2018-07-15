package com.i5i58.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.i5i58.util.web.AuthorizationInterceptor;
import com.i5i58.util.web.DeviceInterceptor;
import com.i5i58.util.web.JWTAuthorizationInterceptor;


@Configuration
public class RestServiceAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截		
    	registry.addInterceptor(getAuthorizationInterceptor()).addPathPatterns("/**").excludePathPatterns("/v1/**");
		registry.addInterceptor(getJWTAuthorizationInterceptor()).addPathPatterns("/v1/**");
		registry.addInterceptor(getDeviceInterceptor()).addPathPatterns("/v1/**");
        super.addInterceptors(registry);
    }
    
	@Bean
	public AuthorizationInterceptor getAuthorizationInterceptor() {
		return new AuthorizationInterceptor();
	}
	
	@Bean
	public JWTAuthorizationInterceptor getJWTAuthorizationInterceptor() {
		return new JWTAuthorizationInterceptor();
	}

	@Bean
	public DeviceInterceptor getDeviceInterceptor() {
		return new DeviceInterceptor();
	}
}