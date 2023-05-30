package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public ModelAndView writeForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}
	/*
	 1.유효성검증
	 2.db의 board 테이블에 내용 저장, 파일업로드
	 3.등록 성공 : list / 실패 : redirect:write
	 */
	@PostMapping("write")
	public ModelAndView write(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { 
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid==null) boardid="1";
		request.getSession().setAttribute("boardid", boardid);
		board.setBoardid(boardid);
		service.boardWrite(board, request);
		mav.setViewName("redirect:list?boardid="+boardid);
		return mav;
	}
	
	/*
	 @RequestParam : 파라미터값을 맵핑하여 저장해주는 Map 객체.
	 	파라미터값 저장하는 방식
	 		1. 파라미터 이름과 매개변수 이름이 같은 경우
	 		2. Bean 클래스 객체의 프로퍼티 이름과 파라미터 이름이 같은 경우
	 		3. Map객체를 이용하는 경우	 
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String, String> param, HttpSession session) {
		Integer pageNum = null;
		if(param.get("pageNum") != null) pageNum = Integer.parseInt(param.get("pageNum"));
		String boardid = param.get("boardid");
		String searchType = param.get("searchType");
		String searchContent = param.get("searchContent");
		ModelAndView mav = new ModelAndView();		
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(boardid == null || boardid.equals("")) {
			boardid = "1";
		}
		if(searchType == null) searchType="";
		if(searchContent == null) searchContent="";
		session.setAttribute("boardid", boardid);
		String boardName = null;
		switch(boardid) {
			case "1" : boardName = "공지사항"; break;
			case "2" : boardName = "자유게시판"; break;
			case "3" : boardName = "QnA"; break;
		}
		int limit=10;
		int listCount = service.boardCount(boardid, searchType, searchContent); 
		List<Board> boardList = service.boardList(pageNum, limit, boardid, searchType, searchContent);
		
		int maxpage = (int)((double)listCount/limit + 0.95);
		int startpage = ((int)(pageNum/10.0 + 0.9) -1)*10 +1; 
		int endpage = startpage + 9; 
		if(endpage > maxpage) endpage = maxpage;
		int boardNo = listCount - (pageNum -1)*limit;
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		mav.addObject("boardid",boardid);
		mav.addObject("boardName",boardName);
		mav.addObject("pageNum",pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listCount",listCount);
		mav.addObject("boardList",boardList);
		mav.addObject("boardNo",boardNo);
		mav.addObject("today",today);
		return mav;
	}
	
	@RequestMapping("detail")
	public ModelAndView detail(int num, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Board b = null;
		try {
			b = service.getBoard(num);
		} catch(EmptyResultDataAccessException e) {
			throw new LoginException("존재하지 않는 게시물입니다.", "../board/list");
		}
		
		String boardid = b.getBoardid();
		if(b.getBoardid() == null || b.getBoardid().equals("")) boardid = "1";
		String boardName = null;
		switch(boardid) {
			case "1" : boardName = "공지사항"; break;
			case "2" : boardName = "자유게시판"; break;
			case "3" : boardName = "QnA"; break;
		}		
		
		service.addReadCnt(num);
		mav.addObject("board",b);
		mav.addObject("boardName",boardName);
		return mav;
	}
}
