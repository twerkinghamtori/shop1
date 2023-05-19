package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.ShopService;

@Controller
@RequestMapping("cart")
public class CartController {
	
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView cardAdd(Integer id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);		
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart==null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSet(item, quantity));
		mav.addObject("message", item.getName() + " : " + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public ModelAndView cartDelete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		List<ItemSet> cartList = cart.getItemSetList();		
		ItemSet is = cartList.get(index);
		cartList.remove(index);		
		mav.addObject("message", is.getItem().getName() + " 장바구니에서 삭제");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartView")
	public ModelAndView cartView(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart==null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		mav.addObject("cart", cart);
		return mav;
	}
}
