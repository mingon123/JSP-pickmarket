<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>거래 후기 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_modi.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요!');
				$('#keyword').val('').focus();
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
		<h2>거래 후기 관리</h2>
		<form id="search_form" action="adminReviewList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>리뷰 작성자</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>리뷰 대상</option>
						<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>리뷰 내용</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value="찾기">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='adminReviewList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 거래 후기가 없습니다.
		</div>
		</c:if>
		
		<c:if test="${count > 0}">
		<table>
			<tr>
				<td>번호</td>
				<td>리뷰 내용</td>
				<td>글번호</td>
				<td>리뷰 대상</td>
				<td>리뷰 작성자</td>
				<td>등록일</td>
			</tr>
			
			<c:forEach var="review" items="${list}">
			<tr>
				<td>${review.re_num}</td>
				<td>
					<!-- <a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${product.product_num}"> -->
					<a href="adminReviewDetail.do?re_num=${review.re_num}">${review.re_content}</a>
				</td>
				<td>${review.product_num}</td>
				<td><a href="${pageContext.request.contextPath}/user/userDetail.do?user_num=${review.re_getter_num}">${review.getterVO.nickname}</a></td>
				<td><a href="${pageContext.request.contextPath}/user/userDetail.do?user_num=${review.re_writer_num}">${review.writerVO.nickname}</a></td>
				<td>${review.re_date}</td>
			</tr>
			</c:forEach>
		</table>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/user/adminPage.do'">
			<input type="button" value="메인" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">                        
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>