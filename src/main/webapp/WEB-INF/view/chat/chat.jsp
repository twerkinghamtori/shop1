<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<c:set var="port" value="${pageContext.request.localPort }" />  <%-- 포트번호 : 8080 --%>
<c:set var="server" value="${pageContext.request.serverName }" /> <%-- IP주소 : localhost. url의 ip주소로 받아옴 --%>
<c:set var="path" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<script type="text/javascript">
	$(function() {
		//load 되면 websocket 객체 생성
		let ws = new WebSocket("ws://${server}:${port}${path}/chatting") //ws로 시작하면 websocket => Echohandler.afterConnectionEstablished()
		ws.onopen = function() { //서버 접속
			$("#chatStatus").text("info:connection opened")
			$("input[name=chatInput]").on("keydown", function(e) {
				if(e.keyCode == 13) {
					let msg = $("input[name=chatInput]").val()
					ws.send(msg) //EchoHandler.handleMessage()
					$("input[name=chatInput]").val("")
				}
			})
		}
		ws.onmessage = function(e) { //서버로부터 메세지 수신
			$("textarea").eq(0).prepend(e.data + "\n") // e.data = 수신된 메세지 값, prepend : 뒤에 말고 앞에 추가.
		} //서버와 접속이 끊긴 경우
		ws.onclose = function(e) {
			$("#chatStatus").text("info:connection close")
		}
	})
</script>
</head>
<body>
	<div id="chatStatus"></div>
	<textarea name="chatMsg" rows="15" cols="40"></textarea><br>
	메세지 입력 : <input type="text" name="chatInput">
</body>
</html>