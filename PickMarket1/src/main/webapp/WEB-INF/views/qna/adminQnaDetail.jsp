<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>QNA 상세정보</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#qna_delete_btn').click(function(){
		if(confirm("정말로 이 QNA를 삭제하시겠습니까?")){
			location.href = 'adminQnaDelete.do?qna_num=${qna.qna_num}';
		}
	});
	
});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${qna.qna_num}번 1:1문의 정보</h2>
		<div class="report-detail-box">
			<table>
				<tr>
					<th>제목</th><td>${qna.qna_title}</td>
				</tr>
				<tr>
					<th>문의자</th><td>${qna.nickname}</td>
				</tr>
				<tr>
					<th>등록일</th>
					<td colspan="3"><fmt:formatDate value="${qna.q_date}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<th>답변일</th>
					<td colspan="3">
					<c:choose>
							<c:when test="${empty qna.a_date}">
								-
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${qna.a_date}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
					</c:choose>
					</td>
				</tr>
				<tr>
					<th>문의 내용</th><td colspan="3">${qna.qna_content}</td>
				</tr>
				<tr>
					<th>답변 내용</th>
					<td colspan="3">
					<c:choose>
							<c:when test="${empty qna.qna_re}">
								-
							</c:when>
							<c:otherwise>
								${qna.qna_re}
							</c:otherwise>
					</c:choose>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="btn-box align-center">
			<input type="button" value="목록" onclick="location.href='adminQnaList.do'">
			<c:choose>
				<c:when test="${empty qna.qna_re}">
					<input type="button" value="답변작성" onclick="location.href='adminQnaAnswerUpdateForm.do?qna_num=${qna.qna_num}'">
				</c:when>
				<c:otherwise>
					<input type="button" value="답변수정" onclick="location.href='adminQnaAnswerUpdateForm.do?qna_num=${qna.qna_num}'">
					<input type="button" value="답변삭제" onclick="location.href='adminQnaAnswerDelete.do?qna_num=${qna.qna_num}'">
				</c:otherwise>
			</c:choose>
			<input type="button" value="삭제" id="qna_delete_btn">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
