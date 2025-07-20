$(function() {
	const chatroom_num = $('#chatroom_num').val();

	// 채팅방 처음 진입 시 약속 정보 불러오기
	$.ajax({
	   url: contextPath + '/chat/getDeal.do',
	   type: 'get',
	   data: {chatroom_num:chatroom_num},
	   dataType: 'json',
	   success: function (data) {
	   		if (data.result === 'success' && data.deal) {
				const datetime = data.deal.deal_datetime.replace('T', ' ').slice(0, 16);
				const x = parseFloat(data.deal.deal_x_loc);
				const y = parseFloat(data.deal.deal_y_loc);

				const geocoder = new kakao.maps.services.Geocoder();
				geocoder.coord2Address(y, x, function(result, status) {
				  if (status === kakao.maps.services.Status.OK) {
				    const address = result[0].address.address_name;

				    $('#dealText').text(`거래 예정 일시: ${datetime} / 장소: ${address}`);
				    $('#dealNotice').show();
				    $('#openDealModal').text('약속 수정');
				  } else {
				    $('#dealText').text(`거래 예정 일시: ${datetime} / 위치(${x}, ${y})`);
				    $('#dealNotice').show();
				    $('#openDealModal').text('약속 수정');
				  }
				});
				
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

  // 약속 저장
  $('#dealForm').submit(function(e) {
    e.preventDefault();
    const formData = $(this).serialize();

    $.ajax({
      url: contextPath + '/chat/setDeal.do',
      type: 'post',
      data: formData,
      dataType: 'json',
      success: function(param) {
        if (param.result === 'success') {
          alert('약속 정보가 저장되었습니다!');
          closeModal();
		  
		  const x = parseFloat($('input[name="deal_x_loc"]').val());
		  const y = parseFloat($('input[name="deal_y_loc"]').val());
		  const datetime = $('input[name="deal_datetime"]').val().replace('T', ' ');
		  

		  const geocoder = new kakao.maps.services.Geocoder();
		  geocoder.coord2Address(y, x, function (result, status) {
		    if (status === kakao.maps.services.Status.OK) {
		      const address = result[0].address.address_name;
		      $('#dealText').text(`거래 예정 일시: ${datetime} / 장소: ${address}`);
		      $('#dealNotice').show();
		      $('#openDealModal').text('약속 수정');
		    }
		  });
		  
		  if (window.message_socket && window.message_socket.readyState === WebSocket.OPEN) {
		      window.message_socket.send('reload');
		  }
        } else {
          alert('저장 실패');
        }
      },
      error: function() {
        alert('서버 오류 발생');
      }
    });
  });

  // 약속 불러오기
  $('#openDealModal').click(function () {
    const chatroom_num = $('#chatroom_num').val();

    $.ajax({
      url: contextPath + '/chat/getDeal.do',
      type: 'get',
      data: { chatroom_num: chatroom_num },
      dataType: 'json',
      success: function (data) {
        if (data.result === 'success' && data.deal) {
		  const x = parseFloat(data.deal.deal_x_loc);
		  const y = parseFloat(data.deal.deal_y_loc);
          const datetime = data.deal.deal_datetime.replace('T', ' ').slice(0, 16);

          // 좌표 → 주소로 변환
          const geocoder = new kakao.maps.services.Geocoder();
          geocoder.coord2Address(y, x, function (result, status) {
            if (status === kakao.maps.services.Status.OK) {
              const address = result[0].address.address_name;

              $('#dealText').text(`거래 예정 일시: ${datetime} / 장소: ${address}`);
              $('#dealNotice').show();
            }
          });

          $('input[name="deal_datetime"]').val(data.deal.deal_datetime.replace(' ', 'T').slice(0, 16));
          $('input[name="deal_x_loc"]').val(x);
          $('input[name="deal_y_loc"]').val(y);
        } else {
          $('#dealForm')[0].reset();
          $('#dealNotice').hide();
        }
        openModal();
      },
      error: function () {
        alert('약속 정보 조회 실패');
      }
    });
  });
  
  // 약속 삭제
    $('#deleteDealBtn').click(function () {
      if (confirm('정말 약속을 삭제하시겠습니까?')) {
        const chatroom_num = $('#chatroom_num').val();

        $.ajax({
          url: contextPath + '/chat/deleteDeal.do',
          type: 'post',
          data: {chatroom_num:chatroom_num},
          dataType: 'json',
          success: function (data) {
            if (data.result === 'success') {
              alert('약속이 삭제되었습니다.');
  			closeModal();
              $('#dealNotice').hide();
              $('#dealText').text('');
              $('#dealForm')[0].reset();
              $('#openDealModal').text('약속 잡기');
  			
  			const defaultLat = parseFloat($('#product_x_loc').val()); // 기본 위도
  			const defaultLng = parseFloat($('#product_y_loc').val()); // 기본 경도
  			
  			$('#deal_x_loc').val(defaultLat);
  			$('#deal_y_loc').val(defaultLng);
  			
  			initMapWithSavedCoordinates(defaultLat, defaultLng);
  			if (window.message_socket && window.message_socket.readyState === WebSocket.OPEN) {
  			    window.message_socket.send('reload');
  			}
            } else {
              alert('삭제 실패');
            }
          },
          error: function () {
            alert('서버 오류 발생');
          }
        });
      }
    });



});
