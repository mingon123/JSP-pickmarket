$(function(){
	/*=================================================
	* 찜 선택 여부와 선택한 총 개수 읽기
	*=================================================*/
	function selectFav(){
		$.ajax({
			url:'getFav.do',
			type:'post',
			data:{product_num:$('#output_fav').attr('data-num')},
			dataType:'json',
			success:function(param){
				displayFav(param);
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
	}
	/*=================================================
	* 찜 등록(및 삭제) 이벤트 연결
	*=================================================*/
	$('#output_fav').click(function(){
		//서버와 통신
		$.ajax({
			url:'writeFav.do',
			type:'post',
			data:{product_num:$(this).attr('data-num')},
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 찜을 눌러주세요!');
				}else if(param.result == 'success'){
					param.alarm = 0;
					displayFav(param);
				}else{
					alert('찜 등록/삭제 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
	});
	/*=================================================
	* 찜 표시 함수
	*=================================================*/
	function displayFav(param){
		let output;
		if(param.status == 'yesFav'){ //찜 선택
			output = '../images/heart.png';
			// 알림 상태에 따라 버튼설정
			if(param.alarm == 0){
				$('#alert_button').text('🔔 알림 끄기').attr('data-flag', 'off');
			} else {
				$('#alert_button').text('🔕 알림 켜기').attr('data-flag', 'on');
			}
			$('#alert_button_box').show(); // 알림 버튼 보이기
			
		}else{ //찜 미선택
			output = '../images/unheart.png';
			$('#alert_button_box').hide(); // 찜 안하면 알림 버튼 숨김			
		}
		//문서 객체에 설정
		$('#output_fav').attr('src',output);
		$('#output_fcount').text(param.count);		
	}	
	/*=================================================
	* 초기 데이터 호출
	*=================================================*/
	selectFav();
	
	/*=================================================
	* 알림 토글
	*=================================================*/
	$('#alert_button').click(function(e){
		e.stopPropagation();
		e.stopPropagation();
		
		const flag = $(this).attr('data-flag');
		const url = flag == 'on'? contextPath + '/notification/updateOnFavAlarm.do'
		: contextPath + '/notification/updateOffFavAlarm.do';

		$.ajax({
			url: url,
			type: 'post',
			data: {
				product_num: $('#output_fav').attr('data-num')
			},
			dataType: 'json',
			success: function(res){
				if(res.result === 'success'){
					// 현재 상태 뒤집기
					const flag = $('#alert_button').attr('data-flag');
						if(flag === 'on'){
							$('#alert_button').text('🔔 알림 끄기').attr('data-flag', 'off');
						}else{
							$('#alert_button').text('🔕 알림 켜기').attr('data-flag', 'on');
						}
				}else if(res.result === 'logout'){
					alert('로그인 후 이용해주세요!');
				}else{
					alert('처리 실패');
				}
			},
			error: function(){
				alert('네트워크 오류 발생');
			}
		});
	});

});