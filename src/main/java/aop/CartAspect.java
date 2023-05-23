package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component
@Aspect
public class CartAspect {
	//pointcut 설정 : CartController에 check로 시작하는 메서드. 매개변수의 마지막이 HttpSession으로 끝나는 메서드
	@Before("execution(* controller.Cart*.check*(..)) && args(..,session)")
	public void cartCheck(HttpSession session) throws Throwable { //args에 의해서 매개변수로 전달받은 session 
		//로그인 정보 검증
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("로그인이 필요한 서비스입니다.", "../user/login");
		}
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart==null || cart.getItemSetList().size() == 0) {
			throw new CartEmptyException("장바구니에 상품이 존재하지 않습니다.", "../item/list");
		}
	}
}
