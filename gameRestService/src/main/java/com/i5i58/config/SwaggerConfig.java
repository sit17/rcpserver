package com.i5i58.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** * SwaggerConfig */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /** * SpringBoot默认已经将classpath:/META-INF/resources/和classpath:/META-INF/resources/webjars/映射 * 所以该方法不需要重写，如果在SpringMVC中，可能需要重写定义（我没有尝试） * 重写该方法需要 extends WebMvcConfigurerAdapter * */
// @Override
// public void addResourceHandlers(ResourceHandlerRegistry registry) {
// registry.addResourceHandler("swagger-ui.html")
// .addResourceLocations("classpath:/META-INF/resources/");
//
// registry.addResourceHandler("/webjars/**")
// .addResourceLocations("classpath:/META-INF/resources/webjars/");
// }

    /** * 可以定义多个组，比如本类中定义把test和demo区分开了 * （访问页面就可以看到效果了） * */
@SuppressWarnings("unchecked")
	/*
    @Bean
    public Docket testApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("test")
                .genericModelSubstitutes(DeferredResult.class)
// .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(or(regex("/api/.*")))//过滤的接口
                .build()
                .apiInfo(testApiInfo());
    }
*/
    @Bean
    public Docket i5i58Api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("i5i58 Game")
                .genericModelSubstitutes(DeferredResult.class)
// .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(or(regex("/.*")))//过滤的接口
                .build()
                .apiInfo(i5i58ApiInfo());
    }
    
    private ApiInfo i5i58ApiInfo() {
        ApiInfo apiInfo = new ApiInfo("i5i58 Game API",//大标题
                "i5i58 Game's REST API, for frank",//小标题
                "1.0",//版本
                "NO terms of service",
                "frank@i5i58.com",//作者
                "www.i5i58.com License, Version 1.0",//链接显示文字
                "http://www.i5i58.com"//网站链接
        );

        return apiInfo;
    }
}