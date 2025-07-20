<!-- 비밀번호 찾기 JSP -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw2.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	let timerInterval;
	$(function(){
		$('#id').focus();
		$('.pw-auth-area, .pw-result-area').hide();
		
		$('#id').on('keydown', function(e) {
	        if (e.key === 'Enter' || e.keyCode === 13) {
	            e.preventDefault();
	            $('#pw-auth-get').click();
	        }
	    });

		$('#pw-auth-get').click(function(){
			
			if(!/^\w+@\w+\.\w+$/.test($('#id').val())){
				alert('이메일 형식을 확인하세요!');
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
						$('#id_notice').css('color','red').text('존재하지 않는 아이디입니다.');
					}else if(param.result == 'idDuplicated'){
						$('#id_notice').css('color','#000000').text('메일로 인증코드가 전송됩니다.');
						//인증코드 전송
						$.ajax({
							url: '${pageContext.request.contextPath}/user/sendEmail.do',
							type: 'post',
							data: { email: $('#id').val() },
							dataType: 'json',
							success: function(param){
								if(param.result === 'success'){
									alert('인증코드 전송 완료!');
									$('.pw-auth-area').show();

									const issuedTime = parseInt(param.issuedTime);
									const expireTime = issuedTime + 5 * 60 * 1000;
									
									console.log('issuedTime:', param.issuedTime);
									
									clearInterval(timerInterval);
									timerInterval = setInterval(function(){
										const now = Date.now();
										const remaining = expireTime - now;
										if(remaining <= 0){
											clearInterval(timerInterval);
											$('#pw-auth-timer').text('인증코드가 만료되었습니다.').css('color','red');
										} else {
											const min = Math.floor(remaining / 1000 / 60);
											const sec = Math.floor((remaining / 1000) % 60);
											$('#pw-auth-timer').text('남은 시간: ' + min + '분' + sec.toString().padStart(2, '0')+'초');
										}
									}, 1000);
								}else{
									alert('전송 실패');
								}
							}
						});
						
					}else{
						alert('아이디 확인 오류 발생');
					}
				},
				error:function(){
					idChecked = 0;
					alert('네트워크 오류 발생');
				}
			});
		});
		//이메일 중복 안내 메시지 초기화 및 이메일 중복 값 초기화 및 인증버튼 비활성화
		$('#id').keydown(function(){
			$('#id_notice').text('');
			$('#auth_get').attr('disabled', true);
			$('.auth-area').hide();
		});
		
		$('#pw-auth-input').on('keydown', function(e) {
	        if (e.key === 'Enter' || e.keyCode === 13) {
	            e.preventDefault();
	            $('#pw-auth-verify').click();
	        }
	    });

		$('#pw-auth-verify').click(function(){
			const code = $('#pw-auth-input').val();
			$.ajax({
				url: '${pageContext.request.contextPath}/user/verifyCode.do',
				type: 'post',
				data: { code: code },
				dataType: 'json',
				success: function(param){
					if(param.result === 'verified'){
						$('#pw-auth-result').text('인증 성공!').css('color','green');
						$('#pw-auth-timer').hide();
						// 비밀번호 조회
						$.ajax({
							url: '${pageContext.request.contextPath}/user/getMaskedPassword.do',
							type: 'post',
							data: { id: $('#id').val() },
							dataType: 'json',
							success: function(res){
								$('.pw-result-area').show();
								$('#pw-result-id').text($('#id').val());
								$('#pw-result-passwd').text(res.maskedPasswd);
							}
						});
					}else if(param.result === 'expired'){
						$('#pw-auth-result').text('인증코드가 만료되었습니다.').css('color','red');
					}else{
						$('#pw-auth-result').text('인증 실패!').css('color','red');
					}
				}
			});
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main" id="pw-main">
		<h2 id="pw-title">비밀번호 찾기</h2>
		<form id="pw-form" method="post">
			<ul>
				<li>
				<div id="id_notice_div">
					<span id="id_notice"></span>
				</div>
					<label for="id">아이디(이메일)</label>
					<input type="email" id="id" name="id" class="form-input" autocomplete="off">
					<input type="button" value="인증번호 받기" id="pw-auth-get">
				</li>

				<li class="pw-auth-area">
					<label for="pw-auth-input">인증번호</label>
					<input type="text" id="pw-auth-input" placeholder="인증번호 입력" class="form-input" autocomplete="off">
					<input type="button" value="인증확인" id="pw-auth-verify">
					<div class="form-notice">
						<span id="pw-auth-timer"></span><br>
						<span id="pw-auth-result"></span>
					</div>
				</li>

				<li class="pw-result-area">
					<label>인증된 이메일:</label>
					<span id="pw-result-id"></span><br>
					<label>비밀번호(일부 마스킹):</label>
					<span id="pw-result-passwd"></span>
				</li>
			</ul>
		</form>
		<div class="pw-login-link">
			<a href="${pageContext.request.contextPath}/user/loginForm.do">로그인하러 가기</a>
		</div>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
