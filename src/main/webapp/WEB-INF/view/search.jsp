<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
	function send() {
		opener.document.loginForm.userid.value = "${result}";
		self.close();
	}
</script>
</head>
<body>
	<h3>${title }찾기 : ${result }</h3>
	<c:if test="${title=='아이디' }">		
		<input type="button" value="아이디전송" onclick="send()">
	</c:if>
	<c:if test="${title=='비밀번호' }">
		<input type="button" value="닫기" onclick="javascript:self.close()">
	</c:if>
</body>
</html>