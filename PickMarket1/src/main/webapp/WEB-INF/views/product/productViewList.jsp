<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최근 본 상품 목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_img.css">
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
	
	$('.delete_btn').click(function(){
		let product_num = $(this).data('product');
		let choice = confirm('최근 본 상품 기록을 삭제하시겠습니까?');
		if(choice){
			location.replace('deleteView.do?product_num=' + product_num);
		}
	});
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="productViewList.do">최근 본 상품 목록</a></h2>
		<c:if test="${count ==0}">
		<div class="result-display">
			최근에 본 상품이 없습니다.
		</div>
		</c:if>
		
		<c:if test="${count > 0}">
			<form id="search_form" action="productViewList.do" method="get">
				<ul class="search">
					<li>
						<select name="keyfield">
							<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>상품명</option>
							<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>내용</option>
						</select>
					</li>
					<li>
						<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
					</li>
					<li>
						<input type="submit" value="검색">
					</li>
					<li>
						<input type="button" value="초기화" onclick="location.href='productViewList.do'">
					</li>
				</ul>
			</form>
		
		    <c:forEach var="product" items="${list}">
		        <div class="product-card">

		            <!-- 썸네일 -->
		            <div class="product-myThumbnail">
		                <c:if test="${empty product.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/images/NoProductImg.png" 
		                    	width="100" height="100" class="product-photo" 
		                    	onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${product.product_num}'">
		                </c:if>
		                <c:if test="${!empty product.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/upload/${product.thumbnail_img}" 
		                    	width="100" height="120" class="product-photo" 
		                    	onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${product.product_num}'" 
		                    	onerror="this.onerror=null; 
		                    	this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
		                    	this.nextElementSibling.style.display='flex';">
		                    <div class="error-text">이미지 로드 실패</div>
		                </c:if>
		            </div>

		            <!-- 상품정보 -->
		            <div class="product-info">
		                <a href="userProductDetail.do?product_num=${product.product_num}" class="product-title">${product.title}</a><br>
		                <div class="product-price">
		                    <fmt:formatNumber value="${product.price}"/>원
		                </div>
						<div class="priduct-date">
							<small>${product.nickname} ${product.reg_date}</small>
						</div>
						
		                <c:choose>
		                    <c:when test="${product.state == 0}">
		                        <span class="product-status-badge status-sale">판매중</span>
		                    </c:when>
		                    <c:when test="${product.state == 1}">
		                        <span class="product-status-badge status-reserved">예약중</span>
		                    </c:when>
		                    <c:when test="${product.state == 2}">
		                        <span class="product-status-badge status-soldout">판매완료</span>
		                    </c:when>
		                </c:choose>
					</div>
					
	                <!-- 버튼영역 -->
	                <div class="product-actions">
	                <button class="action-btn1 delete_btn" data-product="${product.product_num}">삭제</button>
	                <c:if test="${product.modi_date != null}">
	                	<div class="action-btn3">수정됨</div>
	                </c:if>
	                <c:if test="${user.user_num != userNum && product.up_count < 3 && product.up_count > 0}">
	                	<div class="action-btn2">끌올</div>
	                </c:if>
	                </div>
                </div>
		    </c:forEach>
		    <div class="align-center">${page}</div>
			</c:if>
			<div class="user-modify-align-center">
				<input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPage.do'">
			</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>