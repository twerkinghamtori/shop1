package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
public class AdminLoginAspect {
	@Before("execution(* controller.AdminController.*(..)) && args(..,session))")
	public void adminCheck(HttpSession session) throws Throwable {
		User sessionUser = (User)session.getAttribute("loginUser");
		if(sessionUser == null) {
			throw new LoginException("관리자 아이디로 로그인하세요." , "../user/login");
		} else if(!sessionUser.getUserid().equals("admin")) {
			throw new LoginException("관리자만 접속 가능합니다.", "../user/mypage?userid="+sessionUser.getUserid());
		}
	}	
}
