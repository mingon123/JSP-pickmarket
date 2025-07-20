<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내가 쓴 상품 댓글 목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_img.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="myProductReplyList.do">내가 쓴 상품 댓글</a></h2>
		<c:if test="${count ==0}">
		<div class="result-display">
			내가 쓴 상품 댓글이 없습니다.
		</div>
		</c:if>
		
		<div class="myReply-list" style="margin-top: 20px;">
		<c:if test="${count > 0}">
		    <c:forEach var="reply" items="${list}">
		        <div class="product-card">

		            <!-- 썸네일 -->
		            <div class="product-myThumbnail">
		                <c:if test="${empty reply.productVO.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/images/NoProductImg.png" 
		                    	width="100" height="100" class="product-photo" 
		                    	onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${reply.product_num}'">
		                </c:if>
		                <c:if test="${!empty reply.productVO.thumbnail_img}">
		                    <img src="${pageContext.request.contextPath}/upload/${reply.productVO.thumbnail_img}" 
		                    	width="100" height="120" class="product-photo" 
		                    	onclick="location.href='${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${reply.product_num}'"
		                    	onerror="this.onerror=null; 
		                    	this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
		                    	this.nextElementSibling.style.display='flex';">
		                    <div class="error-text">이미지 로드 실패</div>
		                </c:if>
		            </div>

		            <!-- 상품정보 -->
		            <div class="product-info">
		                <a href="userProductDetail.do?product_num=${reply.product_num}" class="product-title">${reply.productVO.title}</a><br>
		                <div class="product-price">
		                    <fmt:formatNumber value="${reply.productVO.price}"/>원
		                </div>
						<div class="priduct-date">
							<small>${reply.productVO.nickname} ${reply.productVO.reg_date}</small>
						</div>
						
		                <c:choose>
		                    <c:when test="${reply.productVO.state == 0}">
		                        <span class="product-status-badge status-sale">판매중</span>
		                    </c:when>
		                    <c:when test="${reply.productVO.state == 1}">
		                        <span class="product-status-badge status-reserved">예약중</span>
		                    </c:when>
		                    <c:when test="${reply.productVO.state == 2}">
		                        <span class="product-status-badge status-soldout">판매완료</span>
		                    </c:when>
		                </c:choose>
					</div>
						
					 <%-- 댓글 정보 --%>
					 <div class="product-info">
					 	<b>${reply.reply_content}</b>
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