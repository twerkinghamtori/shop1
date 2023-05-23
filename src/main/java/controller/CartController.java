package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

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
	
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
//		Cart cart = (Cart)session.getAttribute("CART");
//		if(cart == null || cart.getItemSetList().size() == 0) {
//			throw new CartEmptyException("장바구니에 상품이 없습니다.", "../item/list");
//		}
//		User loginUser = (User)session.getAttribute("loginUser");
//		if(loginUser == null) {
//			throw new LoginException("로그인이 필요한 서비스입니다.", "../user/login");
//		}
		return null;
	}	
	
	@RequestMapping("end") //주문확정
	public ModelAndView checkend(HttpSession session) {
		//1. 로그인, 장바구니 상품 존재 => AOP로 설정. (checkout이랑 같으니까)
		//2. 장바구니 상품을 saleitem 테이블에 등록. 주문정보(sale) 등록 =>service.checkend
		//3. 장바구니 상품 삭제 =>session.remove..
		//4. end.jsp 페이지에서 sale, saleitem 데이터를 출력
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		Sale sale = service.checkend(loginUser, cart);
		session.removeAttribute("CART");
		mav.addObject("sale",sale);
		return mav;
	}
} 
