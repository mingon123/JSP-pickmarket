<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#update_form').submit(function(){
			if($('#title').val().trim()==''){
				alert('제목을 입력하세요!');
				$('#title').val('').focus();
				return false;
			}
			if($('#content').val().trim()==''){
				alert('내용을 입력하세요!');
				$('#content').val('').focus();
				return false;
			}
			if($('#report_img').val().trim()=='' && $('#file_detail').length == 0){
	            alert('사진을 첨부하세요!');
	            $('#report_img').val('').focus();
	            return false;
	        }
		})
	});
</script>
</head>
<body>
<div class="page-main2">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main2" id="user-modify-main">
		<h2>신고수정</h2>
		<form id="update_form" action="updateReport.do" method="post" enctype="multipart/form-data">
			<input type="hidden" name="report_num" value="${report.report_num}">
			<ul>
				<li>
					<label for="nickname">신고 대상</label>
					<input type="text" id="nickname" value="${report.nickname}" readonly>
				</li>
				<li>
					<label for="title">신고 제목</label>
					<input type="text" name="report_title" id="title" maxlength="50" value="${report.report_title}">
				</li>
				<li>
					<label for="content">신고 내용</label>
					<textarea rows="5" cols="40" name="report_content" id="content" class="user-modify-input">${report.report_content}</textarea>
				</li>
				<li>
					<label for="report_img">신고 이미지</label>
					<input type="file" name="report_img" id="report_img" accept="image/gif,image/png,image/jpeg" style="padding-bottom: 10px;">
					<c:if test="${!empty report.report_img}">
					<div id="file_detail">
						<img src="${pageContext.request.contextPath}/upload/${report.report_img}" width="100">
						<input type="hidden" name="report_img_original" value="${report.report_img}">
						<input type="button" value="파일삭제" id="file_del">
						<script type="text/javascript">
							$(function(){
								$('#file_del').click(function(){
									let choice = confirm('삭제하시겠습니까?');
									if(choice){
										$.ajax({
											url:'deleteFile.do',
											type:'post',
											data:{report_num:${report.report_num}},
											dataType:'json',
											success:function(param){
												if(param.result == 'logout'){
													alert('로그인 후 사용하세요');
												}else if(param.result == 'success'){
													$('#file_detail').remove();
												}else if(param.result == 'wrongAccess'){
													alert('잘못된 접속입니다.');
												}else{
													alert('파일 삭제 오류 발생');
												}
											},
											error:function(){
												alert('네트워크 오류 발생');
											}
										})
									}
								});
							});
						</script>
					</div>
					</c:if>
				</li>
			</ul>
			<div class="user-modify-align-center">
				<input type="submit" value="수정">
				<input type="button" value="목록" onclick="location.href='reportList.do'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>