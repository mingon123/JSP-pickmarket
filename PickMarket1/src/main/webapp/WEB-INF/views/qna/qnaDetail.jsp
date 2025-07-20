<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1:1 문의 내역 상세 조회</title>
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
				location.replace('deleteQna.do?qna_num=${qna.qna_num}');
			}
		};
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>1:1 문의 내역 상세 조회</h2>
		<div class="account-box">
			<div class="qna-header qna-detail">
				<span class="fontsize20">제목 : ${qna.qna_title}</span>
				<span class="qna-date">작성일:<fmt:formatDate value="${qna.q_date}" pattern="yyyy.MM.dd"/></span>
			</div>

			<div class="qna-content">
				<span>${qna.qna_content}</span>
			</div>
			<hr width="100%" size="1" noshade="noshade">
			
			<c:choose>
				<c:when test="${empty qna.qna_re}">
				<div class="qna-content"><b>답변 없음</b></div>
				</c:when>
				<c:otherwise>
				<div class="qna-header qna-detail">
					<span class="qna-content fontsize20">답변</span>
					<c:if test="${!empty qna.a_date}">
					<span class="qna-date">답변일:<fmt:formatDate value="${qna.a_date}" pattern="yyyy.MM.dd"/></span>
					</c:if>
				</div>
	
				<div class="qna-reply">
					${qna.qna_re}
				</div>
				<hr width="100%" size="1" noshade="noshade">
				</c:otherwise>
			</c:choose>
		</div>
		<div class="user-modify-align-center">
			<input type="button" value="질문수정" onclick="location.href='${pageContext.request.contextPath}/qna/updateQnaForm.do?qna_num=${qna.qna_num}'">
			<input type="button" value="질문삭제" id="delete_btn">
			<input type="button" value="질문목록" onclick="location.href='${pageContext.request.contextPath}/qna/qnaList.do'">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>