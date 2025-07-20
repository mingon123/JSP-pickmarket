<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>채팅방목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
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
		<h2>채팅방관리</h2>
		<form id="search_form" action="adminChatRoomList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>닉네임</option>
					</select>
				</li>
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>
				<li>
					<input type="submit" value="찾기">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='adminChatRoomList.do'">
				</li>
			</ul>
		</form>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 채팅방이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<table>
			<tr>
				<td>채팅방번호</td>
				<td>판매글번호</td>
				<td>판매자</td>
				<td>구매희망자</td>
			</tr>
			<c:forEach var="chatroom" items="${list}">
			<tr>
				<td>
					<a href="adminChatDetail.do?chatroom_num=${chatroom.chatroom_num}">${chatroom.chatroom_num}</a>
				</td>
				<td>
					<a href="${pageContext.request.contextPath}/product/productModifyForm.do?product_num=${chatroom.product_num}">${chatroom.product_num}</a>
				</td>
				<td>
					<a href="${pageContext.request.contextPath}/user/adminUserDetail.do?user_num=${chatroom.seller_num}">${chatroom.seller.nickname}</a>
				</td>
				<td>
					<a href="${pageContext.request.contextPath}/user/adminUserDetail.do?user_num=${chatroom.buyer_num}">${chatroom.buyer.nickname}</a>
				</td>
			</tr>	
			</c:forEach>
		</table>
		<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/user/adminPage.do'">
			<input type="button" value="메인"
			     onclick="location.href='${pageContext.request.contextPath}/main/main.do'">                        
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>








