package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
 @Controller
 	호출되는 메서드의 리턴타입 : ModelAndView : 뷰이름 + 데이터.
 	호출되는 메서드의 리턴타입 : String : 뷰이름.
 @RestController
 	@Controller의 하위 인터페이스
 	Spring 4.0이후에 추가(이전에는 @ResponseBody)
 	기능 : @Controller + Component + 클라이언트에 데이터 직접 전달
 	호출되는 메서드의 리턴타입 : String : 클라이언트에 전달되는 문자열 값.
 	호출되는 메서드의 리턴타입 : Object : 클라이언트에 전달되는 값.(JSON 형태)
*/
@RestController
@RequestMapping("ajax")
public class AjaxController {
	@RequestMapping("select")
	public List<String> select(String si, String gu, HttpServletRequest request) {
		BufferedReader br = null;
		String path = request.getServletContext().getRealPath("/") + "file/sido.txt";
		try {
			br = new BufferedReader(new FileReader(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>(); //LinkedHash : 순서유지
		String data = null;
		if(si==null && gu==null) {
			try {
				while((data=br.readLine()) != null) {
					String[] arr = data.split("\\s+"); // \\s+ 공백(\\s)1개 이상(+)
					if(arr.length >= 2 ) set.add(arr[0].trim());
//					set.add(arr[0].trim());
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(gu==null) {
			si = si.trim();
			try {
				while((data=br.readLine())!= null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0])) set.add(arr[1].trim()); 
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(si != null && gu != null) {
			si = si.trim();
			gu = gu.trim();
			try {
				while((data=br.readLine())!= null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 
							&& arr[0].equals(si) && arr[1].equals(gu) 
							&& !arr[1].contains(arr[0]) && !arr[2].contains(arr[1])) {
						if(arr.length > 3) {
							if(arr[3].contains(arr[1])) continue;
							arr[2] += " " +  arr[3];
						}
							set.add(arr[2].trim()); 
					}						
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set);
		return list; //pom.xml의 faster.xml.jackson...의 설정에 의해서 브라우저(javascript)에서 배열로 인식
	}
	
	@RequestMapping(value="select2", produces="text/plain; charset=utf-8" )//한글깨짐. 클라이언트로 문자열 전송. 인코딩 설정이 필요.
	//produces : 클라이언트에 전달되는 데이터의 특징을 설정
	//	text/plain : 데이터 특징. 순수문자
	public String select2(String si, String gu, HttpServletRequest request) {
		BufferedReader br = null;
		String path = request.getServletContext().getRealPath("/") + "file/sido.txt";
		try {
			br = new BufferedReader(new FileReader(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>(); 
		String data = null;
		if(si==null && gu==null) {
			try {
				while((data=br.readLine()) != null) {
					String[] arr = data.split("\\s+"); 
					if(arr.length >= 2 ) set.add(arr[0].trim());
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}	else if(gu==null) {
			si = si.trim();
			try {
				while((data=br.readLine())!= null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0])) set.add(arr[1].trim()); 
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(si != null && gu != null) {
			si = si.trim();
			gu = gu.trim();
			try {
				while((data=br.readLine())!= null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 
							&& arr[0].equals(si) && arr[1].equals(gu) 
							&& !arr[1].contains(arr[0]) && !arr[2].contains(arr[1])) {
						if(arr.length > 3) {
							if(arr[3].contains(arr[1])) continue;
							arr[2] += " " +  arr[3];
						}
							set.add(arr[2].trim()); 
					}						
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set);
		return list.toString();
	}
}
