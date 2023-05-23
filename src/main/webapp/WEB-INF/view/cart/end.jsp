<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문완료</title>
</head>
<body>
	<h2>${sale.user.username }님이 주문하신 정보입니다.</h2>
	<h2>배송지 정보</h2>
	<table class="w3-table w3-striped">
		<tr>
			<td width="30%">주문아이디</td>
			<td width="70%">${sale.user.userid }</td>
		</tr>
		<tr>
			<td width="30%">이름</td>
			<td width="70%">${sale.user.username }</td>
		</tr>
		<tr>
			<td width="30%">우편번호</td>
			<td width="70%">${sale.user.postcode }</td>
		</tr>
		<tr>
			<td width="30%">주소</td>
			<td width="70%">${sale.user.address }</td>
		</tr>
		<tr>
			<td width="30%">전화번호</td>
			<td width="70%">${sale.user.phoneno }</td>
		</tr>
	</table>
	
	<h2>주문상품</h2>
	<table class="w3-table w3-striped">
		<tr>
			<th>상품명</th>
			<th>가격</th>
			<th>수량</th>
			<th>합계</th>
		</tr>
		<c:forEach items="${sale.itemList }" var="saleitem">
			<tr>
				<td>${saleitem.item.name }</td>
				<td>${saleitem.item.price }</td>
				<td>${saleitem.quantity }</td>
				<td><fmt:formatNumber value="${saleitem.item.price * saleitem.quantity }" pattern="###,###" /></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4" align="right">
				총 주문 금액 : <fmt:formatNumber value="${sale.total }" pattern="###,###" />
			</td>
		</tr>
		<tr>
				<td colspan="4">
					<a href="../item/list">상품 목록</a>
				</td>
			</tr>
	</table>
</body>
</html>