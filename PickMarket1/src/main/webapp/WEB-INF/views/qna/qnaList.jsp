<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1:1 문의 내역</title>
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
		<h2><a href="qnaList.do">1:1 문의 내역</a></h2>
		<c:if test="${count == 0}">
		<div class="result-display">
			1:1 문의 내역이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<div class="account-box">
			<div class="qna-header qna-meta">
				<span>번호</span>
				<span>제목</span>
				<span>질문날짜</span>
				<span>답변날짜</span>
			</div>
		
			<ul class="qna-list">
				<c:forEach var="qna" items="${list}" varStatus="status">
					<li class="qna-item qna-meta">
						<span>${count - (startRow - 1) - status.index}</span>
						<span>
							<a href="qnaDetail.do?qna_num=${qna.qna_num}">
								${qna.qna_title}
							</a>
						</span>
						<span>
							<fmt:formatDate value="${qna.q_date}" pattern="yyyy.MM.dd"/>
						</span>
						<span>
							<c:choose>
								<c:when test="${!empty qna.a_date}">
									<fmt:formatDate value="${qna.a_date}" pattern="yyyy.MM.dd"/>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</span>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="user-modify-align-center">
			<input type="button" value="질문등록" onclick="location.href='${pageContext.request.contextPath}/qna/writeQnaForm.do'">
			<input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPage.do'">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>