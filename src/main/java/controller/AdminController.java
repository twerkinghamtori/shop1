package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
/*모든 메서드는 관리자로 로그인했을 때만 실행 => AOP */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ShopService service;
}
