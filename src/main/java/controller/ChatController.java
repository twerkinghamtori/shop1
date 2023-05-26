package controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {
	
	@RequestMapping("chat")
	public String chat() {
		return null;
	}
}
