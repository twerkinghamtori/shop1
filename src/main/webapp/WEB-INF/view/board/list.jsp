<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${boardName }</title>
<script type="text/javascript">
	function listpage(page) {
		document.searchForm.pageNum.value=page;
		document.searchForm.submit();
	}
</script>
</head>
<body>
	<table class="w3-table-all w3-border">
	<tr>
		<form action="list" method="post" name="searchForm">			
			<td>
				<input type="hidden" name="pageNum" value="1">
				<input type="hidden" name="boardid" value="${param.boardid }">
				<select name="searchType" class="w3-select">
					<option value="">선택하세요</option>
					<option value="title">제목</option>
					<option value="writer">작성자</option>
					<option value="content">내용</option>
				</select>
				<script type="text/javascript">
					searchForm.searchType.value="${param.searchType}";
				</script>
			</td>
			<td colspan="3">
				<input type="text" name="searchContent" value="${param.searchContent }" class="w3-input">					
			</td>
			<td>
				<input type="submit" value="검색" class="w3-btn w3-blue">
				<input type="button" value="전체 게시물 보기" class="w3-btn w3-blue" onclick="location.href='list?boardid=${boardid}'">
			</td>			
		</form>
	</tr>
		<c:if test="${listCount == 0 }">
			<tr>
				<td colspan="5">등록된 게시물이 없습니다.</td>
			</tr>
			<tr>
				<td colspan="5">
					<a href="write" class="w3-right">[글쓰기]</a>
				</td>
			</tr>
		</c:if>
		<c:if test="${listCount>0 }"> <!-- 등록된 게시물 건수 -->
			<tr>
				<td colspan="5">글개수:${listCount }</td>
			</tr>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>글쓴이</th>
				<th>날짜</th>
				<th>조회수</th>
			</tr>
			<c:forEach var="board" items="${boardList }">
				<tr>
					<td>${boardNo }</td>
					<c:set var="boardNo" value="${boardNo - 1 }" />
					<td class="w3-left">
						<c:if test="${!empty board.fileurl }">
							<a href="file/${board.fileurl }">@</a>
						</c:if>

						<c:forEach begin="1" end="${board.grplevel }">&nbsp;&nbsp;</c:forEach>
						<c:if test="${board.grplevel > 0 }">└</c:if>
						<a href="detail?num=${board.num }">${board.title }</a>
					</td>
					<td>${board.writer }</td>
					<td>
						<fmt:formatDate value="${board.regdate }" pattern="yyyyMMdd" var="rdate" />
						<c:if test="${today==rdate }">
							<fmt:formatDate value="${board.regdate }" pattern="HH:mm:ss" />
						</c:if>
						<c:if test="${today!=rdate }">
							<fmt:formatDate value="${board.regdate }" pattern="yyyy-MM-dd HH:mm" />
						</c:if>
					</td>
					<td>${board.readcnt }</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="5">
					<a href="write" class="w3-right">[글쓰기]</a>
				</td>
			</tr>
			<tr>
				<td colspan="5" class="w3-center">
					<c:if test="${pageNum > 1 }">
						<a href="javascript:listpage('${pageNum -1 }')">[이전]</a>
					</c:if>
					<c:if test="${pageNum <= 1 }">
						[이전]
					</c:if>
					<c:forEach var="a" begin="${startpage }" end="${endpage }">
						<c:if test="${a==pageNum }">[${a }]</c:if>
						<c:if test="${a != pageNum }">
							<a href="javascript:listpage('${a }')">[${a }]</a>
						</c:if>
					</c:forEach>
					<c:if test="${pageNum < maxpage }">
						<a href="javascript:listpage('${pageNum + 1 }')">[다음]</a>
					</c:if>
					<c:if test="${pageNum >= maxpage }">
						[다음]
					</c:if>
				</td>
			</tr>
		</c:if>		
	</table>
</body>
</html>