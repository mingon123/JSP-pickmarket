<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 상세</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/product.reply.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/product.fav.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_alarm.css" >
<script type="text/javascript">
	window.onload=function(){
		//삭제 이벤트 연결
		 const delete_btn = document.getElementById('delete_btn');
		 if (delete_btn) {
		     delete_btn.onclick = function(){
		         let choice = confirm('삭제하시겠습니까?');
		         if (choice) {
		             location.replace('productDelete.do?product_num=${product.product_num}');
		         }//if
		     };//onclick
		 } //if	
	};
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<%-- 페이지 이동할 때마다 카테고리 활성화 변경 여부 확인해주어야 함. 
					-> mvcPage item/user_detail.jsp 참고함. 조건 외 다른항목은 board/detail.jsp 참고. --%>
		<div class="product-container">
		<c:if test="${product.category_status == 0}">
			<div class="result-display">
				<div class="align-center">
					본 상품은 판매 중지되었습니다.			
					<p>
					<input type="button" value="상품목록 보기" onclick="location.href='userProductList.do'">
				</div>
			</div>
			</c:if>
		<c:if test="${product.category_status == 1}">
			<div class="product-image" style="position: relative;">
			<%-- 사진 없으면 기본이미지 --%>
			<c:if test="${empty productImgList}">
				<div class="image-wrapper position-relative">		        
			        <img src="${pageContext.request.contextPath}/images/NoProductImg.png" class="d-block">
			        <%-- 찜 --%>
			        <div class="heart-icon">
			            <img id="output_fav" data-num="${product.product_num}" 
			                 src="${pageContext.request.contextPath}/images/unheart.png" width="40">
			            <div class="fav-text">
			            찜하기 <span id="output_fcount"></span>
			            </div>
			        </div>
			    </div>
			</c:if>
			<%-- 사진 1장일때 --%>
			<c:if test="${!empty productImgList && productImgList.size()==1}">
				<div class="image-wrapper position-relative">
					<img src="${pageContext.request.contextPath}/upload/${productImgList[0].filename}" class="d-block" 
						onerror="this.onerror=null; 
						this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
						this.nextElementSibling.style.display='flex';" 
						alt="상품 이미지">
					<div class="error-text">이미지 로드 실패</div>
					<%-- 찜 --%>
			        <div class="heart-icon">
			            <img id="output_fav" data-num="${product.product_num}" 
			                 src="${pageContext.request.contextPath}/images/unheart.png" width="40">
			            <div class="fav-text">
			            찜하기 <span id="output_fcount"></span>
			            </div>
			        </div>
				</div>
			</c:if>
			<%-- 사진 여러장일때 --%>			
			<c:if test="${!empty productImgList && productImgList.size()>1}">			
			  <div class="carousel-wrapper position-relative">
			  <%-- carousel 시작 --%>
			  <div id="imgCarousel" class="carousel slide" data-ride="carousel">
			    <%-- Indicators --%>
			    <ol class="carousel-indicators">
			      <c:forEach var="img" items="${productImgList}" varStatus="status">
			        <li data-target="#imgCarousel" data-slide-to="${status.index}" class="${status.index == 0 ? 'active' : ''}"></li>
			      </c:forEach>
			    </ol>
			    <%-- Slides --%>
			    <div class="carousel-inner">
			      <c:forEach var="img" items="${productImgList}" varStatus="status">
			        <div class="carousel-item ${status.index == 0 ? 'active' : ''}">
			        	<div class="image-wrapper position-relative">
			        	<img src="${pageContext.request.contextPath}/upload/${img.filename}" class="d-block" 
			         	 onerror="this.onerror=null; 
			         	 this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
			         	 this.nextElementSibling.style.display='flex';" 
			         	 alt="상품 이미지">
			         	<div class="error-text">이미지 로드 실패</div>
			         	</div>
			        </div>
			      </c:forEach>
			    </div>
			    <%-- Controls --%>
			    <a class="carousel-control-prev" href="#imgCarousel" role="button" data-slide="prev">
			      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
			      <span class="sr-only">이전</span>
			    </a>
			    <a class="carousel-control-next" href="#imgCarousel" role="button" data-slide="next">
			      <span class="carousel-control-next-icon" aria-hidden="true"></span>
			      <span class="sr-only">다음</span>
			    </a>
			  </div>
			  <%-- carousel 끝 --%>
			  <%-- 찜 --%>
			  <div class="heart-icon-fixed">
			      <img id="output_fav" data-num="${product.product_num}" 
			           src="${pageContext.request.contextPath}/images/unheart.png" width="40">
			      <div class="fav-text">
			      찜하기 <span id="output_fcount"></span>
			      </div>
			  </div>
			  </div>
			</c:if>
			<div class="seller-info" 
			onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${product.user_num}'" style="cursor:pointer;">
	           	<img class="seller-photo" src="
	  			<c:choose>
	            		<c:when test='${empty seller.photo}'>
	    				${pageContext.request.contextPath}/images/face.png
	            		</c:when>
	            		<c:otherwise>
	            			${pageContext.request.contextPath}/upload/${seller.photo}
	            		</c:otherwise>
	           	</c:choose>"  
	           		onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';" 
	           		 alt="프로필사진">
	           	<strong>${product.nickname}</strong> 
	           	
	           	<!-- 알림 버튼 -->
	           	<c:if test="${product.user_num != user_num}">
    				<div id="alert_button_box" style="position: absolute; right: 0; bottom: 0;">
        				<button id="alert_button" class="alert-btn" data-flag="">🔔 알림 끄기</button>
    				</div>
    			</c:if>
	           	
	        </div> <%-- class="seller-info" 끝 --%>
			</div> <%-- class="product-image" 끝 --%>
			
			<div class="product-detail">
				<form>
					<input type="hidden" name="product_num" value="${product.product_num}" id="product_num">
					<ul>
						<li>
						<h3>
						<span class="product_status">
		                <c:choose>
		                    <c:when test="${product.state == 0}">
		                        <span class="product-status-badge status-sale">판매중</span>
		                    </c:when>
		                    <c:when test="${product.state == 1}">
		                        <span class="product-status-badge status-reserved">예약중</span>
		                    </c:when>
		                    <c:when test="${product.state == 2}">
		                        <span class="product-status-badge status-soldout">판매완료</span>
		                    </c:when>
		                </c:choose>	
		                </span>
		                ${product.title}</h3>
						</li>
						<li><small>${product.category_name} · 
							<c:if test="${product.up_date != null}">끌올 ${product.up_date}</c:if>
							<c:if test="${product.up_date == null}">${product.reg_date}</c:if> 
						</small><li>											
						<li>가격 : <b><fmt:formatNumber value="${product.price}"/>원</b></li>
						<li><p>${product.content}</p></li>						
						<li>
						    <label for="loc">거래희망장소</label>
						    <input type="text" name="loc" id="loc" maxlength="20" readonly>
						    <input type="hidden" name="x_loc" id="x_loc">
		    				<input type="hidden" name="y_loc" id="y_loc">
						    <div id="map" style="width: 95%; height: 300px;"></div>
						</li>									
						<li class="btn-box align-right">
							<c:if test="${product.user_num != user_num}">
							<input type="button" value="판매자와 채팅하기" onclick="location.href='${pageContext.request.contextPath}/chat/chatDetail.do?product_num=${product.product_num}'"> 
							</c:if>		
							<c:if test="${product.user_num == user_num}">
							<input type="button" value="수정" onclick="location.href='${pageContext.request.contextPath}/product/productModifyForm.do?product_num=${product.product_num}'"> 
							<input type="button" value="삭제" id="delete_btn"> 
							<input type="button" value="채팅 목록" onclick="location.href='${pageContext.request.contextPath}/chat/productChatRoomList.do?product_num=${product.product_num}'">
							</c:if>		 
						</li>	
					</ul>
				</form>
			</div>
			
		</c:if>
		</div> <%-- class="product-container" 끝 --%>
		<hr size="1" noshade="noshade" width="100%">
			<!-- 댓글 시작 -->
			<div id="reply_div">
				<span class="re-title">댓글 달기</span>
				<form id="re_form">
					<input type="hidden" name="product_num" value="${product.product_num}" id="product_num">
					<textarea rows="3" cols="50" name="reply_content" id="reply_content" class="rep-content"
					<c:if test="${empty user_num}">disabled="disabled"</c:if>
					><c:if test="${empty user_num}">로그인해야 작성할 수 있습니다.</c:if></textarea>
					<c:if test="${!empty user_num}">
					<div id="re_first">
						<span class="letter-count">300/300</span>
					</div>
					<div id="re_second" class="align-right">
						<input type="submit" value="전송">
					</div>
					</c:if>			
				</form>
			</div>
			<!-- 댓글 목록 출력 시작 -->
			<div id="output"></div>
			<div class="paging-button" style="display:none;">
				<input type="button" value="다음글 보기">
			</div>
			<div id="loading" style="display:none;">
				<img src="${pageContext.request.contextPath}/images/loading.gif" width="50" height="50">
			</div>
			<!-- 댓글 목록 출력 끝 -->
			<!-- 댓글 끝 -->
	</div> <%-- class="content-main" 끝 --%>
