<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>상품 댓글 상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#reply_delete_btn').click(function(){
		if(confirm("정말로 이 상품 댓글을 삭제하시겠습니까?")){
			location.href = 'adminDeleteProductReply.do?reply_num=${reply.reply_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${reply.reply_num}번 상품 댓글 정보</h2>
		<div class="report-detail-box">
			<table>
				<tr>
					<th>상품 제목</th><td>${reply.title}</td>
				</tr>
				<tr>
					<th>댓글 작성자</th><td>${reply.nickname}</td>
				</tr>
				<tr>
					<th>작성일</th><td>${reply.reply_date}</td>
				</tr>
				<tr>
					<th>댓글 내용</th><td>${reply.reply_content}</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="location.href='productReplyList.do'">
			<input type="button" value="수정" onclick="location.href='updateProductReplyForm.do?reply_num=${reply.reply_num}'">
			<input type="button" value="삭제" id="reply_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
