package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.UserDao;

@Service //@Component + Service(controller기능과 dao 기능의 중간 역할 기능)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private UserDao userDao;
	
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
}
