<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_notifi.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_header.css">
<script type="text/javascript">
$(function(){
	const contextPath = '${pageContext.request.contextPath}';
	
	// 드롭다운 자동 열기 체크
	if (localStorage.getItem('reopenDropdown') === 'true') {
		$('#alarm-dropdown').show();         // 드롭다운 보이기
		localStorage.removeItem('reopenDropdown'); // 플래그 제거
	}
	
	function showBadge(count) {
		  const badge = $('#alarm-count');
		  badge.text(count);

		  // 애니메이션 재실행을 위해 클래스 강제로 제거→추가
		  badge.removeClass('pop');
		  void badge[0].offsetWidth; // 강제로 리플로우
		  badge.addClass('pop');

		  badge.show();
		}

	// 아이콘 클릭 시 드롭다운 토글
	$('#alarm-icon').click(function(e){
		e.stopPropagation();
		$('#alarm-dropdown').toggle();
		
	});
	
	// 최근 알림 불러오기 (최대 5개 - 개수 변경 가능)
	$.ajax({
		url: '${pageContext.request.contextPath}/notification/getRecentNotificationList.do',
		type: "get",
		dataType: "json",
		success: function(res){
			if(res.list && res.list.length > 0){
				if (res.unreadCount > 0) {
			        showBadge(res.unreadCount); 
			      } else {
			        $('#alarm-count').hide();
			      }

	        	let html = '';
	        	res.list.forEach(noti => {
	        		const labelMap = {
	        			   	favorite: { class: 'label-fav', text: '찜' },
	        			    keyword: { class: 'label-key', text: '키워드' },
	        			    review: { class: 'label-review', text: '후기' },
	        			    default: { class: 'label-etc', text: '기타' }
	        		};
	        		
	        		const label = labelMap[noti.type] || labelMap.default;
	        		
	        		// 이동할 링크 정의
	        		let linkUrl = "#";
	        		if(noti.type === 'review'){
	        		    linkUrl = contextPath+'/review/writeReviewForm.do?user_num='+noti.opponent_num+'&product_num='+noti.product_num;
	        		} else {
	        		    linkUrl = contextPath + '/product/userProductDetail.do?product_num=' + noti.product_num;
	        		}
	        		
	        		const readClass = noti.is_read == 1 ? 'read' : 'unread';
	        		
	        		//클릭 시 읽음 처리하고 이동
	        		html += '<li class="recent-noti '+ readClass +'" data-noti-num="' + noti.notifi_num + '" data-link="' + linkUrl + '">' +
	                	'<span class="noti-label ' + label.class + '">' + label.text + '</span> ' + noti.message + '</li>';

	        	});
	        	$('#recent-alarms').html(html);
	      	} else {
	        	$('#alarm-count').hide();
	        	$('#recent-alarms').html('<li>알림이 없습니다.</li>');
	      	}
	    },
	    error: function(){
	      	alert('알림 불러오기 실패');
	   	 }
	  });

	
	// 외부 클릭 시 닫기
	$(document).click(function(){
		$('#alarm-dropdown').hide();
	});
	
	$(document).on('click', '.recent-noti', function(e){
		e.preventDefault();
		
		const notifiNum = $(this).data('noti-num');
		const link = $(this).data('link');

		const newTab = window.open('about:blank');

		$.ajax({
			url: contextPath + '/notification/updateReadNotification.do',
			type: 'post',
		    data: { notifi_num: notifiNum },
		    dataType: 'json',
		    success: function(res){
		    	if(res.result === 'success'){
		      		localStorage.setItem('reopenDropdown', 'true'); 
		        	newTab.location.href = link;
		        	location.reload();
		      	} else {
		        	alert('읽음 처리 실패');
		        	newTab.close();
		      	}
		    },
		    error: function(){
		      alert('네트워크 오류');
		      newTab.close();
		    }
		  });
	});

	
});

