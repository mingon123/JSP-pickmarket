<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>거래 후기 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#update_form').submit(function(){
			if($('#re_content').val().trim()==''){
				alert('내용을 입력하세요!');
				$('#re_content').val('').focus();
				return false;
			}
		})
	});
</script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${review.re_num}번 거래 후기 수정</h2>
		<form id="modify_form" action="updateReview.do" method="post">
			<ul>
				<li>
					<input type="hidden" name="re_num" id="re_num" value="${review.re_num}">
				</li>
				<li>
					<label for="re_content">내용</label>
					<textarea rows="6" cols="40" name="re_content" id="re_content" class="input-check" autocomplete="off">${review.re_content}</textarea>
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="수정" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminReviewList.do'" class="simple-button">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>