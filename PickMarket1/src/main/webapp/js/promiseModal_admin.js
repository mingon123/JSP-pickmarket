$(function() {
	const chatroom_num = $('#chatroom_num').val();

	// 약속 정보 조회 (처음 진입 시)
	$.ajax({
	   url: contextPath + '/chat/getDeal.do',
	   type: 'get',
	   data: {chatroom_num: chatroom_num},
	   dataType: 'json',
	   success: function (data) {
	   		if (data.result === 'success' && data.deal) {
				const datetime = data.deal.deal_datetime.replace('T', ' ').slice(0, 16);
				const x = parseFloat(data.deal.deal_x_loc);
				const y = parseFloat(data.deal.deal_y_loc);

				$('#dealText').text(`거래 예정 일시: ${datetime} / 위치(${x}, ${y})`);
				$('#dealNotice').show();
				
				// 지도 위치 표시
				if (map) {
				  const position = new kakao.maps.LatLng(y, x);
				  map.setCenter(position);

				  if (!window.dealMarker) {
				    window.dealMarker = new kakao.maps.Marker({
				      position: position,
				      map: map
				    });
				  } else {
				    window.dealMarker.setPosition(position);
				  }
				}
	     	}
	   },
	   error: function () {
	      console.log('초기 약속 정보 조회 실패');
	    }
	});

	// 모달 열기 버튼
	$('#openDealModal').click(function () {
	    openModal(); // 단순히 열기만
	});

	// 지도 리레이아웃 + 센터 세팅
	window.openModal = function() {
		$('#dealModal').fadeIn(function () {
		  if (map) {
		    map.relayout();
		    const lat = parseFloat(document.getElementById("deal_x_loc").value);
		    const lng = parseFloat(document.getElementById("deal_y_loc").value);
		    map.setCenter(new kakao.maps.LatLng(lat, lng));
		  }
		});
	};

	window.closeModal = function() {
	    $('#dealModal').fadeOut();
	};

});
