<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 댓글</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/he.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css">
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
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>커뮤니티 댓글 관리</h2>
		<form id="search_form" action="adminBoardReplyList.do" method="get">
			<ul class="search">
				<li>
					<select name = "keyfield">
						<option value="4" <c:if test="${param.keyfield==4}">selected</c:if>>글번호</option>
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
					<input type="button" value="초기화" onclick="location.href='adminBoardReplyList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count ==0}">
		<div class="result-display">
			표시할 댓글이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<table class=table-admin>
			<tr>
				<th>번호</th>
				<th>댓글내용</th>
				<th>원본 글 번호</th>
				<th>작성자 닉네임</th>
			</tr>
			<c:forEach var="board" items="${board}">
				<tr>
					<td>${board.breply_num}</td>
					<td><a href="adminBoardReplyDetail.do?breply_num=${board.breply_num}">${board.breply_content}</a></td>
					<td><a href="boardDetail.do?board_num=${board.board_num}">${board.board_num}</a></td>
					<td>${board.nickname}</td>
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