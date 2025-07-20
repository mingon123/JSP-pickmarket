<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>공지사항</title>
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
		<h2>공지사항</h2>
		<form id="search_form" action="noticeList.do" method="get">
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
					<input type="button" value="초기화" onclick="location.href='noticeList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 공지사항이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<div id="info-table-wrapper">
		<table id="info-table">
			<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>등록일</th>
				<th>조회수</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="notice" items="${list}">
			<tr>
				<td>${notice.noti_num}</td>
				<td>
					<a href="noticeDetail.do?noti_num=${notice.noti_num}">${notice.noti_title}</a>
				</td>
				<td>
					<fmt:formatDate value="${notice.noti_date}" pattern="yyyy-MM-dd"/>
				</td>
				<td>${notice.noti_view}</td>
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