</script>    
<!-- header 시작 -->
<header class="header-area header-sticky">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <nav class="main-nav">
                    <!-- ***** Logo Start ***** -->
                    <a href="${pageContext.request.contextPath}/main/main.do" class="logo">
                        <img src="${pageContext.request.contextPath}/images/blackIcon.png" width="200" height="90" style="border-radius: 4px;">
                    </a>
                    <!-- ***** Logo End ***** -->
                    <!-- ***** Menu Start ***** -->
                    <ul class="nav">
                    	<!-- 
                        <li class="scroll-to-section"><a href="#top" class="active">Home</a></li>
                        <li class="scroll-to-section"><a href="#men">Men's</a></li>
                        <li class="scroll-to-section"><a href="#women">Women's</a></li>
                        <li class="scroll-to-section"><a href="#kids">Kid's</a></li>
                        -->
                        
						<c:if test="${!empty user_num && user_auth == 9}">
						<li><a href="${pageContext.request.contextPath}/user/adminPage.do">관리자 메인</a>
						</c:if>
						
                        <li class="scroll-to-section"><a href="${pageContext.request.contextPath}/product/userProductList.do">중고상품</a></li>
						
						<li><a href="${pageContext.request.contextPath}/board/boardList.do">커뮤니티</a></li>
                        
                        <c:if test="${!empty user_num}">
                        	<li><a href="${pageContext.request.contextPath}/user/myPage.do">마이페이지</a></li>
						</c:if>
                        
                        <!-- 
                        <li class="submenu">
                            <a href="javascript:;">Pages</a>
                            <ul>
                                <li><a href="${pageContext.request.contextPath}/info/about.do">About Us</a></li>
                                <li><a href="${pageContext.request.contextPath}/product/products.do">Products</a></li>
                                <li><a href="${pageContext.request.contextPath}/product/productDetail.do">Single Product</a></li>
                                <li><a href="${pageContext.request.contextPath}/info/contact.do">Contact Us</a></li>
                            </ul>
                        </li>
                        <li class="submenu">
                            <a href="javascript:;">Features</a>
                            <ul>
                                <li><a href="#">Features Page 1</a></li>
                                <li><a href="#">Features Page 2</a></li>
                                <li><a href="#">Features Page 3</a></li>
                                <li><a rel="nofollow" href="https://templatemo.com/page/4" target="_blank">Template Page 4</a></li>
                            </ul>
                        </li>
                        <li class="scroll-to-section"><a href="#explore">Explore</a></li>
                         -->

                        <c:if test="${!empty user_num && !empty user_photo}">
						<li class="menu-profile">
							<img src="${pageContext.request.contextPath}/upload/${user_photo}" width="25" height="25" class="my-photo"
								onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';">
						</li>
						</c:if>
						
						<c:if test="${!empty user_num && empty user_photo}">
							<li class="menu-profile">
								<img src="${pageContext.request.contextPath}/images/face.png" width="25" height="25" class="my-photo">
							</li>
						</c:if>
						
						<c:if test="${empty user_num}">
		                <li><a href="${pageContext.request.contextPath}/user/loginForm.do">로그인</a></li>
                        </c:if>
                        
						<c:if test="${!empty user_num}">
						<li class="menu-logout">
							[<span>${user_nickname}</span>]
							<a href="${pageContext.request.contextPath}/user/logout.do">로그아웃</a>
						</li>
						</c:if>
						<c:if test="${!empty user_num}">
						<li class="menu-alarm">
							<div class="alarm-wrapper">
    							<img src="${pageContext.request.contextPath}/images/bell.png" width="25" height="25" class="my-alarm" id="alarm-icon"
    							onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';">
    							<span class="alarm-badge" id="alarm-count" style="display:none;"></span>

    							<!-- 최근 알림 미니 박스 -->
    							<div class="alarm-dropdown" id="alarm-dropdown" style="display:none;">
      								<ul id="recent-alarms">
        								<!-- JS로 최근 알림 동적 추가 -->
      								</ul>
      								<div class="view-all">
        								<a href="${pageContext.request.contextPath}/notification/notificationList.do">전체 알림 보기</a>
      								</div>
    							</div>
  							</div>			
						</li>
						</c:if>
                    </ul>        
                    <a class='menu-trigger'>
                        <span>Menu</span>
                    </a>
                    <!-- ***** Menu End ***** -->
                </nav>
            </div>
        </div>
    </div>
</header>
<!-- header 끝 -->




