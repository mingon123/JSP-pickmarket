<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 댓글 상세</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/he.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class= "content-main">
		<h2>${board.breply_num}번 커뮤니티 댓글 상세</h2><br>
			<div class="user-detail-box">
				<table>
					<tr>
						<th>작성자</th>
						<td>${board.nickname}</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>${board.breply_content}</td>
					</tr>
					<tr>
						<c:if test="${!empty board.breply_modidate}">
						<th>최근 수정일</th>
						<td>${board.breply_modidate}</td>
						</c:if>
					</tr>
					<tr>
						<th>작성일</th>
						<td>${board.breply_date}</td>
					</tr>
					<tr>
						<th>ip</th>
						<td>${board.breply_ip}</td>
					</tr>
					<tr>
						<th>글번호</th>
						<td>${board.board_num}</td>
					</tr>
				</table>
			</div>
			<div class="btn-box align-center">
				<input type="button" value="글 상세보기" onclick="location.href='adminBoardDetail.do?board_num=${board.board_num}'">
				<input type="button" value="목록" onclick="location.href='adminBoardReplyList.do'">
				<input type="button" value="수정" onclick="location.href='adminBoardReplyModifyForm.do?breply_num=${board.breply_num}'">
				<input type="button" value="삭제" id="delete_btn">
				<script type="text/javascript">
					const delete_btn = document.getElementById('delete_btn');
					//이벤트 연결
					delete_btn.onclick=function(){
						let choice = confirm('삭제하시겠습니까?');
						if(choice){
							location.replace('adminBoardReplyDelete.do?board_num=${board.breply_num}');							
						}
					};
				</script>	
			</div>		
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>