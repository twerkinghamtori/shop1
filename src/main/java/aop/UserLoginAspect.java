package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
public class UserLoginAspect {
	//@Around(..)
	@Before("execution(* controller.User*.idCheck*(..)) && args(..,userid,session)")
	//public Object userIdCheck(ProceedingJoinPoint joinPoint, ..)
	public void userIdCheck(String userid, HttpSession session) throws Throwable {
		User sessionUser = (User)session.getAttribute("loginUser");
		if(sessionUser == null) {
			throw new LoginException("로그인이 필요한 서비스입니다.", "login"); //UserController 밑에 있으니까 모든 요청은 user/로 들어오기 때문에 그냥 login 써도 됨
		} else if(!sessionUser.getUserid().equals("admin") && !sessionUser.getUserid().equals(userid)) {
			throw new LoginException("본인만 접근 가능합니다.", "../item/list");
		}
		//return ..joinPoint..;
	}
	
	@Before("execution(* controller.User*.loginCheck*(..)) && args(.., session)")
	public void loginCheck(HttpSession session) throws Throwable {
		User sessionUser = (User)session.getAttribute("loginUser");
		if(sessionUser == null) {
			throw new LoginException("로그인이 필요한 서비스입니다.", "login"); 
		} 
	}
}
