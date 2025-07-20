<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#user_delete_btn').click(function(){
		if(confirm("정말로 이 회원을 삭제하시겠습니까?")){
			location.href = 'adminUserDelete.do?user_num=${target.user_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${target.id} 회원정보</h2>
		
		<div class="user-detail-box">
			<table>
				<tr>
					<th>등급</th>
					<td>
						<c:choose>
							<c:when test="${target.auth == 0}">탈퇴</c:when>
							<c:when test="${target.auth == 1}">정지</c:when>
							<c:when test="${target.auth == 2}">일반</c:when>
							<c:when test="${target.auth == 9}">관리자</c:when>
						</c:choose>
					</td>
					<th>등록일</th>
					<td><fmt:formatDate value="${target.reg_date}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<th>닉네임</th><td>${target.nickname}</td>
					<th>최근 수정일</th>
					<td>
						<c:choose>
							<c:when test="${empty target.modi_date}">
								-
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${target.modi_date}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>이름</th><td>${target.name}</td>
					<th>점수</th><td>${target.score}</td>
				</tr>
				<tr>
					<th>비밀번호</th><td>${target.passwd}</td>
					<th>신고횟수</th><td>${target.report_count}</td>
				</tr>
				<tr>
					<th>전화번호</th><td>${target.phone}</td>
					<th rowspan="2">사진</th>
					<td rowspan="2">
						<c:if test="${not empty target.photo}">
							<img src="${pageContext.request.contextPath}/upload/${target.photo}" width="80" height="80" 
								onerror="this.src='${pageContext.request.contextPath}/images/face.png'">
						</c:if>
						<c:if test="${empty target.photo}">
							<img src="${pageContext.request.contextPath}/images/face.png" width="80" height="80">
						</c:if>
					</td>
				</tr>
				<tr>
					<th>지역</th><td>${region_nm}</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="이전" onclick="history.back()">
			<input type="button" value="목록" onclick="location.href='adminUserList.do'">
			<input type="button" value="수정" onclick="location.href='adminUserUpdateForm.do?user_num=${target.user_num}'">
			<input type="button" value="삭제" id="user_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
