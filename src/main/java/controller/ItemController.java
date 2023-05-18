package controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

@Controller //@Component(객체화) + Controller 기능
@RequestMapping("item") //http://localhost:8080/shop1/item/*
public class ItemController {
	@Autowired
	private ShopService service;
	
	//http://localhost:8080/shop1/item/list
	@RequestMapping("list")
	public ModelAndView list() {
		//ModelAndView : Model과 View를 연결시켜놓은 객체. view에 전송할 데이터 + view 이름 설정
		//view 설정이 안된 경우(ModelAndView()) : url(item/list)과 동일
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList();
		mav.addObject("itemList",itemList); //데이터 저장(setAttribute?). view:item/list 인 mav.
		return mav;
	}
	
	@RequestMapping("detail")
	public ModelAndView detail(Integer id) { //파라미터(id)를 받는 방법 <= 매개변수 활용. 파라미터 이름과 매개변수명이 같아야함!
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	
	@RequestMapping("register")
	public ModelAndView register(@Valid Item item, BindingResult bresult) {
		//item의 프로퍼티와 파라미터 값을 비교하여 같은 이름의 값을 item 객체에 저장
		//@Valid : item 객체ㅔ 입력된 내용 유효성 검사 => 결과를 BindingResult객체로 전달
		ModelAndView mav = new ModelAndView("item/create"); //view 이름 직접설정
		if(bresult.hasErrors()) { //Return if there were "any" errors (at least one error exists)
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		return mav;
	}
}
