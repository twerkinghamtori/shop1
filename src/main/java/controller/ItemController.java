package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

//POJO 방식 : 순수자바소스. 다른 클래스(인터페이스)와 연관이 없음(상속x)
@Controller //@Component(객체화) + Controller 기능
@RequestMapping("item") //http://localhost:8080/shop1/item/*
public class ItemController {
	@Autowired
	private ShopService service;
	
	//http://localhost:8080/shop1/item/list
	@RequestMapping("list") //get, post 방식에 상관 없이 호출
	public ModelAndView list() {
		//ModelAndView : Model과 View를 연결시켜놓은 객체. view에 전송할 데이터 + view 이름 설정
		//view 설정이 안된 경우(ModelAndView()) : url(item/list)과 동일
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList();
		mav.addObject("itemList",itemList); //데이터 저장(setAttribute?). view:item/list 인 mav.
		return mav;
	}
	
	@GetMapping({"detail", "update", "delete"}) //detail과 update가 똑같은 것을 처리
	public ModelAndView detail(Integer id) { //파라미터(id)를 받는 방법 <= 매개변수 활용. 파라미터 이름과 매개변수명이 같아야함!
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	@GetMapping("create") //GET 방식 요청 => 이 메서드를 호출
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item()); //view의 modelAttribute에 item객체 전달
		return mav;
	}
	
	@PostMapping("create") //POST 방식 요청
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) { //매개변수에 요청객체(request) 설정 가능(주입).
		//item의 프로퍼티와 파라미터 값을 비교하여 같은 이름의 값을 item 객체에 저장
		//@Valid : item 객체ㅔ 입력된 내용 유효성 검사 => 결과를 BindingResult객체로 전달
		ModelAndView mav = new ModelAndView("item/create"); //view 이름 직접설정		
		//유효성 검증
		if(bresult.hasErrors()) { //Return if there were "any" errors (at least one error exists)
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//검증 통과
		service.itemCreate(item, request); //db추가, 이미지업로드
		mav.setViewName("redirect:list");
		return mav;
	}
	
//	@GetMapping("update") 
//	public ModelAndView update(Integer id) {
//		ModelAndView mav = new ModelAndView();
//		Item item = service.getItem(id);
//		mav.addObject("item",item);
//		return mav;
//	}
	
	@PostMapping("update")
	public ModelAndView updateRegister(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/update"); 	
		if(bresult.hasErrors()) { 
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}

		service.itemUPdate(item, request); //db추가, 이미지업로드
		mav.setViewName("redirect:list");
		return mav;
	}
	
	@PostMapping("delete")
	public ModelAndView delete(Integer id) {
		ModelAndView mav = new ModelAndView();
		service.itemDelete(id);
		mav.setViewName("redirect:list");
		return mav;
	}
}
