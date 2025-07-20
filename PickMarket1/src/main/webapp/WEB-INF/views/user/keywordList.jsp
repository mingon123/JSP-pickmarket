<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>키워드 알림 설정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#insert_form').submit(function(){
		if($('#keyword').val().trim()==''){
			alert('추가할 검색어를 입력하세요!');
			$('#keyword').val('').focus();
			return false;
		}
	});	
	
});

function deleteKeyword(k_word) {
    if(confirm('정말 삭제하시겠습니까?')) {
        location.href = 'deleteKeyword.do?k_word=' + encodeURIComponent(k_word);
    }
}
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>키워드 알림 설정<small>(최대 3개)</small></h2>
		<form id="insert_form" action="insertKeyword.do" method="post">
			<ul class="search">
				<li>
					<label for="keyword">등록할 키워드 입력</label>
					<input type="text" name="k_word" id="keyword" autocomplete="off" placeholder="키워드를 입력하세요">
				</li>
				<li>
					<input type="submit" value="등록">
				</li>
			</ul>
		</form>
		<c:if test="${count == 0 }">
		<div class="result-display">
			등록된 키워드가 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<c:forEach var="keyword" items="${list}">
		    <div class="account-box">
		        <div class="account-list1">
		            ${keyword.k_word}
		            <input type="button" value="삭제" class="delete-btn" onclick="deleteKeyword('${keyword.k_word}')">
		        </div>
		    </div>
		</c:forEach>
		</c:if>
		<div class="user-modify-align-center">
			<input type="button" value="마이페이지" onclick="location.href='myPage.do'">
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>