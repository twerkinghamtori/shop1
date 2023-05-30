<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>
	<form:form modelAttribute="board" action="write" enctype="multipart/form-data" name="f">
		<table class="w3-table-all">
			<tr>
				<th>글쓴이</th>
				<td>
					<form:input path="writer" class="w3-input" />
					<font color="red">
						<form:errors path="writer"></form:errors>
					</font>
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<form:password path="pass" class="w3-input" />
					<font color="red">
						<form:errors path="pass"></form:errors>
					</font>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>
					<form:input path="title" class="w3-input" />
					<font color="red">
						<form:errors path="title"></form:errors>
					</font>
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td>
					<form:textarea path="content" class="w3-input" />
					<font color="red">
						<form:errors path="content"></form:errors>
					</font>
				</td>
			</tr>
			<script>CKEDITOR.replace("content",{filebrowserImageUploadUrl : "imgupload"})</script>
			<tr>
				<th>첨부파일</th>
				<td>
					<input type="file" name="file1">
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<a href="javascript:document.f.submit()">[게시글등록]</a>
					<a href="list?boardid=${boardid }">[게시글목록]</a>
				</td>				
			</tr>
		</table>
	</form:form>
</body>
</html>