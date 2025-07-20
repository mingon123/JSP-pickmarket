<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>PickMarket</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_chat.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_money_modal.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
    const contextPath = '${pageContext.request.contextPath}';
    window.message_socket = new WebSocket("ws://localhost:8080/PickMarket/webSocket");
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/preview.js"></script>
<script type="text/javascript">
$(function(){
	const user_num = ${user_num};
	const isBuyer = ${productChatRoomVO.buyer_num} == user_num;
	const sellerNum = ${productChatRoomVO.seller_num};
	const buyerNum = ${productChatRoomVO.buyer_num};
	const productNum = ${productChatRoomVO.product_num};
	let payInput = '';
	//웹소켓 시작
	(async function () {
	    await waitForSocketConnection(window.message_socket);
	    // board: 신호 먼저 보냄
	    window.message_socket.send("board:");
	    selectData(); // 안정적으로 실행되도록 여기서 호출
		
	    //분기처리
	    message_socket.onmessage = function(evt){
			let data = evt.data;
			//메시지 알림
			if(data.substring(0,6) == 'board:'){
				selectData();
			}else if(data.startsWith('delete:')) {
				const [_, chat_num, room_num] = data.split(':');
				if ($('#chatroom_num').val() === room_num) {
					const $msg = $('#chat-message-' + chat_num);
					if ($msg.length > 0) {
						// 기존 사진 및 텍스트 영역 제거
						$msg.find('.bubble-photo').remove();
						$msg.find('.bubble-content').remove();
						$msg.find('.chat-delete-btn').remove();
						
						const originalText = $msg.attr('data-original-text');

						// 분기 처리
						if (originalText === '[pay_cancel]') {
							$msg.find('.chat-bubble').append(`
								<div class="bubble-content">
									<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">
									송금이 취소되었습니다.
								</div>
							`);
						}else if(originalText === '[request_cancel]'){
							$msg.find('.chat-bubble').append(`
									<div class="bubble-content">
										<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">
										송금요청이 취소되었습니다.
									</div>
								`);
						}else {
							$msg.find('.chat-bubble').append(`
								<div class="bubble-content">
									<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;">
									삭제된 메시지입니다
								</div>
							`);
						}
					}
				}
			}else if(data == 'reload'){
				location.reload();
				//setTimeout(() => location.reload(), 500);
				return;
			}
		}
	})();

	// WebSocket 연결 대기 함수
	function waitForSocketConnection(socket) {
	    return new Promise((resolve, reject) => {
	        const maxAttempts = 10;
	        let attempt = 0;
	        const interval = setInterval(() => {
	            if (socket.readyState === 1) {
	                clearInterval(interval);
	                resolve();
	            } else if (attempt >= maxAttempts) {
	                clearInterval(interval);
	                reject(new Error("WebSocket 연결 실패"));
	            }
	            attempt++;
	        }, 100);
	    });
	}

	/*
	//웹소켓 시작
	//window.message_socket = new WebSocket("ws://localhost:8080/PickMarket/webSocket");
	const message_socket = window.message_socket;
	message_socket.onopen = function(evt){
		message_socket.send("board:");
	}
	//서버로부터 메시지를 받으면 호출되는 함수 지정
	message_socket.onmessage = function(evt){
		let data = evt.data;
		//메시지 알림
		if(data.substring(0,6) == 'board:'){
			selectData();
		}else if(data.startsWith('delete:')) {
			const [_, chat_num, room_num] = data.split(':');
			if ($('#chatroom_num').val() === room_num) {
				const $msg = $('#chat-message-' + chat_num);
				if ($msg.length > 0) {
					// 기존 사진 및 텍스트 영역 제거
					$msg.find('.bubble-photo').remove();
					$msg.find('.bubble-content').remove();
					$msg.find('.chat-delete-btn').remove();
					
					const originalText = $msg.attr('data-original-text');

					// 분기 처리
					if (originalText === '[pay_cancel]') {
						$msg.find('.chat-bubble').append(`
							<div class="bubble-content">
								<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">
								송금이 취소되었습니다.
							</div>
						`);
					}else if(originalText === '[request_cancel]'){
						$msg.find('.chat-bubble').append(`
								<div class="bubble-content">
									<img src="${pageContext.request.contextPath}/images/delete.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">
									송금요청이 취소되었습니다.
								</div>
							`);
					}else {
						$msg.find('.chat-bubble').append(`
							<div class="bubble-content">
								<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;">
								삭제된 메시지입니다
							</div>
						`);
					}
				}
			}
		}else if(data == 'reload'){
			location.reload();
			//setTimeout(() => location.reload(), 500);
			return;
		}
	}
	*/
	//웹소켓 끝
	
	
	//채팅 삭제
	$(document).on('click', '.chat-delete-btn', function() {
		const chat_num = $(this).attr('data-chat-num');
		$.ajax({
			url: '${pageContext.request.contextPath}/chat/chatDelete.do',
			type: 'post',
			data: { chat_num: chat_num },
			dataType: 'json',
			success: function(result) {
				if(result.result === 'success') {
					const $msg = $('#chat-message-' + chat_num);
					
					// 기존 사진 및 텍스트 영역 제거
					$msg.find('.bubble-photo').remove();
					$msg.find('.bubble-content').remove();
					
					//$msg.find('.bubble-content').html('<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;"> 삭제된 메시지입니다');
					$msg.find('.chat-bubble').append(
							`<div class="bubble-content">
								<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:16px; height:16px; margin-right:4px;">
     							삭제된 메시지입니다
   							  </div>`
					);
					
					$msg.find('.chat-delete-btn').remove();
					message_socket.send('delete:' + chat_num+ ':' + $('#chatroom_num').val());
				} else if(result.result === 'timeout') {
					alert('5분이 지나 삭제할 수 없습니다.');
				} else {
					alert('삭제 실패');
				}
			}
		});
	});
	
	// 송금/요청 취소 통합 핸들러
	$(document).on('click', '.cancel-payment-btn', function () {
	    const chat_num = $(this).data('chat-num');
	    const type = $(this).data('type'); // 'pay' or 'request'
	    
	    const confirmMsg = type === 'request' ? '송금 요청을 취소하시겠습니까?' : '송금을 취소하시겠습니까?';
	    const cancelText = type === 'request' ? '송금 요청이 취소되었습니다.' : '송금이 취소되었습니다.';
	    const cancelCode = type === 'request' ? '[request_cancel]' : '[pay_cancel]';

	    if (confirm(confirmMsg)) {
	        $.ajax({
	            url: '${pageContext.request.contextPath}/chat/chatDelete.do',
	            type: 'post',
	            data: { chat_num: chat_num },
	            dataType: 'json',
	            success: function (result) {
	                if (result.result === 'success') {
	                    const $msg = $('#chat-message-' + chat_num);
	                    $msg.attr('data-original-text', cancelCode);
	                    $msg.find('.bubble-photo').remove();
	                    $msg.find('.bubble-content').remove();

	                    $msg.find('.chat-bubble').append(`
	                        <div class="bubble-content">
	                            <img src="${pageContext.request.contextPath}/images/payment_cancel.png" alt="취소" style="width:16px; height:16px; margin-right:4px;">
	                            ${cancelText}
	                        </div>
	                    `);
	                    $msg.find('.cancel-payment-btn').remove();

	                    message_socket.send('delete:' + chat_num + ':' + $('#chatroom_num').val());
	                    
	                  //판매글 상태를 거래가능으로 변경
	            		$.ajax({
	            		    url: contextPath + '/product/updateToSaleState.do',
	            		    type: 'post',
	            		    data: { product_num: productNum}, 
	            		    dataType: 'json',
	            		    success: function(param) {
	            		    	if (param.result == 'logout') {
	            	                alert('로그인해야 사용할 수 있습니다');
	            	                message_socket.close();
	            	            } else if (param.result == 'success') {
	            	               	alert('판매글의 상태가 업데이트 되었습니다.');
	            	               	message_socket.send('reload');
	            	            } else {
	            	                alert('판매글 상태 업데이트 실패');
	            	            }
	            	        },
	            	        error: function () {
	            	            alert('네트워크 오류 발생');
	            	        }
	            		});
	                } else if (result.result === 'timeout') {
	                    alert('5분이 지나 취소할 수 없습니다.');
	                } else {
	                    alert('취소 실패');
	                }
	            },
	            error: function () {
	                alert('네트워크 오류 발생');
	            }
	        });
	    }
	});
	
	//채팅 메시지 전송을 위한 enter key처리
	$('#message').keydown(function(event){
		if(event.keyCode == 13 && !event.shiftKey){
			//이벤트 발생
			$('#chatting_form').trigger('submit');
		}
		
	});
	
	function selectData() {
	    $.ajax({
	        url: '${pageContext.request.contextPath}/chat/chatMessageList.do',
	        type: 'post',
	        data: {chatroom_num: $('#chatroom_num').val()},
	        dataType: 'json',
	        success: function(param) {
	            if(param.result == 'logout') {
	                alert('로그인 후 사용하세요!');
	                message_socket.close();
	            } else if(param.result == 'success') {
	                $('#chatting_message').empty();
	                let chat_date = '';
	                $(param.list).each(function(index, item) {
	                    let output = '';
	                    // 날짜 구분 출력
	                    if(chat_date != item.chat_date.split(' ')[0]){
	                        chat_date = item.chat_date.split(' ')[0];
	                        output += '<div class="date-divider"><span>' + chat_date + '</span></div>';
	                    }
	                    //여기부터
	                    let writeTime = new Date(item.chat_date);
						let now = new Date();
						let diffMinutes = (now - writeTime) / (1000 * 60);
						let isMine = item.user_num == ${user_num};
						let isDeletable = isMine && item.deleted == 1 && diffMinutes <= 5;
						output += '<div class="chat-item" id="chat-message-'+item.chat_num+'">';
						
	                    let messageTime = item.chat_date.split(' ')[1];
	                    if(item.user_num == ${user_num}) { //내메세지
	                        output += '<div class="from-position">';
	                        // 읽음 표시
	                        if(item.read_check != 0){
	                            output += '<div class="read-check">1</div>'; // 안읽음 (읽지 않은 메시지)
	                        } else {
	                            output += '<div class="read-check"></div>'; // 읽음 (공백)
	                        }
	                        // 시간 표시
	                        output += '<div class="chat-time">' + item.chat_date.split(' ')[1] + '</div>';
	                        // 채팅 버블
	                        //output += '<div class="chat-bubble">';
	                        let isPaymentMessage = item.message && (item.message.startsWith('[pay:') || item.message.startsWith('[request:')||
	                        	    item.message === '[pay_cancel]' || item.message === '[request_cancel]');
	                        output += '<div class="chat-bubble' + (isPaymentMessage ? ' payment-message' : '') + '">';
	                        if(item.deleted==1){
	                        	if(isDeletable && !item.message.startsWith('[pay:') && !item.message.startsWith('[request:')){
	                        		 output += '<button class="chat-delete-btn" data-chat-num="'+item.chat_num+'">×</button>';
	                        	}
	                        	if(item.filename){
	                            	output += '<div class="bubble-photo">';
	                            	output += '<img src="${pageContext.request.contextPath}/upload/' + item.filename + '" alt="사진">';
	                            	output += '</div>';
	                        	}
	                        	if(item.message){
	                        		let amountMatch = item.message.match(/\[(?:pay|request):(\d+)\]/);
	                        		let amount = amountMatch ? parseInt(amountMatch[1],10) : '0';
	                        		let formattedAmount = amount.toLocaleString();
	                        		if (item.message.startsWith('[pay:')) { //송금하기 메세지
	     
	                        	       	if(isBuyer){ //내가 구매자인 경우 송금하기로 보낼 수 있음 - 판매자일 경우 보낼일x
	                        	        	output += '<div class="bubble-content payment-message">'
	                        	           	output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
	                        	            	+formattedAmount + '원 송금했습니다.'
	                        	            if(item.processed !== 1){
	                        	            	output += '<button class="cancel-payment-btn" data-chat-num='+item.chat_num+' data-type="pay">송금 취소</button></div>';
			                                }else{
			                                    output += '<button class="cancel-payment-btn" disabled class="disabled" style="cursor: not-allowed; opacity: 0.6;">취소 불가</button></div>';
			                                }
	                        	        }
	                        	    }else if(item.message.startsWith('[request:')){ //송금요청메세지
	                        	    	if(!isBuyer){ //판매자일 경우 송금요청 메세지를 보냄- 구매자일 경우 보낼일 x
	                        	    		output += '<div class="bubble-content payment-message">'
		                        	        output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
		                        	            	+formattedAmount+'원 송금 요청했습니다.'
		                        	        if(item.processed !== 1){
		                        	        	output += '<button class="cancel-payment-btn" data-chat-num='+item.chat_num+' data-type="request">요청 취소</button></div>';
					                        }else{
					                        	output += '<button class="cancel-payment-btn" disabled class="disabled" style="cursor: not-allowed; opacity: 0.6;">요청 완료</button></div>';
					                        }
		                        	        //output += '<button class="receive-payment-btn" data-chat-num='+item.chat_num+'data-amount='+amount+'>송금 받기</button></div>';
	                        	    	}
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
	                        output += '</div>'; // from-position
	                    }else { // 상대 메시지
	                        output += '<div class="to-position">';
	                        //output += '<div class="chat-bubble">';
	                        let isPaymentMessage = item.message && (item.message.startsWith('[pay:') || item.message.startsWith('[request:')||
	                        	    item.message === '[pay_cancel]' || item.message === '[request_cancel]');
	                        output += '<div class="chat-bubble' + (isPaymentMessage ? ' payment-message' : '') + '">';
	                       	if(item.deleted==1){
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
										
	                                    if(!isBuyer){ //상대메세지가 송금하기이려면 내가 판매자
	                                    	output += '<div class="bubble-content payment-message">'
	                                    	output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
	                                    			+formattedAmount+'원 송금했습니다.'
	                                    	if(item.processed !== 1){
	                                    		output+=  '<button class="receive-payment-btn" data-chat-num=' +item.chat_num+' data-amount='+amount+'>송금 받기</button></div>';
	                                    	}else{
	                                    		output += '<button class="receive-payment-btn" disabled class="disabled" style="cursor: not-allowed; opacity: 0.6;">받기 완료</button></div>';
	                                    	}
	                                    }
	                                }else if(item.message.startsWith('[request:')){ //송금요청메세지
	                                	if(isBuyer){ //상대메세지가 송금요청 - 내가 구매자
	                                		output += '<div class="bubble-content payment-message">'
		                                    output +=  '<img src="${pageContext.request.contextPath}/images/payment.png" alt="송금" style="width:16px; height:16px; margin-right:4px;">'
		                                    		+formattedAmount+'원 송금 요청 받았습니다.'
		                                    if(item.processed !== 1){
		                                    	output+=  '<button class="send-payment-btn" data-chat-num=' +item.chat_num+' data-amount='+amount+'>송금 하기</button></div>';
		                                    }else{
		                                    	output += '<button class="send-payment-btn" disabled class="disabled" style="cursor: not-allowed; opacity: 0.6;">송금 완료</button></div>';
		                                    }
	                                	}
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
	                        output += '<div class="chat-time">' + messageTime + '</div>'; // [시간] 나중에
	                        output += '</div>'; // to-position
	                    }
	                    output += '</div>'; // chat-item
	                    $('#chatting_message').append(output);
	                });
	            	 // 약간의 렌더링 여유 시간 후 스크롤 이동 - 스크롤 중간에 생김 방지
	                setTimeout(function() {
	                    $('#chatting_message').scrollTop($('#chatting_message')[0].scrollHeight);
	                }, 50);
	                //$('#chatting_message').scrollTop($('#chatting_message')[0].scrollHeight);
	            } else {
	                alert('채팅 메시지 호출 오류');
	            }
	        },
	        error: function() {
	            alert('네트워크 오류 발생');
	        }
	    });
	}
	
	//채팅 등록
	$('#chatting_form').submit(function(event){
		if($('#message').val().trim() == '' && $('#upload-photo').val() == ''){
			alert('내용이나 사진 중 하나는 입력하세요');
			return false;
		}
		let formData = new FormData(this);

		$.ajax({
		    url: '${pageContext.request.contextPath}/chat/chatWrite.do',
		    type: 'post',
		    data: formData,
		    dataType: 'json',
		    processData: false,
		    contentType: false,
		    success: function(param){
		        if(param.result == 'logout'){
		            alert('로그인해야 사용할 수 있습니다');
		            message_socket.close();
		        }else if(param.result == 'success'){
		            $('#message').val('').focus();
		            $('#upload-photo').val('');
		            $('#photo-preview').css('display', 'none');
	                $('#preview-image').attr('src', '');
		            message_socket.send('board:');
		        }else{
		            alert('메세지 등록 오류 발생');
		        }
		    },
		    error: function(){
		        alert('네트워크 오류 발생');
		    }
		});
		
		//기본이벤트제거
		event.preventDefault();
	})
	
	

window.openPaymentModal = function () {
	payInput = '';
	updatePayDisplay();
	$('#paymentModal').fadeIn(200);
}

window.closePaymentModal = function (){
	$('#paymentModal').fadeOut(200);
}

// 금액 표시 업데이트
function updatePayDisplay() {
	const display = document.getElementById('pay-amount');
	const formatted = payInput === '' ? '0원' : Number(payInput).toLocaleString() + '원';
	display.textContent = formatted;
}

// 키패드 입력 처리
$(document).on('click', '.key', function () {
	const value = $(this).text();
	if (value === '←') {
		payInput = payInput.slice(0, -1);
	} else {
		payInput += value;
		// 앞에 0 여러 개 방지
		payInput = payInput.replace(/^0+/, '');
  	}
  	updatePayDisplay();
});

// +금액 버튼 처리
$(document).on('click', '.amount-add-btn', function () {
  	const addValue = parseInt($(this).data('amount'), 10);
  	const current = parseInt(payInput || '0', 10);
  	payInput = (current + addValue).toString();
  	updatePayDisplay();
});

// 확인 버튼 클릭
$('#paySubmitBtn').on('click', function () {
  	const amount = parseInt(payInput || '0', 10);
  	if (amount <= 0) {
    	alert('송금할 금액을 입력해주세요.');
    	return;
  	}

  	// 채팅 메시지 형태로 송금 메시지 보내기
  	const chatroomNum = $('#chatroom_num').val();
  	const formData = new FormData();
  	formData.append('chatroom_num', chatroomNum);
  	const message = isBuyer? '[pay:'+amount+'] ${amount.toLocaleString()}원 송금했습니다.'
    			: '[request:'+amount+'] ${amount.toLocaleString()}원 송금요청했습니다.';
	formData.append('message', message);
	console.log('최종 message:', message);
	formData.append('filename', '');
	// 실제 전송
 	$.ajax({
    	url: contextPath + '/chat/chatWrite.do',
    	type: 'post',
    	data: formData,
    	processData: false,
    	contentType: false,
		dataType: 'json',
    	success: function (param) {
			if (param.result == 'logout'){
				alert('로그인해야 사용할 수 있습니다');
				message_socket.close();
			}else if (param.result == 'success') {
        		$('#message').val('');
        		$('#upload-photo').val('');
        		$('#photo-preview').css('display', 'none');
        		$('#preview-image').attr('src', '');
        		message_socket.send('board:');
        		closePaymentModal();
        		//판매글 상태를 예약중으로 변경
        		$.ajax({
        		    url: contextPath + '/product/updateToReservedState.do',
        		    type: 'post',
        		    data: { product_num: productNum}, 
        		    dataType: 'json',
        		    success: function(param) {
        		    	if (param.result == 'logout') {
        	                alert('로그인해야 사용할 수 있습니다');
        	                message_socket.close();
        	            } else if (param.result == 'success') {
        	               	alert('판매글의 상태가 업데이트 되었습니다.');
        	               	message_socket.send('reload');
        	            } else {
        	                alert('판매글 상태 업데이트 실패');
        	            }
        	        },
        	        error: function () {
        	            alert('네트워크 오류 발생');
        	        }
        		});

      		}else {
        		alert('송금 메시지 전송 실패');
      		}
    	},
    	error: function () {
      		alert('네트워크 오류 발생');
    	}
  	});
});

//송금요청 메세지에 있는 송금하기 버튼으로 송금하기 메세지 전송 - 이후 버튼 비활성화
$(document).on('click', '.send-payment-btn', function () {
    const chat_num = $(this).data('chat-num');
    const amount = $(this).data('amount'); // 요청된 금액

    if (!amount || amount <= 0) {
        alert('송금 금액이 유효하지 않습니다.');
        return;
    }
	let money = amount.toLocaleString();
    if (!confirm(money+"원을 송금하시겠습니까?")) return;

    const chatroomNum = $('#chatroom_num').val();
    const formData = new FormData();

    formData.append('chatroom_num', chatroomNum);
    formData.append('message', '[pay:'+amount+'] ${amount.toLocaleString()}원 송금했습니다.');
    formData.append('filename', '');
	// 서버요청
    $.ajax({
        url: contextPath + '/chat/chatWrite.do',
        type: 'post',
        data: formData,
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function (param) {
            if (param.result == 'logout') {
                alert('로그인해야 사용할 수 있습니다');
                message_socket.close();
            } else if (param.result == 'success') {
                $('#message').val('');
                $('#upload-photo').val('');
                $('#photo-preview').css('display', 'none');
                $('#preview-image').attr('src', '');
                message_socket.send('board:');
                message_socket.send('reload');
               
            } else {
                alert('송금 처리 실패');
            }
        },
        error: function () {
            alert('네트워크 오류 발생');
        }
    });
    
 	// 서버에 처리 상태 업데이트 요청
    $.ajax({
        url: contextPath + '/chat/updateProcessedStatus.do',
        type: 'post',
        data: { chat_num: chat_num },
        dataType: 'json',
        success: function (res) {
            if (res.result === 'success') {
                // 버튼 상태 변경
                $button.prop('disabled', true).text('송금 완료').addClass('disabled');
                
            } else if (res.result === 'logout') {
                alert('로그인이 필요합니다.');
                message_socket.close();
            } else {
                alert('송금하기 처리에 실패했습니다.');
            }
        },
        error: function () {
            alert('네트워크 오류 발생');
        }
    });
});

	//송금받기 버튼
	$(document).on('click', '.receive-payment-btn', function () {
	    const amount = $(this).data('amount');
	    const chat_num = $(this).data('chat-num');
	    const $button = $(this);
	    
	    let money = amount.toLocaleString();
	    if (!confirm(money+"원을 받을까요?\n거래가 종료되고 판매완료 상태로 업데이트됩니다.")) return;
		const product_num = ${productChatRoomVO.product.product_num};
		
	 	// 판매글 - 판매완료로 업데이트
	    $.ajax({
	        url: contextPath + '/product/updateToSoldState.do',
	        type: 'post',
	        data: { product_num: product_num,
	        			chatroom_num: $('#chatroom_num').val()},
	        dataType: 'json',
	        success: function (res) {
	            if (res.result === 'success') {
	            	
	            	// 서버에 처리 상태 업데이트 요청
	        	    $.ajax({
	        	        url: contextPath + '/chat/updateProcessedStatus.do',
	        	        type: 'post',
	        	        data: { chat_num: chat_num },
	        	        dataType: 'json',
	        	        success: function (res) {
	        	            if (res.result === 'success') {
	        	            	message_socket.send('reload');
	        	            	
	        	            } else if (res.result === 'logout') {
	        	                alert('로그인이 필요합니다.');
	        	                message_socket.close();
	        	            } else {
	        	                alert('송금받기 처리에 실패했습니다.');
	        	            }
	        	        },
	        	        error: function () {
	        	            alert('네트워크 오류 발생');
	        	        }
	        	    });
	           		
	            } else if (res.result === 'logout') {
	                alert('로그인이 필요합니다.');
	                message_socket.close();
	            } else {
	                alert('판매완료 처리에 실패했습니다.');
	            }
	        },
	        error: function () {
	            alert('네트워크 오류 발생');
	        }
	    });

	});
	
	$('#openPaymentRequestBtn').on('click', function() {
		$('#paymentModalTitle').text('송금 요청');
		$('#paymentType').val('request');
		openPaymentModal();
	});

	$('#openPaymentSendBtn').on('click', function() {
		$('#paymentModalTitle').text('송금 하기');
		$('#paymentType').val('send');
		openPaymentModal();
	});

});
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<!-- 메인 시작 -->
	<div id="pw-main">
		<!-- 상품 정보 -->
		<div class="chat-product-info">
  		<a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${productChatRoomVO.product.product_num}"
     		id="product-info">
    	<c:choose>
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
    	<h2 class="product-title">${productChatRoomVO.product.title}</h2>
    	<c:choose>
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
		<!-- 버튼 영역 -->
		<div class="chat-button-area">
			<!-- 송금 버튼 -->
			<c:choose>
				<%-- 거래 완료 상태: 버튼 비활성화 --%>
				<c:when test="${productChatRoomVO.product.state == 2 && reviewCount != 0}">
					<button class="chat-action-btn" id="paymentDoneBtn" disabled>후기 완료</button>
				</c:when>
				<c:when test="${productChatRoomVO.product.state == 2 && reviewCount == 0}">
					<button class="chat-action-btn" id="review-btn"
						onclick="location.href='${pageContext.request.contextPath}/review/writeReviewForm.do?user_num=${targetUser.user_num}&product_num=${product.product_num}'">후기 작성</button>
				</c:when>
				<%-- 거래 가능 상태 --%>
				<c:when test="${productChatRoomVO.buyer_num == user_num}">
					<button class="chat-action-btn" id="openPaymentSendBtn">송금 하기</button>
				</c:when>
				<c:otherwise>
					<button class="chat-action-btn" id="openPaymentRequestBtn">송금 요청</button>
				</c:otherwise>
			</c:choose>
			<!-- 약속잡기 버튼 -->
			<c:choose>
				<c:when test="${productChatRoomVO.deal_datetime == null}">
					<button class="chat-action-btn" id="openDealModal">약속 잡기</button>
				</c:when>
				<c:otherwise>
					<button class="chat-action-btn" id="openDealModal">약속 수정</button>
				</c:otherwise>
			</c:choose>
			<!-- 매너평가 버튼 -->
			<c:if test="${sessionScope.user_num != targetUser.user_num}">
				<button class="chat-action-btn" onclick="location.href='${pageContext.request.contextPath}/review/writeMannerRateForm.do?rated_num=${targetUser.user_num}&from=chat&chatroom_num=${productChatRoomVO.chatroom_num}'">
				<c:choose>
					<c:when test="${isAlreadyRated}">매너평가 수정</c:when>
					<c:otherwise>매너 평가</c:otherwise>
				</c:choose>
				</button>
			</c:if>
		</div>

		<!-- 상대방 정보 표시 -->
		<c:choose>
			<%-- 내가 구매자 --%>
  			<c:when test="${productChatRoomVO.buyer_num == user_num}">
    			<c:set var="partner" value="${productChatRoomVO.seller}" />
    			<div class="chat-partner-info" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${productChatRoomVO.seller_num}'" style="cursor:pointer;">
        			<img class="partner-photo" src="
  					<c:choose>
              			<c:when test='${empty partner.photo}'>
    						${pageContext.request.contextPath}/images/face.png
              			</c:when>
              			<c:otherwise>
                			${pageContext.request.contextPath}/upload/${partner.photo}
              			</c:otherwise>
            		</c:choose>"  
            			onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';" 
           				 alt="프로필사진">
        			<span class="partner-nickname"><strong class="nickname-highlight">${productChatRoomVO.seller.nickname}</strong> 님과의 대화</span>
    				<c:set var="partner_nickname" value="${productChatRoomVO.seller.nickname}"/>
    			</div>
  			</c:when>
  			<%-- 내가 판매자 --%>
  			<c:otherwise>
    			<c:set var="partner" value="${productChatRoomVO.buyer}" />
    			<div class="chat-partner-info" onclick="location.href='${pageContext.request.contextPath}/user/userDetail.do?user_num=${productChatRoomVO.buyer_num}'" style="cursor:pointer;">
        			<img class="partner-photo" src="
  					<c:choose>
              			<c:when test='${empty partner.photo}'>
                			${pageContext.request.contextPath}/images/face.png
              			</c:when>
              			<c:otherwise>
                			${pageContext.request.contextPath}/upload/${partner.photo}
              			</c:otherwise>
            		</c:choose>"  
            			onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';" 
            			alt="프로필사진">
        			<span class="partner-nickname"><strong class="nickname-highlight">${productChatRoomVO.buyer.nickname}</strong> 님과의 대화</span>
    				<c:set var="partner_nickname" value="${productChatRoomVO.buyer.nickname}"/>
    			</div>
  			</c:otherwise>
		</c:choose>
		
		<!-- 약속 설정 값 출력 공간 -->
		<div id="dealNotice" class="deal-notice" style="display: none;">
  			<span id="dealText"></span>
		</div>
		
		<!-- 채팅 메시지 박스 -->
		<div id="chatting_message">
			<!-- 메시지 들어올 자리 -->
		</div>
		
		<div id="photo-preview" style="display:none; text-align:center; margin-bottom:10px;">
  			<div style="position:relative; display:inline-block;">
    			<img id="preview-image" src="" alt="미리보기" style="max-width:150px; max-height:150px; border-radius:8px; box-shadow:0 0 6px rgba(0,0,0,0.1);">
    			<span id="remove-preview" style="position:absolute; top:-8px; right:-8px; background:#000; color:#fff; border-radius:50%; width:20px; height:20px; font-size:14px; line-height:20px; cursor:pointer;">×</span>
  			</div>
		</div>


		<!-- 채팅 입력 폼 -->
		<form id="chatting_form" enctype="multipart/form-data">
			<input type="hidden" name="chatroom_num"
				value="${productChatRoomVO.chatroom_num}" id="chatroom_num">

			<label for="upload-photo" class="upload-label"> <img
				src="${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png"
				class="upload-icon">
			</label> <input type="file" name="filename" id="upload-photo"
				style="display: none;" accept="image/*">

			<textarea name="message" id="message" rows="2"
				placeholder="메시지를 입력하세요..."></textarea>

			<input type="submit" value="전송" class="send-button">
		</form>
		
		<div class="back-btn-wrapper">
    		<button onclick="history.back();" class="back-btn">목록으로</button>
    	</div>
    	
    	<!-- 송금 요청 / 송금 하기 모달 -->
		<div id="paymentModal" class="modal-overlay" style="display:none;">
  			<div class="modal-content">
    			<button type="button" class="modal-close-btn" onclick="closePaymentModal()">×</button>
    			<div class="payment-info-note">
  					<span>※ 5분 이내에 취소 가능하며,<br>상대가 수락하면 취소할 수 없습니다.</span>
				</div>	
    			<div class="payment-header">
    				<img class="partner-photo" src="
  					<c:choose>
              			<c:when test='${empty partner.photo}'>
    						${pageContext.request.contextPath}/images/face.png
              			</c:when>
              			<c:otherwise>
                			${pageContext.request.contextPath}/upload/${partner.photo}
              			</c:otherwise>
            		</c:choose>"  
            			onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';" 
           				 alt="프로필사진">
  					<div class="receiver-name" id="pay-nickname">${partner_nickname}님에게</div>
  					<div class="pay-amount" id="pay-amount">0원</div>
				</div>

				<div class="pay-shortcuts">
  					<button type="button" class="amount-add-btn" data-amount="10000">+1만</button>
  					<button type="button" class="amount-add-btn" data-amount="50000">+5만</button>
  					<button type="button" class="amount-add-btn" data-amount="100000">+10만</button>
  					<button type="button" class="amount-add-btn" data-amount="500000">+50만</button>
				</div>

				<div class="pay-keypad">
    					<button class="key">1</button><button class="key">2</button><button class="key">3</button>
    					<button class="key">4</button><button class="key">5</button><button class="key">6</button>
    					<button class="key">7</button><button class="key">8</button><button class="key">9</button>
    					<button class="key">00</button><button class="key">0</button><button class="key" id="del">←</button>
				</div>

				<div class="pay-confirm-btn">
					<c:choose>
						<c:when test="${productChatRoomVO.buyer_num == user_num}">
							<button type="button" id="paySubmitBtn">송금하기</button>
						</c:when>
						<c:otherwise>
  							<button type="button" id="paySubmitBtn">요청하기</button>
						</c:otherwise>
					</c:choose>
				</div>
  			</div>
		</div>
    	
		
		<!-- 약속잡기 -->
		<div id="dealModal" class="modal-overlay" style="display:none;">
  			<div class="modal-content">
  				<button type="button" class="modal-close-btn" onclick="closeModal()">×</button>
    			<h3>약속 시간 및 장소</h3>
    			<form id="dealForm">
      				<label>약속 시간</label><br>
      				<input type="datetime-local" name="deal_datetime" required><br><br>

				    <label for="loc">거래희망장소</label>
				    <input type="text" name="loc" id="loc" maxlength="10" class="input-check" readonly>
					<c:set var="xLoc" value="${productChatRoomVO.deal_x_loc > 0 ? productChatRoomVO.deal_x_loc : product.x_loc}" />
					<c:set var="yLoc" value="${productChatRoomVO.deal_y_loc > 0 ? productChatRoomVO.deal_y_loc : product.y_loc}" />
					<input type="hidden" name="deal_x_loc" id="deal_x_loc" value="${xLoc}" />
					<input type="hidden" name="deal_y_loc" id="deal_y_loc" value="${yLoc}" />
					
				    <div id="map" style="width:100%; height:300px;"></div>
				    
				    
      				<input type="hidden" name="chatroom_num" value="${productChatRoomVO.chatroom_num}">
					
					<div class="modal-button-group">
      					<button type="submit">설정하기</button>
      					<button type="button" id="deleteDealBtn">삭제하기</button>
      				</div>
    			</form>
  			</div>
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
            var script2 = document.createElement('script');
            script2.src = "${pageContext.request.contextPath}/js/promiseModal_updated.js";
            document.head.appendChild(script2);
        });
    };
    document.head.appendChild(script);
</script>
<%-- <script type="module" src="${pageContext.request.contextPath}/js/promiseModal_updated.js"></script> --%>

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
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
