<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 댓글 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il.css">
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
	<div class="admin-content-main">
		<h2>상품 댓글 관리</h2>
		<form id="search_form" action="productReplyList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>상품번호</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>댓글 작성자</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value="찾기">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='productReplyList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 상품 댓글이 없습니다.
		</div>
		</c:if>
		
		<c:if test="${count > 0}">
		<table>
			<tr>
				<td>번호</td>
				<td>댓글 내용</td>
				<td>상품번호</td>
				<td>댓글 작성자</td>
				<td>등록일</td>
			</tr>
			
			<c:forEach var="reply" items="${list}">
			<tr>
				<td>${reply.reply_num}</td>
				<td>
					<a href="productReplyDetail.do?reply_num=${reply.reply_num}">${reply.reply_content}</a>
				</td>
				<td>${reply.product_num}</td>
				<td><a href="${pageContext.request.contextPath}/user/userDetail.do?user_num=${reply.user_num}">${reply.nickname}</a></td>
				<td>${reply.reply_date}</td>
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