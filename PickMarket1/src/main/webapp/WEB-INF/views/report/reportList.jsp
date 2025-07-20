<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>신고 목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요!');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
	
	function deleteReport(nickname) {
	    if(confirm('정말 삭제하시겠습니까?')) {
	        location.href = 'deleteReport.do?nickname=' + encodeURIComponent(nickname);
	    }
	}
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="reportList.do">신고 목록</a></h2>
		<c:if test="${count == 0}">
		<div class="result-display">
			신고한 내역이 없습니다.
		</div>
		</c:if>
		
		<c:if test="${count > 0}">
		<form id="search_form" action="reportList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>신고대상</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>신고제목</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value="검색">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='reportList.do'">
				</li>
			</ul>
		</form>
		<div class="account-box">
			<div class="qna-header qna-meta">
				<span>번호</span>
				<span>신고 제목</span>
				<span>신고 대상</span>
				<span>신고일</span>
			</div>
		
			<ul class="qna-list">
				<c:forEach var="report" items="${list}" varStatus="status">
					<li class="qna-item qna-meta">
						<span>${count - (startRow - 1) - status.index}</span>
						<span>
							<a href="reportDetail.do?report_num=${report.report_num}">
								${report.report_title}
							</a>
						</span>
						<span>${report.nickname}</span>
						<span>
							<fmt:formatDate value="${report.report_date}" pattern="yyyy.MM.dd"/>
						</span>
					</li>
				</c:forEach>
			</ul>
		</div>
		</c:if>
		<div class="user-modify-align-center">
			<c:choose>
			    <c:when test="${user.user_num == userNum}">
			        <input type="button" value="MyPage" onclick="location.href='${pageContext.request.contextPath}/user/myPageUserTemp.do'">
			    </c:when>
			    <c:otherwise>
			        <input type="button" value="마이페이지" onclick="location.href='${pageContext.request.contextPath}/user/myPage.do'">
			    </c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>