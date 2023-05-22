package exception;

public class CartEmptyException extends RuntimeException { //RuntimeException => 예외처리 생략 가능 예외 클래스
	private String url;
	
	public CartEmptyException(String msg, String url) {
		super(msg); //getMessage() 메서드로 조회 가능
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
}
 