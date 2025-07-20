<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>알림 목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_notifi.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
const contextPath = '${pageContext.request.contextPath}';
</script>
<script type="text/javascript">
$(function() {
	// 알림 삭제 버튼 이벤트 연결
	$(document).on('click', '.delete-noti-btn', function(e){
		e.preventDefault();       // 링크 방지
	    e.stopPropagation();      // 부모 알림 링크 이동 방지

	    const notifiNum = $(this).data('noti-num');
	    if (!confirm('이 알림을 삭제하시겠습니까?')) return;

	    $.ajax({
	      url: contextPath + '/notification/deleteNotification.do',
	      type: 'post',
	      data: { notifi_num: notifiNum },
	      dataType: 'json',
	      success: function(res){
	        if (res.result === 'success') {
	          // 알림 박스를 제거
	          $(`button[data-noti-num="${notifiNum}"]`).closest('.noti-box').parent().remove();
	          location.reload(); 
	        } else {
	          alert('삭제에 실패했습니다.');
	        }
	      },
	      error: function(){
	        alert('네트워크 오류 발생');
	      }
	    });
	 });
	
	 // 전체 삭제 버튼 클릭
	 $('#delete-all-btn').click(function(){
	  	if(!confirm('모든 알림을 삭제하시겠습니까?')) return;

	  	$.ajax({
	        url: contextPath + '/notification/deleteAllNotification.do',
	        type: 'post',
	        dataType: 'json',
	        success: function(res){
	            if(res.result === 'success'){
	                alert('모든 알림이 삭제되었습니다.');
	                location.reload();
	            }else if(res.result === 'logout'){
	                alert('로그인이 필요합니다.');
	            }else{
	                alert('삭제 실패');
	            }
	     	},
	     	error: function(){
	     		alert('네트워크 오류 발생');
	     	}
	   	 });
	  });
	 
	 // 전체 읽음 버튼 클릭
	 $('#mark-read-all-btn').click(function(){
	  	if(!confirm('모든 알림을 읽음처리 하시겠습니까?')) return;

	  	$.ajax({
	        url: contextPath + '/notification/updateReadAllNotification.do',
	        type: 'post',
	        dataType: 'json',
	        success: function(res){
	            if(res.result === 'success'){
	                alert('모든 알림이 읽음처리되었습니다.');
	                location.reload();
	            }else if(res.result === 'logout'){
	                alert('로그인이 필요합니다.');
	            }else{
	                alert('읽음처리 실패');
	            }
	     	},
	     	error: function(){
	     		alert('네트워크 오류 발생');
	     	}
	   	 });
	  });
	 
	 $('.noti-link').click(function(e){
		  e.preventDefault();
		  
		  const url = $(this).attr('href');
		  const notifiNum = $(this).data('noti-num');
		  
		  const newTab = window.open('about:blank');

		  $.ajax({
		      url: contextPath+'/notification/updateReadNotification.do',
		      type: 'post',
		      data: { notifi_num: notifiNum },
		      dataType: 'json',
		      success: function(res){
		        if(res.result === 'success'){
		        	location.reload();
		        	newTab.location.href = url;
		        }else if(res.result === 'logout'){
		          	alert('로그인이 필요합니다.');
		          	newTab.close();
		        }else{
		          	alert('읽음 처리 실패');
		          	newTab.close();
		        }
		      },
		      error: function(){
		        alert('네트워크 오류 발생');
		        newTab.close();
		      }
		   });
	  });
	  
	
});
</script>

</head>
<body id="noti-page">
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2><a href="notificationList.do">알림 목록</a></h2>
		<!-- 알림 유형 탭 -->
    	<div class="noti-tabs">
        	<a href="notificationList.do?type=all" class="${selectedType eq 'all' ? 'selected' : ''}">전체 <c:if test="${allCount > 0}">(${allCount})</c:if></a>
        	<a href="notificationList.do?type=favorite" class="${selectedType eq 'favorite' ? 'selected' : ''}">찜 알림 <c:if test="${favCount > 0}">(${favCount})</c:if></a>
        	<a href="notificationList.do?type=keyword" class="${selectedType eq 'keyword' ? 'selected' : ''}">키워드 알림 <c:if test="${keyCount > 0}">(${keyCount})</c:if></a>
    	</div>

		<!-- 기능 버튼 그룹 -->
		<c:if test="${not empty list}">
    		<div class="noti-utils">
        		<button type="button" id="mark-read-all-btn">✅ 전체 읽음</button>
        		<button type="button" id="delete-all-btn">🗑️ 전체 삭제</button>
    		</div>
		</c:if>
		
		<!-- 스크롤박스 시작 -->
		<div class="notification-scroll-box">
    	<!--  알림 리스트 출력 -->
    	<c:choose>
        	<c:when test="${not empty list}">
            	<c:forEach var="noti" items="${list}">
  					<!-- 알림별 링크 동적 분기 -->
  					<c:choose>
    					<c:when test="${noti.type eq 'review'}">
      						<c:set var="linkUrl" value="${pageContext.request.contextPath}/review/writeReviewForm.do?user_num=${noti.opponent_num}&product_num=${noti.product_num}" />
    					</c:when>
    					<c:otherwise>
      						<c:set var="linkUrl" value="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${noti.product_num}" />
    					</c:otherwise>
  					</c:choose>

  					<!-- 알림 박스 -->
    				<div class="noti-box ${noti.is_read == 0 ? 'unread' : 'read'}">
  						<a href="${linkUrl}" class="noti-link" data-noti-num="${noti.notifi_num}">
      						<span class="noti-label 
        						${noti.type == 'favorite' ? 'label-fav' : 
          							noti.type == 'keyword' ? 'label-key' : 
          							noti.type == 'review' ? 'label-review' : 'label-etc'}">
        						${noti.type == 'favorite' ? '찜' : 
          							noti.type == 'keyword' ? '키워드' : 
          							noti.type == 'review' ? '후기' : '기타'}
      						</span>
      						<p>${noti.message}</p>
      						<div class="noti-date">${noti.created_date}</div>
  						</a>
      					<!-- 삭제 버튼 -->
    					<button type="button" class="delete-noti-btn" data-noti-num="${noti.notifi_num}" title="삭제">x</button>
    				</div>
				</c:forEach>
				
        	</c:when>
        	<c:otherwise>
            	<p class="no-noti">알림이 없습니다.</p>
        	</c:otherwise>
    	</c:choose>
    	</div>
    	<!-- 스크롤박스 끝 -->
    	<button type="button" class="back-button" onclick="history.back()">이전으로</button>
	</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</div>
</body>
</html>