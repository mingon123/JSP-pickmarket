<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${nickname}님의 판매 내역</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_img.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#statusFilter').change(function(){
        $(this).closest('form').submit();
    });
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<c:choose>
		    <c:when test="${user.user_num == userNum}">
		       <h2><a href="myProductList.do">나의 판매 내역</a></h2>
		    </c:when>
		    <c:otherwise>
		        <h2><a href="myProductList.do?user_num=${userNum}">${nickname}님의 판매 내역</a></h2>
		    </c:otherwise>
		</c:choose>
		
		<c:if test="${count ==0}">
		<div class="result-display">
			등록된 상품이 없거나 숨겨진 상품입니다
		</div>
		</c:if>
		
		<c:if test="${count > 0}">
			<div class="product-list-header">
			    <form action="myProductList.do" method="get" class="filter-form">
			    	<input type="hidden" name="user_num" value="${userNum}"/>
			        <select name="keyfield" id="statusFilter" class="filter-select" onchange="this.form.submit()">
			            <option value="">거래상태 ▼</option>
			            <option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>판매중</option>
			            <option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>예약중</option>
			            <option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>판매완료</option>
			        </select>
			    </form>
			</div>
		
		    <c:forEach var="product" items="${list}">
		        <div class="product-card">

		            <!-- 썸네일 -->
		            <div class="product-myThumbnail">
		                <c:if test="${empty product.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/images/NoProductImg.png" width="100" height="100" class="product-photo" onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${product.product_num}'">
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
							<small>${product.region_nm} · ${product.reg_date}</small>
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
	                <c:if test="${product.modi_date != null}">
	                	<div class="action-btn3">수정됨</div>
	                </c:if>
	                <c:if test="${user.user_num == userNum}">
	                <c:if test="${product.state == 2}">
	                	<c:if test="${product.hide_status == 0}">
							<button class="action-btn1" onclick="location.href='productHide.do?product_num=${product.product_num}'">숨기기 등록</button>
						</c:if>
						<c:if test="${product.hide_status == 1}">
							<button class="action-btn1" onclick="location.href='productHide.do?product_num=${product.product_num}'">숨기기 해제</button>
						</c:if>
	                </c:if>
	                <c:if test="${product.up_count < 3}">
	                        <button class="action-btn" onclick="location.href='productUpCount.do?product_num=${product.product_num}'">끌올(${product.up_count})</button>
	               	</c:if>
	                </c:if>
	                <c:if test="${user.user_num != userNum && product.up_count < 3 && product.up_count > 0}">
	                	<div class="action-btn2">끌올</div>
	                </c:if>
	                </div>
                </div>
		    </c:forEach>
		    <div class="align-center">${page}</div>
			<div class="user-modify-align-center">
				<c:choose>
				    <c:when test="${user.user_num == userNum}">
				        <input type="button" value="마이페이지" onclick="history.back()">
				    </c:when>
				    <c:otherwise>
				        <input type="button" value="뒤로가기" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${userNum}'">
				    </c:otherwise>
				</c:choose>
			</div>
		</c:if>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>