<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내가 쓴 댓글</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class= "page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="myBoardReplyList.do">내가 쓴 댓글</a></h2>
		<c:if test="${count == 0}">
		<div class="result-display">
			등록한 댓글이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<div class="account-box">
			<div class="qna-header qna-meta">
				<span>글 작성자</span>
				<span>글 제목</span>
				<span>댓글내용</span>
				<span>등록일</span>
			</div>
		
			<ul class="qna-list">
				<c:forEach var="breply" items="${list}">
					<li class="qna-item qna-meta">
						<span>${breply.nickname}</span>
						<span>
							<a href="boardDetail.do?board_num=${breply.board_num}">
								${breply.btitle}
							</a>
						</span>
						<span>${breply.breply_content}</span>
						<span>${breply.breply_date}</span>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="user-modify-align-center">
			<input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPage.do'">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>