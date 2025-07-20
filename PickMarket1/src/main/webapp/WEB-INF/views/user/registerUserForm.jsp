<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang='ko'>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>

<script type="text/javascript">
	let authChecked = 0;
	let idChecked = 0;
	let timerInterval;
	
	$(function(){		
		//이메일 중복 체크
		$('#id_check').click(function(){
			if(!/^\w+@\w+\.\w+$/.test($('#id').val())){
				alert('이메일 형식에 맞게 입력하세요!');
				$('#id').val('').focus();
				return;
			}
			//서버와 통신
			$.ajax({
				url:'checkUniqueInfo.do',
				type:'post',
				data:{id:$('#id').val()},
				dataType:'json',
				success:function(param){
					if(param.result == 'idNotFound'){
						idChecked = 1;
						$('#id_notice').css('color','#000000').text('등록 가능합니다. 인증버튼을 눌러주세요.');
						$('#auth_get').removeAttr('disabled');
					}else if(param.result == 'idDuplicated'){
						idChecked = 0;
						$('#id_notice').css('color','red').text('중복된 이메일입니다.');
						$('#id').val('').focus();
					}else{
						idChecked = 0;
						alert('이메일 중복 체크 오류 발생');
					}
				},
				error:function(){
					idChecked = 0;
					alert('네트워크 오류 발생');
				}
			});
		});//end of click
		
		//이메일 중복 안내 메시지 초기화 및 이메일 중복 값 초기화 및 인증버튼 비활성화
		$('#register_form #id').keydown(function(){
			idChecked = 0;
			authChecked = 0;
			$('#id_notice').text('');
			$('#auth_get').attr('disabled', true);
			$('.auth-area').hide();
		});
		
		$('#auth_get').click(function(){
			$(this).prop('disabled', true);
			//이메일 인증코드 전송
			$.ajax({
				url: '${pageContext.request.contextPath}/user/sendEmail.do',
				type: 'post',
				data: { email:$('#id').val()},
				dataType:'json',
				success: function(param) {
        
					if(param.result == 'success'){
			    		alert('인증코드를 이메일로 전송했습니다.');
			        	$('.auth-area').show();
			        	
			        	 // 인증 유효시간 계산 및 표시
			        	const issuedTime = parseInt(param.issuedTime);
			        	const validDuration = 5 * 60 * 1000; // 5분
			        	const expireTime = issuedTime + validDuration;
			        	
			        	clearInterval(timerInterval); // 기존 타이머 제거
			        	timerInterval = setInterval(function(){
		                const now = Date.now();
		                const remaining = expireTime - now;

		                if (remaining <= 0) {
		                     clearInterval(timerInterval);
		                     $('#auth_timer').text('인증코드가 만료되었습니다.').css('color', 'red').show();
		                     $('#auth_get').prop('disabled', false);
		                 } else {
		                     const min = Math.floor(remaining / 1000 / 60);
		                     const sec = Math.floor((remaining / 1000) % 60);
		                     $('#auth_timer').text('남은 시간: ' + min + '분' + sec.toString().padStart(2, '0')+'초');
		                 }
		               }, 1000);
					}
					else{
						alert('이메일 전송 실패');
						$('#auth_get').prop('disabled', false);
					}
			    },
			    error: function() {
			        alert('네트워크 오류 발생');
			        $('#auth_get').prop('disabled', false);
			    }
			});
			
		});
		//인증번호와 일치확인 여부
		$('#auth_verify').click(function() {
			$('#auth_timer').text('').hide();
		    $.ajax({
		        url: '${pageContext.request.contextPath}/user/verifyCode.do',
		        type: 'post',
		        data: { code: $('#auth_input').val() },
		        dataType:'json',
		        success: function(param) {
		            if (param.result === 'verified') {
		            	authChecked = 1;
		                clearInterval(timerInterval); 
		                $('#auth_result').css('color', 'green').text('인증 성공!');
		            } else if (param.result === 'expired') {
		            	authChecked = 0;
		                $('#auth_result').css('color', 'red').text('인증코드가 만료되었습니다.');
		                $('#auth_get').prop('disabled', false);
		            } else {
		            	authChecked = 0;
		                $('#auth_result').css('color', 'red').text('인증 실패!');
		                $('#auth_get').prop('disabled', false);
		            }
		        },
		        error: function() {
		        	authChecked = 0;
		            alert('인증 확인 실패');
		            $('#auth_get').prop('disabled', false);
		        }
		    });
		});
		
		//회원정보 등록 유효성 체크
		$('#register_form').submit(function(){
			const items = document.querySelectorAll('.input-check');
			for(let i=0;i<items.length;i++){
				if(items[i].value.trim()==''){
					const label = document.querySelector('label[for="'+items[i].id+'"]');
					alert(label.textContent + ' 입력 필수');
					items[i].value='';
					items[i].focus();
					return false;
				}//end of if
				
				if(items[i].id == 'id' && !/^\w+@\w+\.\w+$/.test($('#id').val())){
					alert('이메일 형식에 맞게 입력하세요!');
					$('#id').val('').focus();
					return false;
				}
				if(items[i].id == 'id' && idChecked==0){
					alert('아이디(이메일) 중복체크 필수');
					return false;
				}
				
				if(items[i].id == 'id' && authChecked==0){
					alert('아이디(이메일) 인증 필수');
					return false;
				}
				
				if(items[i].id == 'passwd' && 
						   !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{}[\]|\\:;\"'<>,.?/~`]).{8,16}$/.test($('#passwd').val())){
					alert('비밀번호는 영문, 숫자, 특수문자를 포함한 8~16자로 입력하세요.');
					$('#passwd').val('').focus();
					return false;
				}
				
				if(items[i].id == 'passwd_check' && ($('#passwd_check').val() != $('#passwd').val())){
					alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
					return false;
				}
				
			}

		});
		
	});
