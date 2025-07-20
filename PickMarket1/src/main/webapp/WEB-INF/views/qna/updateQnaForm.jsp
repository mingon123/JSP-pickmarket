<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1:1 문의 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#update_form').submit(function(){
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
		})
	});
</script>
</head>
<body>
<div class="page-main2">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main2" id="user-modify-main">
		<h2>1:1 문의 수정</h2>
		<form id="update_form" action="updateQna.do" method="post">
			<input type="hidden" name="qna_num" value="${qna.qna_num}">
			<ul>
				<li>
					<label for="title">제목</label>
					<input type="text" name="qna_title" id="title" maxlength="50" value="${qna.qna_title}">
				</li>
				<li>
					<label for="content">내용</label>
					<textarea rows="5" cols="40" name="qna_content" class="user-modify-input" id="content">${qna.qna_content}</textarea>
				</li>
			</ul>
			<div class="user-modify-align-center">
				<input type="submit" value="수정">
				<input type="button" value="목록" onclick="location.href='qnaList.do'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>