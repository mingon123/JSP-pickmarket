<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/he.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw_admin.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
</script>
<style type="text/css">
/* 커뮤니티 목록 관리 테이블 스타일 */
.table-admin {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px; /* 여백 줄임 */
}

.table-admin th, .table-admin td {
  text-align: center; /* 가운데 정렬 */
  padding: 10px 8px;
  border-bottom: 1px solid #ddd;
}

/* 제목 상단 여백 줄이기 */
.content-main h2 {
  margin-bottom: 20px;
  font-size: 24px;
  text-align: center;
  font-weight: bold;
}
</style>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>커뮤니티 목록 관리</h2>
		<form id="search_form" action="adminBoardList.do" method="get">
			<ul class="search">
				<li>
					<select name = "keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>글제목</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>작성자</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name ="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value= "검색">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='adminBoardList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count ==0}">
		<div class="result-display">
			표시할 글이 없습니다
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<table class=table-admin>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자 닉네임</th>
			</tr>
			<c:forEach var="board" items="${board}">
			<tr>
				<td>${board.board_num}</td>
				<td><a href="adminBoardDetail.do?board_num=${board.board_num}">${board.btitle}</a></td>
				<td><a href="${pageContext.request.contextPath}/user/adminUserDetail.do?user_num=${board.user_num}">${board.nickname}</a></td>
			</tr>
			</c:forEach>
		</table>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/user/adminPage.do'">
			<input type="button" value="홈으로" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">
		</div>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>