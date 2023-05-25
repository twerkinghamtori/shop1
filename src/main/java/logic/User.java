package logic;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class User {
	@Size(min=3, max=10, message="아이디는 3자이상 10자 이하로 입력하세요.")
	private String userid;
//	@Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$",
//			message="비밀번호는 최소 8 자 및 최대 10 자, 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자를 포함하세요.")
	@Size(min=3, max=10, message="비밀번호는 3자이상 10자 이하로 입력하세요.")
	private String password;
	@NotEmpty(message="사용자이름은 필수입니다.")
	private String username;
	private String phoneno;
	private String postcode;
	private String address;
	@NotEmpty(message="이메일을 입력하세요.")
	@Email(message="이메일 형식으로 입력하세요.")
	private String email;
	@Past(message="생일은 과거 날짜만 가능합니다.")
	//입력받은 형식을 pattern에 맞춰서 Date타입으로 변환
	@DateTimeFormat(pattern="yyyy-MM-dd") //String => Date
	@NotNull(message="생일을 입력하세요.") //NotEmpty : null이거나 "빈문자열", NotNull: null이 아닌
	private Date birthday;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@Override
	public String toString() {
		return "User [userid=" + userid + ", password=" + password + ", username=" + username + ", phoneno=" + phoneno
				+ ", postcode=" + postcode + ", address=" + address + ", email=" + email + ", birthday=" + birthday
				+ "]";
	}	
}