<!-- 주소 API 시작 -->
<script type="text/javascript">
    var kakaoApiKey = "${kakaoApiKey}";
    var script = document.createElement('script');
    script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=" + kakaoApiKey + "&autoload=false&libraries=services";
    script.onload = function () {
        kakao.maps.load(function () {
            var lat = parseFloat("${product.x_loc}");
            var lng = parseFloat("${product.y_loc}");
        	displayLocationByCoordinates(lat, lng);
        	getAddressFromCoordinates(lat, lng);
        });
    };
    document.head.appendChild(script);
</script>

<script>
	var marker;
	var map;
	
	// 지도와 마커 표시
    function displayLocationByCoordinates(lat, lng) {
        var container = document.getElementById('map');
        var options = {
            center: new kakao.maps.LatLng(lat, lng),
            level: 3
        };

        map = new kakao.maps.Map(container, options);

        marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat, lng)
        });

        marker.setMap(map);
    }
	
    // 위도/경도를 주소로 변환하고 input#loc에 표시
    function getAddressFromCoordinates(lat, lng) {
        var geocoder = new kakao.maps.services.Geocoder();
        var coord = new kakao.maps.LatLng(lat, lng);

        geocoder.coord2Address(coord.getLng(), coord.getLat(), function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                var address = result[0].address.address_name;
                document.getElementById("loc").value = address;
            } else {
                document.getElementById("loc").value = "주소를 불러올 수 없습니다.";
            }
        });
    }
</script>
<!-- 주소 API 끝 -->	
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>