<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${notice.noti_num}번 공지사항 수정</h2>
		<form id="modify_form" action="adminNoticeUpdate.do" method="post">
			<ul>
				<li>
					<input type="hidden" name="noti_num" id="noti_num" value="${notice.noti_num}">
				</li>
				<li>
					<label for="noti_title">제목</label>
					<input type="text" name="noti_title" id="noti_title" maxlength="100" class="input-check" autocomplete="off" value="${notice.noti_title}">
				</li>
				<li>
					<label for="noti_content">내용</label>
					<textarea rows="6" cols="40" name="noti_content" id="noti_content" class="input-check" autocomplete="off">${noti_content}</textarea>
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="수정" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminNoticeList.do'" class="simple-button">
			</div>                                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
