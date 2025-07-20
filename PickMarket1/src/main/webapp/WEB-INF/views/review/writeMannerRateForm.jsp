<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>매너 평가하기</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
	    $(function(){
	        $('#manner-form').on('submit', function(event) {
	            if ($('input[name="manner"]:checked').length === 0) {
	                alert("하나 이상의 항목을 선택하세요.");
	                event.preventDefault();
	            }
	        });
	    });	
	});
</script>
</head>
<body>
<div class="page-main1">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="mypage-div">
		<h3>매너 평가하기</h3>
		<div class="mypage-profile-wrapper">
			<div class="mypage-photo">
				<c:if test="${empty user.photo}">
				<img src="${pageContext.request.contextPath}/images/face.png" width="150" height="150" class="my-photo">
				</c:if>
				<c:if test="${!empty user.photo}">
				<img src="${pageContext.request.contextPath}/upload/${user.photo}" width="150" height="150" class="my-photo">
				</c:if>
			</div>
			<div class="mypage-basic-info">
				<ul>
					<li>닉네임 : <b>${user.nickname}</b></li>
					<li>동네 : ${user.locationVO.region_nm}</li>
				</ul>
			</div>
			
			<div class="manner-score">
				<div class="manner-circle1">
					<div class="manner-value">36.5°</div>
					<div class="manner-label">매너온도</div>
				</div>
			</div>
		</div>
		<hr width="100%" size="1" noshade="noshade">
		
		<form id="manner-form" action="writeMannerRate.do" method="post">
			<input type="hidden" name="rated_num" value="${user.user_num}">
			<input type="hidden" name="from" value="${param.from}">
			<input type="hidden" name="product_num" value="${product.product_num}">
			<input type="hidden" name="chatroom_num" value="${chatroom_num}">
			<div class="account-box">
			    <div class="account-title">어떤 점이 좋았나요?</div>
			    <ul class="account-list">
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="1"> 친절하고 매너가 좋아요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="2"> 시간 약속을 잘 지켜요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="3"> 응답이 빨라요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="4"> 물품상태가 설명한 것과 같아요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="5"> 나눔을 해주셨어요.
			            </label>
			        </li>
			    </ul>
			</div>
	
			<div class="account-box">
			    <div class="account-title">어떤 점이 별로였나요?</div>
			    <ul class="account-list">
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="6"> 불친절해요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="7"> 시간약속을 안지켜요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="8"> 채팅 메시지를 읽고도 답이 없어요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="9"> 약속장소에 나타나지 않았어요.
			            </label>
			        </li>
			        <li>
			            <label>
			                <input type="checkbox" name="manner" value="10"> 약속후 직전 취소했어요.
			            </label>
			        </li>
			    </ul>
			</div>
			<div class="user-modify-align-center">
				<input type="submit" value="평가하기">
				<c:choose>
					<c:when test="${!empty product}">
						<input type="button" value="취소" onclick="location.href='${pageContext.request.contextPath}/chat/chatDetail.do?product_num=${product.product_num}'">
					</c:when>
					<c:otherwise>
						<input type="button" value="취소" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${user.user_num}'">
					</c:otherwise>
				</c:choose>
			</div>
		</form>
	</div>
</div>
</body>
</html>