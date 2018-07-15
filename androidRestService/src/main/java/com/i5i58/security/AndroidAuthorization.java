package com.i5i58.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * token 认证注入
 * @author frank
 *
 */
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface AndroidAuthorization {

}
