<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>운영정책 수정</title>
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
		<h2>${op.pol_num}번 운영정책 수정</h2>
		<form id="modify_form" action="adminOPUpdate.do" method="post">
			<ul>
				<li>
					<input type="hidden" name="pol_num" id="pol_num" value="${op.pol_num}">
				</li>
				<li>
					<label for="pol_title">제목</label>
					<input type="text" name="pol_title" id="pol_title" maxlength="100" class="input-check" autocomplete="off" value="${op.pol_title}">
				</li>
				<li>
					<label for="pol_content">내용</label>
					<textarea rows="6" cols="40" name="pol_content" id="pol_content" class="input-check" autocomplete="off">${pol_content}</textarea>
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="수정" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminOPList.do'" class="simple-button">
			</div>                                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
