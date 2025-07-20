<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내가 쓴 게시물 목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="myBoardList.do">내가 쓴 게시물 목록</a></h2>
		<c:if test="${count == 0}">
		<div class="result-display">
			등록한 게시물이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
        	<c:forEach var="board" items="${list}">
				<table class="board-table">
				    <tr>
				    	<td onclick="location.href='boardDetail.do?board_num=${board.board_num}'" style="cursor: pointer;">
				      		<c:if test="${empty board.bfilename}">
			                    <img src="${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png" class="board-thumbnail">
			                </c:if>
			                <c:if test="${!empty board.bfilename}">
			                    <img src="${pageContext.request.contextPath}/upload/${board.bfilename}" class="board-thumbnail"
			                    	onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png';">
			                </c:if>
			      			<div style="flex: 1;">
			        			<div style="float: left;"><b>${board.btitle}</b></div>
		        				<div style="float: right;">
						          	<small>글 등록일:${board.breg_date}</small>
						          	<c:if test="${empty board.bmodi_date}">
						          		<small>/최근 수정일:${board.breg_date}</small>
			                		</c:if>
					        	</div>
					        	<br><hr size="1" noshade width="100%">
					        	${board.bcontent}
				        	<div class="board-stats">조회수 ${board.bhit} 좋아요 ${board.like_count} 댓글 ${board.reply_count}</div>
					      	</div>
						</td>
				  	</tr>
				</table>
        	</c:forEach>
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