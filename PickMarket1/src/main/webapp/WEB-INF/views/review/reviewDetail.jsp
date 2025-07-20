<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>거래 후기 상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		const delete_btn = document.getElementById('delete_btn');
		
		delete_btn.onclick=function(){
			let choice = confirm('삭제하시겠습니까?');
			if(choice){
				location.replace('reviewDelete.do?re_num=${review.re_num}');
			}
		};
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>거래 후기 정보</h2>
		<div class="account-box">
			<div class="qna-header qna-detail">
				<h2>상품 제목 : <a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${review.productVO.product_num}">${review.productVO.title}</a></h2>
				<span class="qna-date">작성일:${review.re_date}</span>
			</div>
			<div class="qna-header qna-detail">
				<span class="fontsize20">상품 내용</span>
				<span class="qna-date fontsize20">판매자:${review.getterVO.nickname}</span>
			</div>
			<div class="qna-content">
				<span>${review.productVO.content}</span>
			</div>
			<hr width="100%" size="1" noshade="noshade">
			
			<div class="qna-header qna-detail">
				<span class="fontsize20">리뷰 내용</span>
				<span class="qna-date fontsize20">구매자:${review.writerVO.nickname}</span>
			</div>
			<div class="qna-reply">			
				${review.re_content}
			</div>
			<hr width="100%" size="1" noshade="noshade">
		</div>
		<div class="user-modify-align-center">
			<input type="button" value="목록" onclick="location.href='myReviewList.do?user_num=${review.re_writer_num}'">
			<input type="button" value="삭제" id="delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
