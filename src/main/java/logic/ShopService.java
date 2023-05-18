package logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ItemDao;

@Service //@Component + Service(controller기능과 dao 기능의 중간 역할 기능)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	
	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}
}
