package logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.UserDao;
import dao.SaleDao;
import dao.SaleItemDao;

@Service //@Component + Service(controller기능과 dao 기능의 중간 역할 기능)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SaleDao saleDao;
	
	@Autowired
	private SaleItemDao saleItemDao;
	
	@Autowired
	private BoardDao boardDao;
	
	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/") + "img/"; 
			System.out.println(path);
			uploadFileCreate(item.getPicture(), path);
			item.setPictureUrl(item.getPicture().getOriginalFilename()); //pictureUrl(String)에 파일이름 저장.
		}
		int maxid = itemDao.maxId();
		item.setId(maxid+1);
		itemDao.insert(item);
	}
	
	//파일 업로드
	private void uploadFileCreate(MultipartFile file, String path) {
		//file : 파일의 내용, path : 업로드할 폴더
		String orgFile = file.getOriginalFilename(); //파일 이름
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		try {
			//file에 저장된 내용을 ....img/파일이름으로 바꿈. => 저장한다는 의미
			file.transferTo(new File(path+orgFile)); //파일 저장
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void itemUPdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/") + "img/"; 
//			System.out.println(path);
			uploadFileCreate(item.getPicture(), path);
			item.setPictureUrl(item.getPicture().getOriginalFilename()); //pictureUrl(String)에 파일이름 저장.
		}
		itemDao.update(item);
	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);
	}

	public void userinsert(User user) {
		userDao.userinsert(user);
	}

	public User selectUserOne(String userid) {
		return userDao.selectUserOne(userid);
	}

	// 1. sale테이블과 saleitem테이블에 등록
	// 2. 결과를 sale 객체에 저장
	public Sale checkend(User loginUser, Cart cart) {
		int maxSaleId = saleDao.getMaxSaleId();
		Sale sale = new Sale();
		sale.setSaleid(maxSaleId+1);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		int seq = 0;
		for(ItemSet is : cart.getItemSetList()) {
			SaleItem saleItem = new SaleItem(sale.getSaleid(), ++seq, is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale; //주문정보, 주문상품정보, 상품정보, 사용자정보
	}

	public List<Sale> selectSaleList(String userid) {
		List<Sale> saleList = saleDao.selectSaleList(userid);
		for(Sale s : saleList) {
			List<SaleItem> saleItemList = saleItemDao.selectSaleItemList(s.getSaleid());			
			for(SaleItem si : saleItemList) {
				si.setItem(itemDao.selectItem(si.getItemid()));
			}
			s.setItemList(saleItemList);
		}				
		return saleList;
	}

	public void userUpdate(User user) {
		userDao.userUpdate(user);
	}

	public void userDelete(String userid) {
		userDao.userDelete(userid);
	}

	public User changePass(String chgpass, String userid) {
		userDao.changePass(chgpass, userid);
		User user = userDao.selectUserOne(userid);
		return user;
	}

	public List<User> selectUserAll() {
		return userDao.selectUserAll();
	}

//	public List<User> getUserList(String[] idchks) {
//		List<User> list = new ArrayList<>();
//		for(String s : idchks) {
//			list.add(userDao.selectUserOne(s));
//		}
//		return list;
//	}
	
	public List<User> getUserList(String[] idchks) {
		return userDao.list(idchks);
	}

	public String getSearch(User user) {
		String result = "";
		if(user.getUserid() == null) {
			result = userDao.searchId(user.getEmail(), user.getPhoneno());
		} else {
			result = userDao.searchPw(user.getUserid(), user.getEmail(), user.getPhoneno());
		}
		return result;
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		int maxnum = boardDao.maxnum();
		board.setNum(++maxnum);
		board.setGrp(maxnum);
		
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/") + "board/file/"; 
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename()); 
		}		
		boardDao.insert(board);
	}

	public int boardCount(String boardid, String searchType, String searchContent) {
		return boardDao.count(boardid, searchType, searchContent);
	}

	public List<Board> boardList(Integer pageNum, int limit, String boardid, String searchType, String searchContent) {
		return boardDao.list(pageNum, limit, boardid, searchType, searchContent);
	}

	public Board getBoard(int num) {
		return boardDao.selectOne(num);
	}

	public void addReadCnt(int num) {
		boardDao.addReadCnt(num);
	}
	
	@Transactional //보통 service에서 @Transactional 처리. 업무를 원자화(All or Nothing).
	public void boardReply(Board b) {
		boardDao.grpStepAdd(b); //이미 등록된 grpstep값 1씩 증가
		int maxnum = boardDao.maxnum();
		b.setNum(++maxnum);
		b.setGrplevel(b.getGrplevel() + 1);
		b.setGrpstep(b.getGrpstep()+1);		
		boardDao.insert(b);		
	}
}
