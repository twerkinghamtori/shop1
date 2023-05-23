package logic;

public class SaleItem {
	private int saleid; //주문번호
	private int seq;   //주문상품번호
	private int itemid; //상품아이디
	private int quantity;
	
	private Item item;
	
	public SaleItem() {}
	
	public SaleItem(int saleid, int seq, ItemSet itemSet) {
		this.saleid = saleid;
		this.seq = seq;		
		this.itemid = itemSet.getItem().getId();
		this.quantity = itemSet.getQuantity();
		this.item = itemSet.getItem();
	}

	public int getSaleid() {
		return saleid;
	}

	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "SaleItem [saleid=" + saleid + ", seq=" + seq + ", itemid=" + itemid + ", quantity=" + quantity
				+ ", item=" + item + "]";
	}
	
	
}
