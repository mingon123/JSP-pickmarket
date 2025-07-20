<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>신고상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#report_delete_btn').click(function(){
		if(confirm("정말로 이 신고를 삭제하시겠습니까?")){
			location.href = 'adminReportDelete.do?report_num=${report.report_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${report.report_num}번 신고 정보</h2>
		
		<div class="report-detail-box">
			<table>
				<tr>
					<th>피신고자 닉네임</th><td><a href="${pageContext.request.contextPath}/user/adminUserDetail.do?user_num=${report.suspect_num}">${report.nickname}</a></td>
					<th>신고자 닉네임</th><td><a href="${pageContext.request.contextPath}/user/adminUserDetail.do?user_num=${report.reporter_num}">${report.reporter_nickname}</a></td>
				</tr>
				<tr>
					<th>신고 제목</th><td colspan="3">${report.report_title}</td>
				</tr>
				<tr>
					<th>신고 내용</th><td colspan="3">${report.report_content}</td>
				</tr>
				<tr>
					<th>등록일</th>
					<td colspan="3"><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<th rowspan="2">사진</th>
					<td rowspan="2" colspan="3">
						<c:if test="${not empty report.report_img}">
							<img src="${pageContext.request.contextPath}/upload/${report.report_img}" width="400" height="400" 
								onerror="this.src='${pageContext.request.contextPath}/images/face.png'"  width="400" height="400">
						</c:if>
						<c:if test="${empty report.report_img}">
							<img src="${pageContext.request.contextPath}/images/face.png" width="400" height="400">
						</c:if>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="history.back()">
			<input type="button" value="삭제" id="report_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
