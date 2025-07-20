<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>거래 후기 상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		const delete_btn = document.getElementById('delete_btn');
		
		delete_btn.onclick=function(){
			let choice = confirm('삭제하시겠습니까?');
			if(choice){
				location.replace('adminReviewDelete.do?re_num=${review.re_num}');
			}
		};
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${review.re_num}번 거래 후기 정보</h2>
		<div class="report-detail-box">
			<table>
				<tr>
					<th>상품 제목</th><td><a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${review.productVO.product_num}">${review.productVO.title}</a></td>
				</tr>
				<tr>
					<th>상품 내용</th><td colspan="3">${review.productVO.content}</td>
				</tr>
				<tr>
					<th>리뷰 내용</th>	<td colspan="3">${review.re_content}</td>
				</tr>
				<tr>
					<th>판매자</th><td>${review.getterVO.nickname}</td>
				</tr>
				<tr>
					<th>구매자</th><td>${review.writerVO.nickname}</td>
				</tr>
				
				<tr>
					<th>작성일</th><td colspan="3">${review.re_date}</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="location.href='adminReviewList.do'">
			<input type="button" value="수정" onclick="location.href='updateReviewForm.do?re_num=${review.re_num}'">
			<input type="button" value="삭제" id="delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
