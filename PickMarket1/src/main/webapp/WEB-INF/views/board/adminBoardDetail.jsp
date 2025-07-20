<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 글 상세</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/he.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw_admin.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class= "content-main">
		<h2>${board.board_num}번 커뮤니티 글 상세</h2><br>
			<input type="hidden" name="board_num" value="${board.board_num}">
				<div class="user-detail-box">
					<table>
						<tr>
							<th>제목</th>
							<td>${board.btitle}</td>
						</tr>
						<tr>
							<th>조회수</th>
							<td>${board.bhit}</td>
						</tr>
						<tr>
							<th>작성자</th>
							<td>${board.nickname}</td>
						</tr>
						<tr>
							<th>최근 수정일</th>
							<td>
							<c:choose>
    							<c:when test="${!empty board.bmodi_date}">
      								${board.bmodi_date}
    							</c:when>
    							<c:otherwise>
      							 -	
    							</c:otherwise>
  							</c:choose>
  							</td>
						</tr>
						<tr>
							<th>작성일</th>
							<td>${board.breg_date}</td>
						</tr>
						<tr>
							<th>내용</th>
							<td>${board.bcontent}</td>
						</tr>
						<tr>
							<th>사진</th>
							<td>
							<c:if test="${!empty board.bfilename}">
								<img src="${pageContext.request.contextPath}/upload/${board.bfilename}" class="detail-img"
									onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png';">
							</c:if>
							</td>
						</tr>
					</table>
			<div class="btn-box align-center">
				<input type="button" value="댓글목록보기" onclick="location.href='adminBoardReplyList.do?keyfield=4&keyword=${board.board_num}'">
				<input type="button" value="목록" onclick="location.href='adminBoardList.do'">
				<input type="button" value="수정" onclick="location.href='adminBoardModifyForm.do?board_num=${board.board_num}'">
				<input type="button" value="삭제" id="delete_btn">
				<script type="text/javascript">
					const delete_btn = document.getElementById('delete_btn');
					//이벤트 연결
					delete_btn.onclick=function(){
						let choice = confirm('삭제하시겠습니까?');
						if(choice){
							location.replace('adminBoardDelete.do?board_num=${board.board_num}');							
							}
						};
					</script>
				</div>
			</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>	
</body>
</html>