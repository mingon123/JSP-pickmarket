<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내가 좋아요한 글</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_add.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="myBoardList.do">내가 좋아요한 글</a></h2>
		<c:if test="${count == 0}">
		<div class="result-display">
			내가 좋아요한 글이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
        	<c:forEach var="boardFav" items="${list}">
				<table class="board-table">
				    <tr>
						<td onclick="location.href='boardDetail.do?board_num=${boardFav.board_num}'" style="cursor: pointer;">
						  	<div class="board-fav-thumbnail-wrapper">
								<c:choose>
							    	<c:when test="${empty boardFav.boardVO.bfilename}">
							        	<img src="${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png" class="board-fav-thumbnail" />
							      	</c:when>
							      	<c:otherwise>
							        	<img src="${pageContext.request.contextPath}/upload/${boardFav.boardVO.bfilename}" class="board-fav-thumbnail" 
							        		onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png';"/>
							      	</c:otherwise>
							    </c:choose>
							    <div class="heart-icon">
							    	<img src="${pageContext.request.contextPath}/images/heart.png" width="20">
							    </div>
							</div>
			      			<div style="flex: 1;">
			        			<div style="float: left;"><b>${boardFav.boardVO.btitle}</b></div>
		        				<div style="float: right;">
						          	<small>글 등록일:${boardFav.boardVO.breg_date}</small>
						          	<c:if test="${empty boardFav.boardVO.bmodi_date}">
						          		<small>/최근 수정일:${boardFav.boardVO.breg_date}</small>
			                		</c:if>
					        	</div>
					        	<br><hr size="1" noshade width="100%">
					        	${boardFav.boardVO.bcontent}
				        	<div class="board-stats">조회수 ${boardFav.boardVO.bhit} 좋아요 ${boardFav.boardVO.like_count} 댓글 ${boardFav.boardVO.reply_count}</div>
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