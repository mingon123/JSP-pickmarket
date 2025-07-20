<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고하기</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#write_form').submit(function(){
			if($('#title').val().trim()==''){
				alert('제목을 입력하세요!');
				$('#title').val('').focus();
				return false;
			}
			if($('#content').val().trim()==''){
				alert('내용을 입력하세요!');
				$('#content').val('').focus();
				return false;
			}
			if($('#report_img').val().trim()==''){
				alert('사진을 첨부하세요!');
				$('#report_img').val('').focus();
				return false;
			}
		})
	});
</script>
</head>
<body>
<div class="page-main2">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main2" id="user-modify-main">
		<h2>신고하기</h2>
		<form id="write_form" action="writeReport.do" method="post" enctype="multipart/form-data">
			<input type="hidden" name="suspect_num" value="${suspect_num}">
			<ul>
				<li>
					<label for="nickname">신고 대상</label>
					<input type="text" id="nickname" value="${nickname}" readonly>
				</li>
				<li>
					<label for="title">신고 제목</label>
					<input type="text" name="report_title" id="title" maxlength="50">
				</li>
				<li>
					<label for="content">신고 내용</label>
					<textarea rows="5" cols="40" name="report_content" id="content" class="user-modify-input"></textarea>
				</li>
				<li>
					<label for="report_img">신고 이미지</label>
					<input type="file" name="report_img" id="report_img" accept="image/gif,image/png,image/jpeg">
				</li>
			</ul>
			<div class="user-modify-align-center">
				<input type="submit" value="등록">
				<input type="button" value="취소" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${suspect_num}'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>