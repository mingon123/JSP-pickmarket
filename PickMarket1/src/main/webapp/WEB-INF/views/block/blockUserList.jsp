<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>차단계정 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#search_form').submit(function(){
		if($('#keyword').val().trim()==''){
			alert('검색어를 입력하세요!');
			$('#keyword').val('').focus();
			return false;
		}
	});
	
	$('.delete-btn').click(function(){
		const blocked_num = $(this).data('blocked-num');
		if(confirm('차단해제 하시겠습니까?')){
			location.href = 'deleteBlockUser.do?blocked_num=' + blocked_num;
		}
	});
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="blockUserList.do">차단계정 관리</a></h2>
		<form id="search_form" action="blockUserList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>닉네임</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>차단사유</option>
					</select>
				</li>
				<li>
					<input type="search" name="keyword" id="keyword" value="${param.keyword}" autocomplete="off" placeholder="검색어 입력">
				</li>
				<li>
					<input type="submit" value="검색">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='blockUserList.do'">
				</li>
			</ul>
		</form>
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 게시물이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<div class="block-container">
			<c:forEach var="block" items="${list}">
				<div class="block-card">
					<c:if test="${empty block.photo}">
					<img src="${pageContext.request.contextPath}/images/face.png" width="100" height="100" class="my-photo" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${block.blocked_num}'">
					</c:if>
					<c:if test="${!empty block.photo}">
					<img src="${pageContext.request.contextPath}/upload/${block.photo}" width="100" height="100" class="my-photo" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${block.blocked_num}'">
					</c:if>
					<div class="nickname">${block.nickname}</div>
					<div class="content">${block.block_content}</div>
					<div class="date">${block.block_date}</div>
					<button class="delete-btn" data-blocked-num="${block.blocked_num}">차단 해제</button>
				</div>
			</c:forEach>
		</div>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="user-modify-align-center">
			<input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPage.do'">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>