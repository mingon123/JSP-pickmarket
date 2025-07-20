<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 글 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		//이벤트 연결
		$('#modify_form').submit(function(){
			if($('#btitle').val().trim()==''){
				alert('제목을 입력하세요');
				$('#btitle').val('').focus();
				return false;
			}
			if($('#bcontent').val().trim()==''){
				alert('내용을 입력하세요');
				$('#bcontent').val('').focus();
				return false;
			}
			if($('#nickname').val().trim()==''){
				alert('닉네임을 입력하세요');
				$('#nickname').val('').focus();
				return false;
			}
			if($('#bhit').val().trim()==''){
				alert('조회수를 입력하세요');
				$('#bhit').val('').focus();
				return false;
			}
			if($('#breg_date').val().trim()==''){
				alert('작성일을 입력하세요');
				$('#breg_date').val('').focus();
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
		<h2>${board.board_num} 커뮤니티 글 수정</h2>
		<form action="adminBoardModify.do" method="post" id="modify_form" enctype="multipart/form-data">
			<input type="hidden" name="board_num" value="${board.board_num}">
			<ul>
				<li>
					<label for ="btitle">제목</label>
					<input type="text" name="btitle" id="btitle" maxlength="30" value="${board.btitle}" class="input-check">
				</li>
				<li>
					<label for ="bhit">조회수</label>
					<input type="number" name="bhit" id="bhit" min="0" max="9999" value="${board.bhit}" class="input-check">
				</li>
				<li>
					<label for ="nickname">작성자</label>
					<input type="text" name="nickname" id="nickname" maxlength="10" value="${board.nickname}" class="input-check">
				</li>
				<li>
					<label for ="bfilename">이미지</label>
					<input type="file" name="bfilename" id="bfilename" accept="image/gif,image/png,image/jpeg" value="${board.bfilename}">
				</li>
				<li>
					<label for="bcontent">내용</label>
					<textarea rows="5" cols="30" name="bcontent" id="bcontent" class="input-check">${board.bcontent}</textarea>
				</li>				
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="수정" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminBoardList.do'">
			</div>
		</form>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>