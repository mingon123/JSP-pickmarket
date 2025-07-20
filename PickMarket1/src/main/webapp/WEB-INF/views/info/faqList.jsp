<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>FAQ</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_add.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/il_modi.css">
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
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>자주 묻는 질문</h2>
		<form id="search_form" action="faqList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>제목</option>
						<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>내용</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value="찾기">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='faqList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 FAQ가 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<div id="info-table-wrapper">
		<table id="info-table">
			<thead>
			<tr>
				<th>번호</th>
				<th>FAQ 제목</th>
				<th>등록일</th>
				<th>수정일</th>
			</tr>
			</thead>	
			<tbody>
			<c:forEach var="faq" items="${list}">
			<tr>
				<td>${faq.faq_num}</td>
				<td>
					<a href="faqDetail.do?faq_num=${faq.faq_num}">${faq.faq_title}</a>
				</td>
				<td>
					<fmt:formatDate value="${faq.faq_date}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<c:choose>
							<c:when test="${empty faq.faq_modidate}">
								-
							</c:when>
							<c:otherwise>
								<fmt:formatDate value="${faq.faq_modidate}" pattern="yyyy-MM-dd"/>
							</c:otherwise>
					</c:choose>
				</td>
			</tr>	
			</c:forEach>
			</tbody>
		</table>
		</div>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="메인"
			     onclick="location.href='${pageContext.request.contextPath}/main/main.do'">                        
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>

