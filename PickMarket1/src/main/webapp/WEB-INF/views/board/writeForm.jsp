<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 게시판 등록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw_board_writeform.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		//이벤트 연결
		$('#write_form').submit(function(){
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
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>커뮤니티 글 등록</h2><br>
		<form id="write_form" action="write.do" method="post" enctype="multipart/form-data">
			<ul>
				<li>
					<label for="btitle">제목</label>
					<input type="text" name="btitle" id="btitle" maxlength="50" autocomplete="off">
				</li>
				<li>
					<label for="bcontent">내용</label>
					<textarea rows="5" cols="40" name="bcontent" id ="bcontent" autocomplete="off"></textarea>
				</li>
				<li>
					<label for ="bfilename">이미지</label>
					<input type="file" name="bfilename" id="bfilename" accept="image/gif,image/png,image/jpeg">
				</li>
			</ul>
			<div class="align-center">
				<input type="submit" value="등록">
				<input type="button" value="목록" onclick="location.href='boardList.do'">
			</div>
		</form>	
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>