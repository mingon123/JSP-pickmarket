<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 작성</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#write_form').submit(function(){
			if($('#getter').val().trim()==''){
				alert('리뷰대상자 입력하세요!');
				$('#title').val('').focus();
				return false;
			}
			if($('#content').val().trim()==''){
				alert('리뷰내용을 입력하세요!');
				$('#content').val('').focus();
				return false;
			}
		});
	});
</script>
</head>
<body>
<div class="page-main2">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main2" id="user-modify-main">
		<input type="hidden" value="${user.user_num}">
		<h2>리뷰 작성</h2>
		<form id="write_form" action="writeReview.do" method="post">
			<input type="hidden" value="${product_num}" name="product_num">
			<ul>
				<li>
					<label for="getter">리뷰 대상자</label>
					<input type="text" name="getter" id="getter" maxlength="50" value="${targetUser.nickname}" readonly>
				</li>
				<li>
					<label for="content">내용</label>
					<textarea rows="5" cols="40" name="content" id="content"></textarea>
				</li>
			</ul>   
			<div class="user-modify-align-center">
				<input type="submit" value="등록">
				<input type="button" value="목록" onclick="location.href='reviewList.do?user_num=${targetUser.user_num}'">
			</div>                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>