<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==""){
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
		<h2>상품 관리</h2>
		<form id="search_form" action="adminProductList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>상품제목</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>카테고리</option>						
						<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>닉네임</option>
					</select>
				</li>		
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>	
				<li>
					<input type="submit" value="검색">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='adminProductList.do'">
				</li>
			</ul>
		</form>

		<c:if test="${count == 0}">
			<div class="result-display">
				표시할 상품이 없습니다.
			</div>
		</c:if>
		<c:if test="${count > 0}">
			<table>
				<tr>
					<th>상품번호</th>
					<th>카테고리</th>
					<th>상품제목</th>
					<th>등록일</th>
					<th>상태</th>
					<th>닉네임</th>
				</tr>
				<c:forEach var="product" items="${list}">
				<tr>
					<td>${product.product_num}</td>
					<td>${product.category_name}</td>
					<%-- 관리자 페이지라서 바로 수정폼으로 갈 수 있도록 함  --%>
					<td><a href="productModifyForm.do?product_num=${product.product_num}">${product.title}</a></td>
					<td>${product.reg_date}</td>
					<td>
						<c:if test="${product.state == 0}">판매중</c:if>
						<c:if test="${product.state == 1}">예약중</c:if>
						<c:if test="${product.state == 2}">판매완료</c:if>
					</td>
					<td>${product.nickname}</td>
				</tr>		
				</c:forEach>
			</table>
			<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/user/adminPage.do'">
			<input type="button" value="등록" onclick="location.href='productWriteForm.do'">
			<input type="button" value="메인" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">   
		</div>
	</div>	
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>	
</body>
</html>