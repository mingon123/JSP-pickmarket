<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>운영정책 상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#op_delete_btn').click(function(){
		if(confirm("정말로 이 운영정책을 삭제하시겠습니까?")){
			location.href = 'adminOPDelete.do?pol_num=${op.pol_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${op.pol_num}번 운영정책 상세</h2>
		<div class="report-detail-box">
			<table>
				<tr>
					<th>제목</th><td>${op.pol_title}</td>
				</tr>
				<tr>
					<th>등록일</th>
					<td colspan="3"><fmt:formatDate value="${op.pol_date}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<th>수정일</th>
					<td colspan="3">
					<c:choose>
							<c:when test="${empty op.pol_modi_date}">
								-
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${op.pol_modi_date}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
					</c:choose>
					</td>
				</tr>
				<tr>
					<th>내용</th><td colspan="3">${op.pol_content}</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="location.href='adminOPList.do'">
			<input type="button" value="수정" onclick="location.href='adminOPUpdateForm.do?pol_num=${op.pol_num}'">
			<input type="button" value="삭제" id="op_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
