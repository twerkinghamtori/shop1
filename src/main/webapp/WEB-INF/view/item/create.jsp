<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<!-- 서버에서 파라미터 검증. 파일 업로드 -->
<html>
<head>
<meta charset="UTF-8">
<title>상품등록</title>
</head>
<body>
	<form:form modelAttribute="item" action="register" enctype="multipart/form-data"> <!-- modelAttribute : bean 객체 이름이 "id 속성"으로 설정. 생략 시 command 문자열 생성 -->
		<h2>상품등록</h2>
		<table>
			<tr>
				<td>상품명</td>
				<td><form:input path="name" /></td> <!-- path : 설정한 property는 id와 name 속성으로 설정되며 model의 값을 getter를 통해 가져와 value 속성에 주입 -->
				<td><font color="red"><form:errors path="name" /></font></td>
			</tr>			
			<tr>
				<td>상품가격</td>
				<td><form:input path="price" /></td>
				<td><font color="red"><form:errors path="price" /></font></td>
			</tr>
			<tr>
				<td>상품이미지</td>
				<td colspan="2"><input type="file" name="picture"></td>
			</tr>
			<tr>
				<td>상품설명</td>
				<td><form:textarea path="description" cols="20" rows="5" /></td>
				<td><font color="red"><form:errors path="description" /></font></td>
			</tr>
			<tr>
				<td colspan="3">
					<input type="submit" value="상품등록">
					<input type="button" value="상품목록" onclick="location.href='list'">
				</td>
			</tr>
		</table>
	</form:form>
</body>
</html>