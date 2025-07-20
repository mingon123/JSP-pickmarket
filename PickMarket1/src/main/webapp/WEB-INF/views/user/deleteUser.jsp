<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${check}">
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>회원탈퇴 완료</h2>
		<div class="result-display">
			<div class="align-center">
				회원탈퇴가 완료되었습니다.<p>
				<button onclick="location.href='${pageContext.request.contextPath}/main/main.do'">홈으로</button>
			</div>
		</div>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
</c:if>
<c:if test="${!check}">
	<script>
		alert('입력한 정보가 정확하지 않습니다.');
		history.go(-1);
	</script>
</c:if>