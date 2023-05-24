package controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Sale;
import logic.ShopService;
import logic.User;


@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*") //controller에서 설정되지 않은 모든 요청시 호출되는 메서드
	public ModelAndView join() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User()); // modelAttribute="user" => User객체 생성
		return mav;
	}
	
	@PostMapping("join")
	public ModelAndView userAdd(@Valid User user, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		//input check
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			br.reject("error.input.user"); ///reject(에러코드) 메서드 => global error에 추가
			br.reject("error.input.check");
			return mav;
		}
		//회원가입 => db의 useraccount table에 저장
		try {
			service.userinsert(user);
			mav.addObject("user",user);
		} catch(DataIntegrityViolationException e) { //중복키 error
			e.printStackTrace();
			br.reject("error.duplicate.user"); //global 오류 등록
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;
	}
	
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult br, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			br.reject("error.login.input");
			return mav;
		}
		User dbUser = null;
		//userid에 맞는 User를 db에서 조회
		try {
			dbUser = service.selectUserOne(user.getUserid());
		} catch(EmptyResultDataAccessException e) { //spring jdbc template에서만 나는 예외. 조회된 데이터가 없음.
			e.printStackTrace();
			br.reject("error.login.id");
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		//비밀번호 검증
		if(user.getPassword().equals(dbUser.getPassword())) {
			session.setAttribute("loginUser", dbUser);
			mav.addObject("userid",dbUser.getUserid());
			mav.setViewName("redirect:mypage");
		} else {
			br.reject("error.login.password");
			mav.getModel().putAll(br.getModel());
		}		
		return mav;
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<Sale> saleList = service.selectSaleList(userid);
		User user = service.selectUserOne(userid);
		mav.addObject("saleList", saleList);
		mav.addObject("user",user);
		return mav;
	}
	
	@GetMapping({"update", "delete"})
	public ModelAndView idCheckUpdateGet(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUserOne(userid);
		mav.addObject("user",user);
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView idCheckUpdatePost(@Valid User user, BindingResult br, String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User sessionUser = (User)session.getAttribute("loginUser");
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			br.reject("error.input.check");
			return mav;
		} 
		if(!user.getPassword().equals(sessionUser.getPassword())) {
			mav.getModel().putAll(br.getModel());
			br.reject("error.login.password");			
			return mav;
		}
		try {
			service.userUpdate(user);
			if(!sessionUser.getUserid().equals("admin")) session.setAttribute("loginUser", user);
			mav.addObject("user",user);		
		} catch(DataIntegrityViolationException e) { 
			e.printStackTrace();
			br.reject("error.duplicate.user"); 
			mav.getModel().putAll(br.getModel());
			return mav;
		}	
		mav.setViewName("redirect:mypage?userid="+userid);
		return mav;
	}
	
	@PostMapping("delete")
	public String idCheckDeletePost(String password, String userid, HttpSession session) { //throw new Exception 말고..해볼까..
		User sessionUser = (User)session.getAttribute("loginUser");
		if(userid.equals("admin")) {
			throw new LoginException("관리자는 탈퇴가 불가합니다.", "mypage?userid="+userid);
		}
		if(!password.equals(sessionUser.getPassword())) {
			throw new LoginException("비밀번호가 틀립니다.", "delete?userid="+userid);
		}
		try {
			service.userDelete(userid);
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new LoginException("주문내역이 있는 회원은 탈퇴할 수 없습니다. 관리자에게 문의하세요.", "mypage?userid="+userid);
		} catch(Exception e) {
			e.printStackTrace();
			throw new LoginException("탈퇴시 오류 발생", "delete?userid="+userid);
		}
		if(sessionUser.getUserid().equals("admin")) {
			return "redirect:../admin/list";
		} else {
			session.invalidate();
			return "redirect:login";
		}
	}
}
