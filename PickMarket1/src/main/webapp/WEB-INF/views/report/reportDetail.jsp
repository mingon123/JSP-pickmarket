<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고 내역 상세 조회</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		const delete_btn = document.getElementById('delete_btn');
		
		delete_btn.onclick=function(){
			let choice = confirm('삭제하시겠습니까?');
			if(choice){
				location.replace('deleteReport.do?report_num=${report.report_num}');
			}
		};
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${report.report_title}</h2>
		<div class="account-box">
			<div class="qna-header qna-detail">
				<span class="fontsize20">신고 대상 : ${report.nickname}</span>
				<span class="qna-date">신고일:<fmt:formatDate value="${report.report_date}" pattern="yyyy.MM.dd"/></span>
			</div>

			<div class="qna-content">
				<div class="align-center">
					<img src="${pageContext.request.contextPath}/upload/${report.report_img}" class="detail-img">
				</div>
				<p>
				<span>${report.report_content}</span>
			</div>
		</div>
		<div class="user-modify-align-center">
			<input type="button" value="신고수정" onclick="location.href='${pageContext.request.contextPath}/report/updateReportForm.do?report_num=${report.report_num}'">
			<input type="button" value="신고삭제" id="delete_btn">
			<input type="button" value="신고목록" onclick="location.href='${pageContext.request.contextPath}/report/reportList.do'">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>