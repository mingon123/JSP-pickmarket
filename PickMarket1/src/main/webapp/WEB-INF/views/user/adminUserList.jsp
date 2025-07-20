<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
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
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>회원관리</h2>
		<form id="search_form" action="adminUserList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>아이디</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>닉네임</option>
						<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>전화번호</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value="검색">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='adminUserList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 회원정보가 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<table>
			<tr>
				<td>번호</td>
				<td>아이디</td>
				<td>닉네임</td>
				<td>등급</td>
				<td>신고횟수</td>
			</tr>
			<c:forEach var="user" items="${list}">
			<tr>
				<td>${user.user_num}</td>
				<td>
					<c:if test="${user.auth > 0}">
					<a href="adminUserDetail.do?user_num=${user.user_num}">${user.id}</a>
					</c:if>
					<c:if test="${user.auth == 0}">
					${user.id}		
					</c:if>
				</td>
				<td>${user.nickname}</td>
				<td>
					<c:if test="${user.auth == 0}">탈퇴</c:if>
					<c:if test="${user.auth == 1}">정지</c:if>
					<c:if test="${user.auth == 2}">일반</c:if>
					<c:if test="${user.auth == 9}">관리</c:if>
				</td>
				<td>${user.report_count}</td>
			</tr>	
			</c:forEach>
		</table>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='adminPage.do'">
			<input type="button" value="등록" onclick="location.href='adminUserRegisterForm.do'">
			<input type="button" value="메인"
			     onclick="location.href='${pageContext.request.contextPath}/main/main.do'">                        
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>








