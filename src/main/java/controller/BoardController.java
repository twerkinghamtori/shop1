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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	
	@GetMapping({"reply","update","delete"})
	public ModelAndView replyView(Integer num, HttpSession session) {
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
		mav.addObject("board",b);
		mav.addObject("boardName", boardName);
		return mav;
	}
	
	/*
	 1.유효성 검사하기 - 파라미터값 저장
	 	-원글정보 : num, grp, grplevel, grpstep, boardid
	 	-답글정보 : writer, pass, title, content
	 2. db에 insert => service.boardReply()
	 	-원글의 grpstep보다 큰 이미 등록된 답글의 grpstep 값을 +1 => boardDao.grpStepAdd()
	 	-num : maxNum() + 1
	 	-db에 insert => boardDao.insert()
	 	-grp : 원글과 동일
	 	-grplevel : 원글의 grplevel +1
	 	-grpstep : 원글의 grpstep +1
	 */
	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {
			Board dbBoard = service.getBoard(board.getNum());
			Map<String, Object> map = br.getModel();
			Board b = (Board)map.get("board"); //화면에서 입력받은 값을 저장한 Board 객체
			b.setTitle(dbBoard.getTitle());
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list?boardid="+board.getBoardid());
		} catch(Exception e) {
			e.printStackTrace();
			throw new LoginException("답변 등록 시 오류 발생", "reply?num="+board.getNum());
		}		
		return mav;
				
	}
	
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		if(board.getPass()==null || board.getPass().equals("")) {
			br.reject("error.required.pass");
			return mav;
		}
		Board dbboard = service.getBoard(board.getNum());
		String dbPass = dbboard.getPass();
		if(!dbPass.equals(board.getPass())) {
			br.reject("error.login.password");
			return mav;
		} else {
			try {
				service.boardDelete(board.getNum());
				mav.setViewName("redirect:list?boardid="+ dbboard.getBoardid());
			} catch(Exception e) {
				e.printStackTrace();
//				br.reject("error.board.fail");
				throw new LoginException("게시글 삭제 시 오류 발생", "delete?num="+board.getNum());				
			}			
		}
		return mav;
	}
		
	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum, HttpServletRequest request, Model model) { 
		/*
		 upload : CKEditor 모듈에서 업로드되는 이미지의 이름. 업로드되는 이미지파일의 내용. 이미지값
		 CKEditorFuncNum : CKEditor 모듈에서 파라미터로 전달되는 값. 리턴해야하는 값
		 Model : ModelAndView 를 분리. view에 전달할 데이터 정보를 저장할 객체
		 */
		String path = request.getServletContext().getRealPath("/") + "board/imgfile/";
		service.uploadFileCreate(upload, path); //upload(파일의내용), path(업로드할 폴더)
		//request.getContextPath() : 웹어플리케이션 서버 경로. 프로젝트명(웹어플리케이션서버이름) = shop1/
		//localhost:8080/shop1/board/imgfile/hamtori.jpg
		String fileName = request.getContextPath() + "/board/imgfile/" + upload.getOriginalFilename();
		model.addAttribute("fileName", fileName);
		return "ckedit"; //view이름
	}
}
