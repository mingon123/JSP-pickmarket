<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>차단상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#block_delete_btn').click(function(){
		if(confirm("정말로 이 차단내역을 삭제하시겠습니까?")){
			location.href = 'adminBlockDelete.do?blocker_num=${block.blocker_num}&blocked_num=${block.blocked_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>'${block.blocker_nickname}' -> '${block.nickname}' 차단정보</h2>
		
		<div class="block-detail-box">
			<table>
				<tr>
					<th>차단자 닉네임</th><td>${block.blocker_nickname}</td>
				</tr>
				<tr>
					<th>피차단자 닉네임</th><td>${block.nickname}</td>
				</tr>
				<tr>
					<th>차단일</th><td>${block.block_date}</td>
				</tr>
				<tr>
					<th>차단 사유</th><td colspan="3">${block.block_content}</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="history.back()">
			<input type="button" value="삭제" id="block_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
