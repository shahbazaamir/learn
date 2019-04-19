package hello;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class UserAspect {

	@Before("execution(  *  hello.HelloController.index())")
	public void before(JoinPoint joinPoint){
		 
		System.out.println( " pointcut expression matched  ");
		System.out.println( " performing advice  "+ joinPoint);
	}
}
