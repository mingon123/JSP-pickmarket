<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${user.nickname} 받은 매너평가</title>
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
		    <c:when test="${user.user_num == rated_num}">
		        <h2>내가 받은 매너평가</h2>
		    </c:when>
		    <c:otherwise>
		        <h2>${nickname} 님이 받은 매너평가</h2>
		    </c:otherwise>
		</c:choose>
		
		<div class="section">
		    <div class="section-header-wrapper">
		        <div class="section-header-box">
		        	<c:choose>
					    <c:when test="${user.user_num == rated_num}">
					        내가 받은 매너평가
					    </c:when>
					    <c:otherwise>
					        ${nickname} 님이 받은 매너평가
					    </c:otherwise>
					</c:choose>
				</div>
		    </div>
		    <hr width="100%" size="1" noshade="noshade">
		    <div class="manner-box">
		        <div class="manner-list">
		            <c:forEach var="manner" items="${list}">
		                <div class="manner-item">
		                	<c:if test="${manner.count>0}">
		                    <span class="manner-count"><b>${manner.count}</b></span>
		                    <span class="manner-op-box"><b>${manner.mannerOp}</b></span>
		                    </c:if>
		                </div>
		            </c:forEach>
		            <c:if test="${empty list}">
		            	<div class="no-manner">받은 매너평가가 없습니다.</div>
		            </c:if>
		        </div>
		    </div>
		</div>
		<div class="user-modify-align-center">
			<c:choose>
			    <c:when test="${user.user_num == rated_num}">
			        <input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPageUserTemp.do'">
			    </c:when>
			    <c:otherwise>
			        <input type="button" value="뒤로가기" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${rated_num}'">
			    </c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>