<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %> <%-- jtsl include --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container-lg">
		<h3 class="text-center mt-3 mb-3">${boardName } 게시글 삭제</h3>
		<form action="delete" method="post" name="f">
			<spring:hasBindErrors name="board">
				<font color="red">
					<c:forEach var="error" items="${errors.globalErrors }">
						<spring:message code="${error.code }"/><br> 
					</c:forEach>
				</font>
			</spring:hasBindErrors>		

			<input type="hidden" name="num" value="${param.num}">			

			<table class="table table-hover table-bodered">
				<tr>
					<td class="table-dark">비밀번호</td>
					<td><input class="form-control" type="password" name="pass"></td>
				</tr>			

				<tr>
					<td colspan="2">
						<a class="btn btn-danger" href="javascript:document.f.submit()">삭제</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>