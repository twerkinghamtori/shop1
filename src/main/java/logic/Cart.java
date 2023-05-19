package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	
	public void push(ItemSet itemSet) {
		int count = itemSet.getQuantity();
		for(ItemSet old : itemSetList) {
			if(itemSet.getItem().getId() == old.getItem().getId()) {
				count = old.getQuantity() + itemSet.getQuantity();
				old.setQuantity(count);
				return;
			}
		}
		itemSetList.add(itemSet);
	}
	
	public int getTotal() {
		int sum = 0;
		for(ItemSet s : itemSetList) {
			sum += s.getItem().getPrice() * s.getQuantity();
		}
		return sum;
	}
}
