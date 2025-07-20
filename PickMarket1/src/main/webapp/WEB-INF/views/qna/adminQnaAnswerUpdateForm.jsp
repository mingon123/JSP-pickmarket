<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>QNA 답변 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#modify_form').submit(function(){
    	const items = document.querySelectorAll('.input-check');
		for(let i=0;i<items.length;i++){
			if(items[i].value.trim()==''){
				const label = document.querySelector('label[for="'+items[i].id+'"]');
				alert(label.textContent + ' 입력 필수');
				items[i].value='';
				items[i].focus();
				return false;
			}//end of if
		}
	});
});
</script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
	<c:set var="reContent" value="" />
	<c:set var="check" value="작성" />
		<c:if test="${not empty qna.a_date}">
    		<c:set var="reContent" value="${qna.qna_re}" />
    		<c:set var="check" value="수정" />
		</c:if>
		<h2>${qna.qna_num}번 1:1문의 답변 ${check}</h2>
		<form id="modify_form" action="adminQnaAnswerUpdate.do" method="post">
			<ul>
				<li>
					<input type="hidden" name="qna_num" id="qna_num" value="${qna.qna_num}">
				</li>
				<li>
					<label for="a_date">최신 답변일</label>
					<input type="hidden" name="a_date" id="a_date" value="${qna.a_date}" readonly>
				</li>
				<li>
					<label>문의 제목</label>
					${qna.qna_title}
				</li>
				<li>
					<input type="hidden" name="qna_title" id="qna_title" value="${qna.qna_title}">
				</li>
				<li>
					<label for="qna_content">문의 내용</label>
					${qna.qna_content}
				</li>
				<li>
					<label for="qna_re">답변 내용</label>
					
					<textarea rows="6" cols="40" name="qna_re" id="qna_re" class="input-check" autocomplete="off">${reContent}</textarea>
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="${check}" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminQnaList.do'" class="simple-button">
			</div>                                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
