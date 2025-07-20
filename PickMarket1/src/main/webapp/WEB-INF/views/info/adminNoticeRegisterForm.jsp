<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>공지사항 등록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin_info.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#register_form').submit(function(){
    	const items = document.querySelectorAll('.input-check');
		for(let i=0;i<items.length;i++){
			if(items[i].value.trim()==''){
				const label = document.querySelector('label[for="'+items[i].id+'"]');
				alert(label.textContent + ' 입력 필수');
				items[i].value='';
				items[i].focus();
				return false;
			}//end of if
		}
	});
});
</script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>공지사항 등록</h2>
		<form id="register_form" action="adminNoticeRegister.do" method="post">
			<ul>
				<li>
					<label for="noti_title">제목</label>
					<input type="text" name="noti_title" id="noti_title" maxlength="100" class="input-check" autocomplete="off" >
				</li>
				<li>
					<label for="noti_content">내용</label>
					<textarea rows="6" cols="40" name="noti_content" id="noti_content" class="input-check" autocomplete="off"></textarea>
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="등록" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminNoticeList.do'" class="simple-button">
			</div>                                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
