<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>운영정책 상세</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_add.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_modi.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h3><b>PickMarket 운영정책</b></h3><br>
		<div class="info-header">
		  	<span class="notice-title">${op.pol_title}</span>
		</div>
		<p class="info-content">${op.pol_content}</p>
		<hr size="1" noshade width="100%">
		<div class="align-right">
			작성일 : ${op.pol_date}
		</div><br>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='opList.do'">
			<%-- 관리자만 수정,삭제 가능 --%>
			<c:if test="${user_auth == 9}">
			<input type="button" value="수정" onclick="location.href='adminOPUpdateForm.do?pol_num=${op.pol_num}'">
			<input type="button" value="삭제" id="delete_btn">
			<script type="text/javascript">
				const delete_btn = document.getElementById('delete_btn');
				//이벤트 연결
				delete_btn.onclick=function(){
					let choice = confirm('삭제하시겠습니까?');
					if (choice) {
						location.replace('adminOPDelete.do?pol_num=${op.pol_num}');
					}
				};
			</script>
			</c:if>
		</div>
	</div> <%-- end of content-main --%>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>

