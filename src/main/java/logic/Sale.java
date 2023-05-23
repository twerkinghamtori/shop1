package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
	private int saleid;
	private String userid;
	private Date saledate;
	
	private User user;
	
	private List<SaleItem> itemList = new ArrayList<>(); //주문 상품 목록
	
	public int getTotal() {
		int sum = 0;
//		for(SaleItem s : itemList) {
//			sum += s.getItem().getPrice() * s.getQuantity();
//		}
//		return sum;
		return itemList.stream().mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum();
	}
	
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getSaledate() {
		return saledate;
	}
	public void setSaledate(Date saledate) {
		this.saledate = saledate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SaleItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItem> itemList) {
		this.itemList = itemList;
	}
	
	@Override
	public String toString() {
		return "Sale [saleid=" + saleid + ", userid=" + userid + ", saledate=" + saledate + ", user=" + user
				+ ", itemList=" + itemList + "]";
	}
	
	
}	
