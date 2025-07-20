<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원차단</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#write_form').on('submit', function(e){
			if($('#nickname').val().trim()==''){
				alert('차단 대상을 입력하세요!');
				$('#nickname').focus();
				return false;
			}
			if($('#block_content').val().trim()==''){
				alert('내용을 입력하세요!');
				$('#block_content').focus();
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
		<h2>회원차단</h2>
		<form id="write_form" action="insertBlockUser.do" method="post">
			<input type="hidden" name="blocked_num" value="${blocked_num}">
			<ul>
				<li>
					<label for="nickname">차단 대상</label>
					<input type="text" id="nickname" value="${nickname}" readonly>
				</li>
				<li>
					<label for="block_content">차단 사유</label>
					<textarea rows="5" cols="40" name="block_content" id="block_content"></textarea>
				</li>
			</ul>
			<div class="user-modify-align-center">
				<input type="submit" value="등록">
				<input type="button" value="취소" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${blocked_num}'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>