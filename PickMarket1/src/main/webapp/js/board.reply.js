$(function(){
	let rowCount = 10;
	let currentPage;
	let count;
	/*========================
	 *댓글 목록
	 *=======================*/
	//댓글 목록
	function selectList(pageNum) {
	    currentPage = pageNum;
	    // 로딩 이미지 노출
	    $('#loading').show();

	    // 서버와 통신
	    $.ajax({
	        url: 'listReply.do',
	        type: 'post',
	        data: {
	            pageNum: pageNum,
	            rowCount: rowCount,
	            board_num: $('#board_num').val()
	        },
	        dataType: 'json',
	        success: function(param) {
	            count = param.count;
				$('#reply-total').text(param.count);
	            if (pageNum == 1) {
	                // 처음 호출시는 해당 ID의 div의 내부 내용물을 제거
	                $('#output').empty();
	            }
				if (count === 0) {
					$('#output').html('<div class="no-reply-msg">첫 댓글을 남겨주세요!</div>');
					$('.paging-button').hide(); // 다음 버튼 숨김
					return; // 아래 반복문 스킵
				}

	            $(param.list).each(function(index, item) {
	                let output = '<div class="reply-item">';
					output += '  	<div class="reply-header">';
					output += '    	<span class="reply-nickname">' + item.nickname + '</span>';
					output += '    <div class="reply-dates">';
					output += '    	<span class="reply-date">' + item.breply_date + '</span>';
					if (item.breply_modidate) {
					// 수정일이 존재하면 '최근 수정일' 추가
					     output += ' <span class="modify-date"> ·  ' + item.breply_modidate + '(수정됨)</span>';
					 }
					output += '  </div>';
					output += '  </div>';
	                output += '  <div class="reply-content">' + item.breply_content + '</div>';

	                // 로그인한 회원번호와 작성자의 회원번호 일치 여부 체크
	                if (param.user_num == item.user_num) {
	                    // 로그인한 회원번호와 작성자 회원번호 일치
						output += '  <div class="reply-btns">';
						output += ' 	<input type="button" data-renum="' + item.breply_num + '" value="수정" class="modify-btn">';
						output += ' 	<input type="button" data-renum="' + item.breply_num + '" value="삭제" class="delete-btn" >';
						output += '</div>';
					}
	                output += '</div>';

	                // 문서 객체에 추가
	                $('#output').append(output);
	            });

	            // 페이지 버튼 처리
	            if (currentPage >= Math.ceil(count / rowCount)) {
	                // 다음 페이지가 없음
	                $('.paging-button').hide();
	            } else {
	                // 다음 페이지가 존재
	                $('.paging-button').show();
	            }
	        },
	        error: function() {
	            $('#loading').hide();
	            alert('네트워크 오류 발생');
	        }
	    });
	}


	//페이지 처리 이벤트 연결 (다음 댓글 보기 버튼 클릭 시 데이터 추가)
	$('.paging-button input').click(function(){
		selectList(currentPage + 1);
	});
	
	/*========================
	 *댓글 등록
	 *=======================*/
	//댓글 등록 이벤트 연결
	$('#re_form').submit(function(event){
		if($('#breply_content').val().trim()==''){
			alert('내용을 입력하세요');
			$('#breply_content').val('').focus();
			return false;
		}//end of if
		
		//form 이하의 태그에 입력한 데이터를 모두 읽어서 쿼리 스트링으로 반환
		let form_data=$(this).serialize();
		// 서버와 통신
		$.ajax({
			url:'writeReply.do',
			type:'post',
			data:form_data,
			dataType:'json',
			success:function(param){
				if(param.result=='logout'){
					alert('로그인해야 작성할 수 있습니다');
				}else if(param.result == 'success'){
					//폼 초기화
					initForm();
					//댓글 작성이 성공하면 새로 입력한 글을 포함해서 첫 번째 페이지에 게시글 목록을 다시 호출함
					selectList(1);
					
				}else{
					alert('댓글 등록 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');	
			}
		});//end of ajax
		
		//기본이벤트 제거
		event.preventDefault();
	});
	//댓글 작성 폼 초기화
	function initForm() {
		$('textarea').val('');
		$('#re_first .letter-count').text('300/300');
	}
	
	/*========================
	 *댓글 수정
	 *=======================*/
	//댓글 수정 버튼 클릭 시 수정폼 노출
	$(document).on('click','.modify-btn',function(){
		//댓글 번호
		let breply_num = $(this).attr('data-renum');
		//댓글 내용
		let breply_content = $(this)
		        .closest('.reply-item')
		        .find('.reply-content')
		        .html()
		        .replace(/<br\s*\/?>/gi, '\n');
		                                                            //g:지정문자열 모두, i:대소문자 무시
		//댓글 수정폼 UI
		let modifyUI = '<form id="mre_form">';
		    modifyUI += '<input type="hidden" name="breply_num" id="mre_num" value="' + breply_num + '">';
		    modifyUI += '<textarea rows="3" cols="50" name="breply_content" id="mre_content" class="rep-content">' + breply_content + '</textarea>';
		    modifyUI += '<div id="mre_first"><span class="letter-count">300/300</span></div>';
		    modifyUI += '<div id="mre_second" class="align-right">';
		    modifyUI += ' <input type="submit" value="수정">';
		    modifyUI += ' <input type="button" value="취소" class="re-reset">';
		    modifyUI += '</div>';
		    modifyUI += '</form>';
		
		//이전에 이미 수정하는 댓글이 있을 경우 수정버튼을 클릭하면 숨김 sub-item 클래스로 지정한 div를 환원시키고 수정폼을 제거
		initModifyForm();
		//지금 클릭해서 수정하고자 하는 데이터는 감추기
		//수정 버튼을 감싸고 있는 div
		$(this).closest('.reply-btns').hide();
		
		//수정폼을 수정하고자 하는 데이터가 있는 div에 노출
		 $(this).closest('.reply-item').append(modifyUI);
		
		//입력한 글자수 셋팅
		let inputLength = $('#mre_content').val().length;
		let remain = 300-inputLength;
		remain += '/300';
		
		//문서 객체에 반영
		$('#mre_first.letter-count').text(remain);
		
	});//end of on
	
	//댓글 수정폼 초기화
	function initModifyForm(){
		$('.sub-item').show();//div 노출
		$('#mre_form').remove();//수정폼 제거
	}
	
	//수정폼에서 취소 버튼 클릭 시 수정폼 초기화
	$(document).on('click','.re-reset',function(){
		const replyBox = $('#mre_form').closest('.reply-item');
		replyBox.find('.reply-btns').show();
		initModifyForm();
	});
	
	//댓글 수정
	$(document).on('submit','#mre_form',function(event){
		const contentVal = $('#mre_content').val().trim(); 
		if($('#mre_content').val().trim()==''){
			alert('내용을 입력하세요');
			$('#mre_content').val('').focus();
			return false;
		}
		
		//폼에 입력한 데이터 반환
		let form_data = $(this).serialize();
		
		//서버와 통신
		$.ajax({
			url:'updateReply.do',
			type:'post',
			data:form_data,
			dataType:'json',
			success:function(param) {
				if(param.result == 'logout'){
					alert('로그인해야 수정할 수 있습니다');
				}else if(param.result == 'success'){
					const replyBox = $('#mre_form').closest('.reply-item');
					
					replyBox.find('.reply-content').html(useBrNoHtml(contentVal));
					// 수정일도 업데이트 (서버에서 새로 받은 날짜 반영)
					if (param.breply_modidate) {
						const dateContainer  = replyBox.find('.reply-dates');
						dateContainer .find('.modify-date').remove(); // 기존 수정일 제거
						dateContainer .append('<span class="modify-date"> · ' + param.breply_modidate + '(수정됨)</span>');
					 }
					 replyBox.find('.reply-btns').show();
					//수정 폼 삭제 및 초기화
					initModifyForm();
				}else if(param.result == 'wrongAccess'){
					alert('타인의 글을 수정할 수 없습니다');
				}else{
					alert('댓글 수정 오류 발생');
				}
				
			},
			error:function(){
				alert('네트워크 오류 발생');
				
			}
		})
		
		//기본 이벤트 제거
		event.preventDefault();
	});
	/*========================
	 *HTML 태그를 허용하지 않으면서 줄바꿈
	 *=======================*/
	function useBrNoHtml(str){
		if(!str) str;
		
		return str.replace(/</g, '&1t;').replace(/>/g, '&gt;').replace(/\r\n/g, '<br>').replace(/\r/g, '<br>').replace(/\n/g, '<br>');		
	}
		
	/*========================
	 *댓글 등록 및 수정 공통 (textarea에 내용 입력시 글자수 체크)
	 *=======================*/
	$(document).on('keyup','textarea',function(){
		//입력한 글자수 구함
		let inputLength = $(this).val().length;
		
		if(inputLength > 300){//300자를 넘어선 경우
			$(this).val($(this).val().substring(0,300));
		}else{//300자 이하인 경우
			let remain = 300 - inputLength;
			remain += '/300';
			if($(this).attr('id') =='breply_content'){
				//등록 폼 글자수
				$('#re_first .letter-count').text(remain);
			}else{
				//수정 폼 글자수
				$('#mre_first .letter-count').text(remain);
			}
		}
	});
	/*========================
	 *댓글 삭제
	 *=======================*/
	$(document).on('click','.delete-btn',function(){
		//댓글 번호
		let breply_num = $(this).attr('data-renum');
		//서버와 통신
		$.ajax({
			url:'deleteReply.do',
			type:'post',
			data:{breply_num:breply_num},
			dataType:'json',
			success:function(param){
				if(param.result == 'longout'){
					alert('로그인해야 삭제할 수 있습니다');	
				}else if(param.result == 'success'){
					alert('삭제 완료');
					selectList(1);
				}else if(param.result == 'wrongAccess'){
					alert('타인의 글을 삭제할 수 없습니다');
				}else{
						alert('댓글 삭제 오류 발생');
					}
			},
			error:function(){
				alert('네트워크 오류 발생');
				
			}
		})
	});
	
	/*========================
	 *초기 데이터(목록) 호출
	 *=======================*/
	selectList(1);
});