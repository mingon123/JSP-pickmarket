<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>공지사항 상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#notice_delete_btn').click(function(){
		if(confirm("정말로 이 공지사항을 삭제하시겠습니까?")){
			location.href = 'adminNoticeDelete.do?noti_num=${notice.noti_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${notice.noti_num}번 공지사항 정보</h2>
		<div class="report-detail-box">
			<table>
				<tr>
					<th>제목</th><td>${notice.noti_title}</td>
				</tr>
				<tr>
					<th>조회수</th><td>${notice.noti_view}</td>
				</tr>
				<tr>
					<th>등록일</th>
					<td colspan="3"><fmt:formatDate value="${notice.noti_date}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<th>내용</th><td colspan="3">${notice.noti_content}</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="location.href='adminNoticeList.do'">
			<input type="button" value="수정" onclick="location.href='adminNoticeUpdateForm.do?noti_num=${notice.noti_num}'">
			<input type="button" value="삭제" id="notice_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
