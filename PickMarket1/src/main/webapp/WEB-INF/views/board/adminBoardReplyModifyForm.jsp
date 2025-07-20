<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 댓글 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		//이벤트 연결
		$('#modify_form').submit(function(){
			if($('#breply_content').val().trim()==''){
				alert('내용을 입력하세요');
				$('#breply_content').val('').focus();
				return false;
			}
		});
	});
</script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${board.breply_num} 커뮤니티 댓글 수정</h2>
		<form action="adminBoardReplyModify.do" method="post" id="modify_form">
			<input type="hidden" name="board_num" value="${board.board_num}">
			<input type="hidden" name="breply_num" value="${board.breply_num}">
			<ul>
				<li>
				<c:if test="${!empty board.breply_modidate}">
				최근 수정일 : ${board.breply_modidate}<br>
				</c:if>
				작성일:${board.breply_date}<br>
				글제목 : ${board.boardVO.btitle}
				</li>
								
				<li>
					<label for="breply_content">내용</label>
					<textarea rows="5" cols="30" name="breply_content" id="breply_content" class="input-check">${board.breply_content}</textarea>
				</li>				
			</ul>
			<div class="btn-box align-center">
				<input type="button" value="글 상세보기" onclick="location.href='adminBoardDetail.do?board_num=${board.board_num}'">
				<input type="submit" value="수정" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminBoardList.do'">
				<script type="text/javascript">
					const modify_btn = document.getElementById('modify_btn');
					//이벤트 연결
					modify_btn.onclick=function(){
						let choice = confirm('수정하시겠습니까?');
						if(choice){
							location.replace('adminBoardReplyDetail.do?board_num=${board.breply_num}');							
						}
					};
				</script>
				
			</div> 
			
		</form>
		
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>