<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>찜한 상품</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_add.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_img.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/product.myfav.js"></script>
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
		<h2><a href="myProductFavList.do">찜한 상품</a></h2>
		<form id="search_form" action="myProductFavList.do" method="get">
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
					<input type="button" value="초기화" onclick="location.href='myProductFavList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			내가 찜한 상품이 없습니다.
		</div>
		</c:if>		
		<div class="myFav-list">
		<c:if test="${count > 0}">
		    <c:forEach var="fav" items="${list}">
		        <div class="product-card">

		            <!-- 썸네일 -->
		            <div class="product-myThumbnail">
		                <c:if test="${empty fav.productVO.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/images/NoProductImg.png" 
		                    	width="100" height="120" class="product-photo" 
		                    	onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${fav.product_num}'">
		                </c:if>
		                <c:if test="${!empty fav.productVO.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/upload/${fav.productVO.thumbnail_img}" 
		                    	width="100" height="120" class="product-photo" 
		                    	onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${fav.product_num}'"
		                    	onerror="this.onerror=null; 
		                    	this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
		                    	this.nextElementSibling.style.display='flex';">
		                    <div class="error-text">이미지 로드 실패</div>
		                </c:if>
			            <%-- 찜 --%>
				        <div class="heart-icon">
				            <img class="output_fav" data-num="${fav.product_num}" 
				                 src="${pageContext.request.contextPath}/images/unheart.png" width="40">				            
				        </div>
		            </div>

		            <!-- 상품정보 -->
		            <div class="fav-product-info">
		                <a href="userProductDetail.do?product_num=${fav.product_num}" class="product-title">${fav.productVO.title}</a><br>
		                <div class="product-price">
		                    <fmt:formatNumber value="${fav.productVO.price}"/>원
		                </div>
						<div class="priduct-date">
							<small>${fav.productVO.nickname} ${fav.productVO.reg_date}</small>
						</div>
						
		                <c:choose>
		                    <c:when test="${fav.productVO.state == 0}">
		                        <span class="product-status-badge status-sale">판매중</span>
		                    </c:when>
		                    <c:when test="${fav.productVO.state == 1}">
		                        <span class="product-status-badge status-reserved">예약중</span>
		                    </c:when>
		                    <c:when test="${fav.productVO.state == 2}">
		                        <span class="product-status-badge status-soldout">판매완료</span>
		                    </c:when>
		                </c:choose>
		                
		                <div class="fav_date">
		                ${fav.fav_date} 찜
		                </div>
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
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>