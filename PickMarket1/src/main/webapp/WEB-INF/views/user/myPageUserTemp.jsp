<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>MyPage</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_img.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main1">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="mypage-div">
		<h3><a href="${pageContext.request.contextPath}/user/myPage.do">마이페이지</a></h3>
		<div class="mypage-profile-wrapper">
			<div class="mypage-photo">
				<c:if test="${empty user.photo}">
				<img src="${pageContext.request.contextPath}/images/face.png" width="150" height="150">
				</c:if>
				<c:if test="${!empty user.photo}">
				<img src="${pageContext.request.contextPath}/upload/${user.photo}" width="150" height="150">
				</c:if>
			</div>
			<div class="mypage-basic-info">
				<ul>
					<li>닉네임 : <b>${user.nickname}</b></li>
					<li>동네 : ${user.locationVO.region_nm}</li>
				</ul>
			</div>
			
			<div class="manner-score">
				<div class="manner-circle" onclick="location.href='${pageContext.request.contextPath}/user/myPageUserTemp.do'">
					<div class="manner-value">${user_temp}°</div>
					<div class="manner-label">매너온도</div>
				</div>
			</div>
		</div>
		<hr width="100%" size="1" noshade="noshade">
		
		<div class="section">
		    <div class="section-header-wrapper">
		        <div class="section-header-box">판매 물품 (${productCount})</div>
		        <c:if test="${!empty productList}">
		        <a href="${pageContext.request.contextPath}/product/myProductList.do?user_num=${user.user_num}" class="more-link">더보기 ></a>
		        </c:if>
		    </div>
		    <hr width="100%" size="1" noshade="noshade">
		    <div class="manner-box">
		        <div class="myProduct-list">
				    <c:forEach var="product" items="${productList}">
			        <div class="myProduct-card">
		
			            <!-- 썸네일 -->
						<div class="myProduct-thumbnail">
						    <c:if test="${empty product.thumbnail_img}" >
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
			                <a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${product.product_num}" class="product-title">${product.title}</a><br>
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
					</div>
					</c:forEach>
					<c:if test="${empty productList}">
	            	<div class="no-manner">등록한 상품이 없습니다.</div>
		            </c:if>
				</div>
			</div>
		</div>

		<hr width="100%" size="1" noshade="noshade">
		<div class="section">
		    <div class="section-header-wrapper">
		        <div class="section-header-box">내가 받은 매너평가</div>
		        <c:if test="${!empty mannerList}">
		        <a href="${pageContext.request.contextPath}/review/mannerList.do?user_num=${user.user_num}" class="more-link">더보기 ></a>
		        </c:if>
		    </div>
		    <hr width="100%" size="1" noshade="noshade">
		    <div class="manner-box">
		        <div class="manner-list">
		            <c:forEach var="manner" items="${mannerList}">
	                <div class="manner-item">
	                    <span class="manner-count"><b>${manner.count}</b></span>
	                    <span class="manner-op-box"><b>${manner.mannerOp}</b></span>
	                </div>
		            </c:forEach>
		            
		            <c:if test="${empty mannerList}">
	            	<div class="no-manner">받은 매너평가가 없습니다.</div>
		            </c:if>
		        </div>
		    </div>
		</div>
		
		
		<!-- 내가 받은 거래후기 -->
		<div class="section">
		    <div class="section-header-wrapper">
		        <div class="section-header-box">
		            내가 받은 거래후기 (${reviewCount})
		        </div>
		        <c:if test="${!empty reviewList}">
		        <a href="${pageContext.request.contextPath}/review/reviewList.do?user_num=${user.user_num}" class="more-link">더보기 ></a>
		        </c:if>
		    </div>
			<hr width="100%" size="1" noshade="noshade">
		    
		    <div class="review-list">
		        <c:forEach var="review" items="${reviewList}">
	            <div class="review-item">
	                <div class="review-profile">
		                <c:if test="${empty review.userVO.photo}">
						<img src="${pageContext.request.contextPath}/images/face.png" width="50" height="50" class="my-photo1">
						</c:if>
						<c:if test="${!empty review.userVO.photo}">
						<img src="${pageContext.request.contextPath}/upload/${review.userVO.photo}" width="50" height="50" class="my-photo1">
						</c:if>
	                </div>
	                <div class="review-content">
	                    <div class="review-nickname">${review.userVO.nickname}</div>
	                    <div class="review-date">${review.re_date}</div>
	                    <div class="review-text">${review.re_content}</div>
	                </div>
	            </div>
		        </c:forEach>
		        <div class="manner-box">
			        <div class="manner-list">
				        <c:if test="${empty reviewList}">
			            <div class="no-manner">받은 거래후기가 없습니다.</div>
				        </c:if>
				    </div>
		    	</div>
		    </div>
		</div>
	</div>
</div>
</body>
</html>