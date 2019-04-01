package hello;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class UserAspect {
//private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//What kind of method calls I would intercept
	//execution(* PACKAGE.*.*(..))
	//Weaving & Weaver
	@Before("execution(  *  hello.HelloController.index())")
	public void before(JoinPoint joinPoint){
		//Advice
		System.out.println( " Check for user access ");
		System.out.println( " Allowed execution for {}"+ joinPoint);
	}
}
