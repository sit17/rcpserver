package com.i5i58.utils;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Aspect // 定义一个切面
//@Configuration
/**
 * 此方法已无效，采用spring拦截器+@annotation方式处理身份认证
 * @author frank
 *
 */
@Deprecated
public class TokenVirify {

	//@Pointcut("execution(* com.i5i58..*(..))")
	public void doMethod() {
	}// 定义一个切入点
	/*
	 * @Before("execution(* com.i5i58..*.*(..))") public void
	 * doBeforeInServiceLayer(JoinPoint joinPoint) { startTimeMillis =
	 * System.currentTimeMillis(); // 记录方法开始执行的时间 System.out.println("@Before");
	 * }
	 * 
	 * @After("execution(* com.i5i58..*.*(..))") public Object
	 * doAfterInServiceLayer(JoinPoint joinPoint) { endTimeMillis =
	 * System.currentTimeMillis(); // 记录方法执行完成的时间 System.out.println("@After");
	 * return null; }
	 */

	//@Around("execution(* com.i5i58.service..*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("@Around");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String url = request.getRequestURL().toString();
		System.out.println(url);
		
		return pjp.proceed();
	}
}