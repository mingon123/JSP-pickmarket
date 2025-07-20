<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${nickname}님이 받은 거래후기</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<c:choose>
		    <c:when test="${user.user_num == userNum}">
		        <h2>내가 받은 거래후기</h2>
		    </c:when>
		    <c:otherwise>
		        <h2>${nickname}님이 받은 거래후기</h2>
		    </c:otherwise>
		</c:choose>
		
		<!-- 내가 받은 거래후기 -->
		<div class="section">
		    <div class="section-header-wrapper">
		        <div class="section-header-box">
		        	<c:choose>
					    <c:when test="${user.user_num == userNum}">
					        내가 받은 거래후기 (${reviewCount})
					    </c:when>
					    <c:otherwise>
					        ${nickname} 님이 받은 거래후기 (${reviewCount})
					    </c:otherwise>
					</c:choose>
		        </div>
		    </div>
			<hr width="100%" size="1" noshade="noshade">
		    
		    <div class="review-list">
		        <c:forEach var="review" items="${list}">
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
		        <div class="align-center">${page}</div>
		        <div class="manner-box">
			        <div class="manner-list">
				        <c:if test="${empty list}">
			            <div class="no-manner">받은 거래후기가 없습니다.</div>
				        </c:if>
				    </div>
		    	</div>
		    </div>
		</div>
		<div class="user-modify-align-center">
			<c:choose>
			    <c:when test="${user.user_num == userNum}">
			        <input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPageUserTemp.do'">
			    </c:when>
			    <c:otherwise>
			        <input type="button" value="뒤로가기" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${userNum}'">
			    </c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>