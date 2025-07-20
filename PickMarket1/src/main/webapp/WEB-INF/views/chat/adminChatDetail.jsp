<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>채팅관리</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/jw_chat_admin.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/jw_money_modal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script type="text/javascript">
$(function(){
	const sellerNum = ${productChatRoomVO.seller_num};
	const buyerNum = ${productChatRoomVO.buyer_num};
	const productNum = ${productChatRoomVO.product_num};
	const chatroom_num = ${productChatRoomVO.chatroom_num};
	
	/*
	$.ajax({
		url: '${pageContext.request.contextPath}/chat/adminChatMessageList.do',
		type: 'post',
		data: {chatroom_num: chatroom_num},
		dataType: 'json',
		success: function(param) {
	    	if(param.result == 'logout') {
	        	alert('로그인 후 사용하세요!');
	    	} else if(param.result == 'success') {
	    			const $chatting = $('#chatting_message').empty();
	                let chat_date = '';
	                $(param.list).each(function(index, item) {
	                    let output = '';
	                    // 날짜 구분 출력
	                    if(chat_date != item.chat_date.split(' ')[0]){
	                        chat_date = item.chat_date.split(' ')[0];
	                        output += '<div class="date-divider"><span>' + chat_date + '</span></div>';
	                    }
	                    const isSeller = item.user_num == sellerNum;
	                    const positionClass = isSeller ? 'to-position' : 'from-position';
	                    const bubbleAlign = isSeller ? '판매자' : '구매자';
	                   
						output += '<div class="chat-item" id="chat-message-'+item.chat_num+'">';
						output += '<div class="'+positionClass+'">';
	                    let messageTime = item.chat_date.split(' ')[1];
	                    
	                   if(isSeller){
	                    output += '<div class="chat-bubble">';
	                    if(item.deleted==1){
	                    	output += '<button class="chat-delete-btn" data-chat-num="'+item.chat_num+'">×</button>';
	                    	if(item.filename){
	                        	output += '<div class="bubble-photo">';
	                        	output += '<img src="${pageContext.request.contextPath}/upload/' + item.filename + '" alt="사진">';
	                        	output += '</div>';
	                    	}
	                        if (item.message) {
	                        	let amountMatch = item.message.match(/\[(?:pay|request):(\d+)\]/);
	                            let amount = amountMatch ? parseInt(amountMatch[1], 10) : '0';
	                            let formattedAmount = amount.toLocaleString();
	                            if (item.message.startsWith('[pay:')) { //송금하기
	                            	output += '<div class="bubble-content payment-message">'
	                            	output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
	                                    			+formattedAmount+'원 송금' + '</div>'
	                            }else if(item.message.startsWith('[request:')){ //송금요청메세지
	                            	output += '<div class="bubble-content payment-message">'
		                        	output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
		                                    		+formattedAmount+'원 송금 요청' + '</div>'
	                            }else {
	                                    output += '<div class="bubble-content">' + item.message + '</div>';
	                            }
	                        }
	                     }else if(item.deleted == 0 && item.message==null){
	                       		output += '<div class="bubble-content">' 
	                        	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 삭제된 메시지입니다';
	                       		output += '</div>';
	                     }else if(item.deleted == 0 && item.message == '[pay_cancel]'){
	                       	 	output += '<div class="bubble-content payment-message">'
	                       	 	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">송금이 취소되었습니다.</div>';
	                     }else if(item.deleted == 0 && item.message == '[request_cancel]'){
	                        	output += '<div class="bubble-content payment-message">'
		                       	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">송금요청이 취소되었습니다.</div>';
		                 }else if(item.deleted == 9){
		                	 	output += '<div class="bubble-content">' 
		                     	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 관리자에 의해 삭제된 메시지입니다';
		                       	output += '</div>';
		                 }else{
	                        	output += '메세지를 불러오는 중 오류 발생';
	                        }
	                        output += '</div>'; // chat-bubble
	                        output += '<div class="chat-time">' + messageTime + '</div>'; 
	                   }else{
	                	   output += '<div class="chat-time">' + messageTime + '</div>'; 
	                	   output += '<div class="chat-bubble">';
		                    if(item.deleted==1){
		                    	output += '<button class="chat-delete-btn" data-chat-num="'+item.chat_num+'">×</button>';
		                    	if(item.filename){
		                        	output += '<div class="bubble-photo">';
		                        	output += '<img src="${pageContext.request.contextPath}/upload/' + item.filename + '" alt="사진">';
		                        	output += '</div>';
		                    	}
		                        if (item.message) {
		                        	let amountMatch = item.message.match(/\[(?:pay|request):(\d+)\]/);
		                            let amount = amountMatch ? parseInt(amountMatch[1], 10) : '0';
		                            let formattedAmount = amount.toLocaleString();
		                            if (item.message.startsWith('[pay:')) { //송금하기
		                            	output += '<div class="bubble-content payment-message">'
		                            	output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
		                                    			+formattedAmount+'원 송금' + '</div>'
		                            }else if(item.message.startsWith('[request:')){ //송금요청메세지
		                            	output += '<div class="bubble-content payment-message">'
			                        	output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
			                                    		+formattedAmount+'원 송금 요청' + '</div>'
		                            }else {
		                                    output += '<div class="bubble-content">' + item.message + '</div>';
		                            }
		                        }
		                     }else if(item.deleted == 0 && item.message==null){
		                       		output += '<div class="bubble-content">' 
		                        	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 삭제된 메시지입니다';
		                       		output += '</div>';
		                     }else if(item.deleted == 0 && item.message == '[pay_cancel]'){
		                       	 	output += '<div class="bubble-content payment-message">'
		                       	 	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">송금이 취소되었습니다.</div>';
		                     }else if(item.deleted == 0 && item.message == '[request_cancel]'){
		                        	output += '<div class="bubble-content payment-message">'
			                       	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">송금요청이 취소되었습니다.</div>';
			                 }else if(item.deleted == 9){
			                	 	output += '<div class="bubble-content">' 
			                     	output += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 관리자에 의해 삭제된 메시지입니다';
			                       	output += '</div>';
			                 }else{
		                        	output += '메세지를 불러오는 중 오류 발생';
		                        }
		                        output += '</div>'; // chat-bubble
	                   }
	                        output += '</div>'; // from-position or to-position
	                    
	                    output += '</div>'; // chat-item
	                    $('#chatting_message').append(output);
	                });
	            	 // 약간의 렌더링 여유 시간 후 스크롤 이동 - 스크롤 중간에 생김 방지
	                setTimeout(function() {
	                    $('#chatting_message').scrollTop($('#chatting_message')[0].scrollHeight);
	                }, 50);
	            } else {
	                alert('채팅 메시지 호출 오류');
	            }
	        },
	        error: function() {
	            alert('네트워크 오류 발생');
	        }
	    });
	*/
	
	$.ajax({
	    url: '${pageContext.request.contextPath}/chat/adminChatMessageList.do',
	    type: 'post',
	    data: { chatroom_num: chatroom_num },
	    dataType: 'json',
	    success: function(param) {
	        if (param.result === 'logout') {
	            alert('로그인 후 사용하세요!');
	            return;
	        } else if (param.result !== 'success') {
	            alert('채팅 메시지 호출 오류');
	            return;
	        }

	        const $chatting = $('#chatting_message').empty();
	        let chat_date = '';
	        let fullOutput = '';

	        $(param.list).each(function(index, item) {
	            const isSeller = item.user_num == sellerNum;
	            const positionClass = isSeller ? 'to-position' : 'from-position';
	            const messageTime = item.chat_date.split(' ')[1];
	            const chatDateOnly = item.chat_date.split(' ')[0];
	            let output = '';

	            // 날짜 구분
	            if (chat_date !== chatDateOnly) {
	                chat_date = chatDateOnly;
	                output += '<div class="date-divider"><span>' + chat_date + '</span></div>';
	            }

	            // 채팅 박스
	            output += '<div class="chat-item" id="chat-message-' + item.chat_num + '">';
	            output += '<div class="' + positionClass + '">';

	            if (isSeller) {
	                output += '<div class="chat-bubble">' + renderMessageContent(item) + '</div>';
	                output += '<div class="chat-time">' + messageTime + '</div>';
	            } else {
	                output += '<div class="chat-time">' + messageTime + '</div>';
	                output += '<div class="chat-bubble">' + renderMessageContent(item) + '</div>';
	            }

	            output += '</div></div>'; // .positionClass, .chat-item
	            fullOutput += output;
	        });

	        $chatting.html(fullOutput);

	        // 스크롤 맨 아래로 이동
	        setTimeout(function() {
	            $chatting.scrollTop($chatting[0].scrollHeight);
	        }, 50);
	    },
	    error: function() {
	        alert('네트워크 오류 발생');
	    }
	});
	
	function renderMessageContent(item) {
	    let content = '';

	    if (item.deleted == 1) {
	        content += '<button class="chat-delete-btn" data-chat-num="' + item.chat_num + '">×</button>';

	        if (item.filename) {
	            content += '<div class="bubble-photo">';
	            content += '<img src="${pageContext.request.contextPath}/upload/' + item.filename + '" alt="사진">';
	            content += '</div>';
	        }

	        if (item.message) {
	            const amountMatch = item.message.match(/\[(?:pay|request):(\d+)\]/);
	            const amount = amountMatch ? parseInt(amountMatch[1], 10) : 0;
	            const formattedAmount = amount.toLocaleString();

	            if (item.message.startsWith('[pay:')) {
	                content += '<div class="bubble-content payment-message">';
	                content += '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">';
	                content += formattedAmount + '원 송금</div>';
	            } else if (item.message.startsWith('[request:')) {
	                content += '<div class="bubble-content payment-message">';
	                content += '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">';
	                content += formattedAmount + '원 송금 요청</div>';
	            } else {
	                content += '<div class="bubble-content">' + item.message + '</div>';
	            }
	        }

	    } else if (item.deleted == 0 && item.message == null) {
	        content += '<div class="bubble-content">';
	        content += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 삭제된 메시지입니다';
	        content += '</div>';
	    } else if (item.deleted == 0 && item.message === '[pay_cancel]') {
	        content += '<div class="bubble-content payment-message">';
	        content += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">송금이 취소되었습니다.</div>';
	    } else if (item.deleted == 0 && item.message === '[request_cancel]') {
	        content += '<div class="bubble-content payment-message">';
	        content += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">송금요청이 취소되었습니다.</div>';
	    } else if (item.deleted == 9) {
	        content += '<div class="bubble-content">';
	        content += '<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 관리자에 의해 삭제된 메시지입니다</div>';
	    } else {
	        content += '메세지를 불러오는 중 오류 발생';
	    }

	    return content;
	}


	
	// 메시지 삭제
	$(document).on('click', '.chat-delete-btn', function() {
	    const chat_num = $(this).data('chat-num');
	    
	    if (!confirm('정말 삭제하시겠습니까?')) {
	        return; // 취소 누르면 실행 중단
	    }
	    
	    $.ajax({
	      url: contextPath + '/chat/adminChatDelete.do',
	      type: 'post',
	      data: { chat_num: chat_num },
	      dataType: 'json',
	      success: function(result) {
	        if (result.result === 'success') {
	          const $msg = $('#chat-message-' + chat_num);
	          $msg.find('.bubble-photo, .bubble-content, .chat-delete-btn').remove();
	          $msg.find('.chat-bubble').append(`<div class="bubble-content">
	              <img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;">
	              관리자에 의해 삭제된 메시지입니다.
	            </div>`);
	        } else {
	          alert('삭제 실패');
	        }
	      },
	      error: function() {
	        alert('네트워크 오류');
	      }
	    });
	 });
	
	$(document).on('click', '#openDealModal', function() {
	    $('#dealModal').fadeIn();
	});
	function closeModal() {
	    $('#dealModal').fadeOut();
	}


});
</script>
<script type="module"
	src="${pageContext.request.contextPath}/js/promiseModal_admin.js"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<!-- 메인 시작 -->
	<div id="pw-main">
		<div class="admin-chat-title">
			<h2>채팅 관리</h2>
			<hr>
		</div>

		<input type="hidden" id="chatroom_num"
			value="${productChatRoomVO.chatroom_num}">
		<!-- 상품 정보 -->
		<div class="chat-product-info">
			<a
				href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${productChatRoomVO.product.product_num}"
				id="product-info"> <c:choose>
					<c:when test="${empty productChatRoomVO.product.thumbnail_img}">
						<img class="product-thumbnail"
							src="${pageContext.request.contextPath}/images/NoProductImg.png"
							alt="상품사진" />
					</c:when>
					<c:otherwise>
						<img class="product-thumbnail"
							src="${pageContext.request.contextPath}/upload/${productChatRoomVO.product.thumbnail_img}"
							onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/NoProductImg.png';"
							alt="상품사진" />
					</c:otherwise>
				</c:choose>
				<h2 class="product-title">${productChatRoomVO.product.title}</h2> <c:choose>
					<c:when test="${productChatRoomVO.product.state == 0}">
						<span class="product-status status-sale">판매중</span>
					</c:when>
					<c:when test="${productChatRoomVO.product.state == 1}">
						<span class="product-status status-reserved">예약중</span>
					</c:when>
					<c:when test="${productChatRoomVO.product.state == 2}">
						<span class="product-status status-sold">판매완료</span>
					</c:when>
				</c:choose>
			</a>
		</div>
		<%-- 
		<div class="chat-button-area">
			<!-- 약속잡기 버튼 -->
			<button class="chat-action-btn" id="openDealModal">약속 열람</button>
		</div>
		--%>
		<!-- 판매자 구매자 닉네임 출력 -->
		<div class="chat-user-labels">
			<div class="seller-label">판매자:
				${productChatRoomVO.seller.nickname}</div>
			<div class="buyer-label">구매자:
				${productChatRoomVO.buyer.nickname}</div>
		</div>

		<!-- 약속 설정 값 출력 공간 -->
		<div id="dealNotice" class="deal-notice" style="display: none;">
			<span id="dealText"></span>
		</div>

		<!-- 채팅 메시지 박스 -->
		<div id="chatting_message">
			<!-- 메시지 들어올 자리 -->
		</div>

		<!-- 약속잡기 -->
		<div id="dealModal" class="modal-overlay" style="display: none;">
			<div class="modal-content">
				<button type="button" class="modal-close-btn" onclick="closeModal()">×</button>
				<h3>약속 시간 및 장소</h3>
				<form id="dealForm">
					<label>약속 시간</label><br> <input type="datetime-local"
						name="deal_datetime" required><br>
					<br> <label for="loc">거래희망장소</label> <input type="text"
						name="loc" id="loc" maxlength="10" class="input-check" readonly>
					<c:set var="xLoc"
						value="${productChatRoomVO.deal_x_loc > 0 ? productChatRoomVO.deal_x_loc : product.x_loc}" />
					<c:set var="yLoc"
						value="${productChatRoomVO.deal_y_loc > 0 ? productChatRoomVO.deal_y_loc : product.y_loc}" />
					<input type="hidden" name="deal_x_loc" id="deal_x_loc"
						value="${xLoc}" /> <input type="hidden" name="deal_y_loc"
						id="deal_y_loc" value="${yLoc}" />

					<div id="map" style="width: 100%; height: 300px;"></div>


					<input type="hidden" name="chatroom_num"
						value="${productChatRoomVO.chatroom_num}">

					<div class="modal-button-group">
						<button type="button" id="deleteDealBtn">삭제하기</button>
					</div>
				</form>
			</div>
		</div>
		<div class="chat-button-area">
			<button type="button" class="admin-back-button"
				onclick="location.href='${pageContext.request.contextPath}/chat/adminChatRoomList.do'">목록으로</button>
		</div>
	</div>
	<!-- 메인 끝 -->
	<!-- 주소 API 시작 -->
	<script type="text/javascript">
    var kakaoApiKey = "${kakaoApiKey}";
    var script = document.createElement('script');
    script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=" + kakaoApiKey + "&autoload=false&libraries=services";
    script.onload = function () {
        kakao.maps.load(function () {
            var lat = ${!empty productChatRoomVO.deal_x_loc || productChatRoomVO.deal_x_loc > 0 ? productChatRoomVO.deal_x_loc : product.x_loc};
            var lng = ${!empty productChatRoomVO.deal_y_loc || productChatRoomVO.deal_y_loc > 0 ? productChatRoomVO.deal_y_loc : product.y_loc};
            
            if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
                initMapWithSavedCoordinates(lat, lng);
            } else {
            	// 서울 시청 출력
    			const fallbackLat = parseFloat(document.getElementById("deal_x_loc").value) || 37.5665;
    			const fallbackLng = parseFloat(document.getElementById("deal_y_loc").value) || 126.9780;
    			initMapWithSavedCoordinates(fallbackLat, fallbackLng);
            }
        });
    };
    document.head.appendChild(script);
</script>
	<script type="module"
		src="${pageContext.request.contextPath}/js/promiseModal_updated.js"></script>

	<script>
	var marker;
	var map;
	
	function initMapWithSavedCoordinates(lat, lng) {
	    var geocoder = new kakao.maps.services.Geocoder();

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

        geocoder.coord2Address(lng, lat, function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                var address = result[0].address.address_name;
                document.getElementById("loc").value = address;
            } else {
            	console.error("주소 변환 실패", status);
            }
        });

        kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
            var latLng = mouseEvent.latLng;
            var newLat = latLng.getLat();
            var newLng = latLng.getLng();

            document.getElementById("deal_x_loc").value = newLat;
            document.getElementById("deal_y_loc").value = newLng;

	        if (marker) {
	            marker.setMap(null);
	        }

	        marker = new kakao.maps.Marker({
	            position: latLng
	        });
	        marker.setMap(map);

	        geocoder.coord2Address(newLng, newLat, function(result, status) {
	            if (status === kakao.maps.services.Status.OK) {
	                var address = result[0].address.address_name;
	                document.getElementById("loc").value = address;
	            }
	        });
	    });
	}
</script>
	<!-- 주소 API 끝 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
