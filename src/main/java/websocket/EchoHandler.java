package websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//EchoHandler : 문장들을 왔다갔다 하게 해주는거
@Component
public class EchoHandler extends TextWebSocketHandler implements InitializingBean { 
	//WebSocketSession : 클라이언트, 채팅 중인 한개의 브라우저
	//현재 채팅중인 모든 브라우저의 session을 set객체로 저장
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
	
	//채팅관련 이벤트들
	@Override //Socket이 연결된 경우. (chat.jsp에서 ws객체 만들어지자마자 호출되는 함수)
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		System.out.println("클라이언트 접속 : " + session.getId());
		clients.add(session);
	}
	
	@Override //클라이언트에서 메세지 수신된 경우
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		//클라이언트가 전송한 메세지
		String loadMessage = (String)message.getPayload();
		System.out.println(session.getId() + " : 클라이언트 메세지 : " + loadMessage);
		clients.add(session); //set객체니까 2번 저장되지는 않음.
		for(WebSocketSession s : clients) { //(broadcasting)
			s.sendMessage(new TextMessage(loadMessage)); //client에게 받은 메세지를 모든 client에게 전송
		}
	}	
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		System.out.println("오류발생 : " + exception.getMessage());
	}
	
	@Override //브라우저와 접속 종료
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제 : " + status.getReason());
		clients.remove(session);
	}

	@Override
	public void afterPropertiesSet() throws Exception {}
}