</script>
</head>
<body>
<div class="page-main">
   <jsp:include page="/WEB-INF/views/common/header.jsp"/>
   <div class="content-main" id="signup-main">
      <h2 id="signup-title">회원가입</h2>
      <form id="register_form" action="registerUser.do" method="post">
         <ul>
            <li>
               <div class="form-notice">
               <span id="id_notice"></span>
               </div>
               <label for="id">이메일(ID)</label>
               <div class="inline-input-group">
                  <input type="email" name="id" id="id" maxlength="50" autocomplete="off" class="input-check">
                  <input type="button" value="중복확인" id="id_check">
                  <input type="button" value="인증" id="auth_get" disabled>
               </div>
            </li>
            <li class="auth-area">
               <label for="auth">인증번호 입력</label>
               <div class="auth-input-group">
                  <input type="text" id="auth_input" placeholder="인증번호입력" autocomplete="off" class="input-check">
                  <input type="button" value="인증확인" id="auth_verify">
               </div>
               <div class="form-notice">
                  <span id="auth_timer"></span>
                  <span id="auth_result"></span>
               </div>
            </li>
            <li>
               <label for="name">이름</label>
               <input type="text" name="name" id="name" maxlength="10" class="input-check">
            </li>
            <li>
               <label for="passwd">비밀번호</label>
               <input type="password" name="passwd" id="passwd" maxlength="30" class="input-check">
            </li>
            <li>
               <label for="passwd_check">비밀번호 확인</label>
               <input type="password" name="passwd_check" id="passwd_check" maxlength="30" class="input-check">
            </li>
            <li>
               <label for="phone">전화번호</label>
               <input type="text" name="phone" id="phone" autocomplete="off" maxlength="15" class="input-check">
            </li>
            <li>
                <label for="address">주소</label>
                <div class="address-input-group">
	           		<input type="text" id="address" name="address" class="input-check" readonly>
	           		<input type="hidden" id="region_cd" name="region_cd">
					<input type="button" value="현재 위치로 지역 입력" onclick="getCurrentLocation()">
				</div>
            </li>
         </ul>

         <div class="login-link">
           이미 회원이신가요? <a href="${pageContext.request.contextPath}/user/loginForm.do">로그인하기</a>
         </div>
         <div class="align-center">
            <input type="submit" value="가입하기">
         </div>
      </form>
   </div>
   
<!-- 주소 API 시작 -->
<script type="text/javascript">
    var kakaoApiKey = "${kakaoApiKey}";
    var script = document.createElement('script');
    script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=" + kakaoApiKey + "&autoload=false&libraries=services";
    script.onload = function () {
        kakao.maps.load(function () {
        	
        });
    };
    document.head.appendChild(script);
</script>

<script>
    function getCurrentLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var lat = position.coords.latitude;  // 위도
                var lon = position.coords.longitude; // 경도

                console.log("현재 위치 - 위도: " + lat + ", 경도: " + lon);

                // Kakao Geocoder 사용하여 주소 변환
                var geocoder = new kakao.maps.services.Geocoder();
                var locPosition = new kakao.maps.LatLng(lat, lon);

                // 좌표로 주소를 변환
                geocoder.coord2RegionCode(locPosition.getLng(), locPosition.getLat(), function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var region_nm = result[0].region_1depth_name + ' ' +
                        				result[0].region_2depth_name + ' ' +
                        				result[0].region_3depth_name;

                        document.getElementById("address").value = region_nm;
                        
                        $.ajax({
                            url: '${pageContext.request.contextPath}/location/getRegionCd.do',
                            type: 'GET',
                            data: { region_nm: region_nm },
                            dataType: 'json',
                            success:function(param){
            					if(param.result == 'success'){
            						console.log("받아온 region_cd:", param);
                                    $("#region_cd").val(param.region_cd);
            					}else if(param.result == 'region_nmIsNull'){
            						alert('지역명 없음');
            					}else if(param.result == 'region_cdNotFound'){
            						alert('지역코드 없음');
            					}else{
            						alert('지역 에러')
            					}
            				},
                            error: function() {
                                alert('네트워크 오류 발생');
                            }
                        });
                    } else {
                    	alert('주소 변환 실패');
                    }
                });
            }, function(error) {
            	alert('위치 정보 오류', error)
            });
        } else {
            alert("이 브라우저에서는 위치 정보를 사용할 수 없습니다.");
        }
    }
</script>
<!-- 주소 API 끝 -->

</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>