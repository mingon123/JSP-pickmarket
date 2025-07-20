<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
$(function(){
    const defaultImage = "${pageContext.request.contextPath}/images/face.png";
    let nicknameChecked = 0;
    // 닉네임 중복 확인
    $('#nickname_check').click(function(){
        const current = $('#nickname').val().trim();
        const original = $('#originalNickname').val().trim();
        const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,15}$/;
        
        if (!nicknameRegex.test(current)) {
            alert('닉네임은 한글, 영문, 숫자만 가능하며 2~15자 사이여야 합니다.');
            $('#nickname').focus();
            return;
        }

        // 현재 닉네임 그대로일 경우
        if (current === original && current !== "") {
            nicknameChecked = 1;
            $('#nickname_notice').css('color', 'red').text('현재 사용 중인 닉네임입니다.');
            return;
        }

        // 서버에 중복 확인 요청
        $.ajax({
            url: 'checkUniqueNickname.do',
            type: 'post',
            data: { nickname: current },
            dataType: 'json',
            success: function(param){
                if(param.result === 'nicknameNotFound'){
                    nicknameChecked = 1;
                    $('#nickname_notice').css('color','#000000').text('사용가능한 닉네임입니다.');
                } else if(param.result === 'nicknameDuplicated'){
                    nicknameChecked = 0;
                    $('#nickname_notice').css('color','red').text('중복된 닉네임입니다.');
                    $('#nickname').val('').focus();
                } else {
                    nicknameChecked = 0;
                    alert('닉네임 중복 체크 오류 발생');
                }
            },
            error: function(){
                nicknameChecked = 0;
                alert('네트워크 오류 발생');
            }
        });
    });
	

	$('#register_photo_form #nickname').keydown(function(){
		nicknameChecked = 0;
		$('#nickname_notice').text('');
	});
	
	// 최종 제출 유효성 검사
    $('#register_photo_form').submit(function(e){
    	e.preventDefault(); // 기본 전송 막기
        const nicknameVal = $('#nickname').val().trim();
        if (nicknameVal === '') {
            alert('닉네임을 입력하세요!');
            $('#nickname').focus();
            return false;
        }
        if (nicknameChecked === 0) {
            alert('닉네임 중복 확인을 해주세요!');
            return false;
        }
        //서버와 통신
		$.ajax({
			url:'updateNickname.do',
			type:'post',
			data: { nickname: nicknameVal },
			dataType:'json',
			success : function(param){
				if(param.result == 'logout'){
					alert('회원가입을 다시 시도해주세요.');
				}else if(param.result == 'success'){
					alert('닉네임이 설정되었습니다.\n로그인 후 이용 바랍니다.');
					location.href = 'logout.do';
				}else{
					alert('닉네임 설정 중 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
        return false; //닉네임 변경 오류 발생시 전송 막기
    });

    // 수정 버튼 누르면 파일 선택 영역 보이기
    $('#photo_btn').click(function(){
    	$('#photo_btn_wrap').hide();
        $('#photo_choice').show();
    });

    // 미리보기 이미지 반영
    $('#photo').on('change', function(event){
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#preview-image').attr('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    });

    $('#photo_submit').click(function(){
        $('#photo_choice').hide();
        $('#photo_btn_wrap').show();
    });
  //이미지 전송
	$('#photo_submit').click(function(){
		
		if($('#photo').val()==''){
			alert('파일을 선택하세요!');
			$('#photo').focus();
			return;
		}
		
		//파일 전송
		const form_data = new FormData();
		/*
		document.getElementById('photo').files[0]
		*/
		form_data.append('photo',$('#photo')[0].files[0]);
		
		//서버와 통신
		$.ajax({
			url:'updateUserPhoto.do',
			type:'post',
			data:form_data,
			dataType:'json',
			contentType:false, 
			processData:false, 
			success : function(param){
				if(param.result == 'logout'){
					alert('회원가입을 다시 시도해주세요.');
				}else if(param.result == 'success'){
					alert('프로필 사진이 수정되었습니다.');
					//수정된 이미지 정보 저장
					photo_path = $('.my-photo').attr('src');
					$('#photo').val('');
					$('#photo_choice').hide();
					$('#photo_btn').show(); 
				}else{
					alert('파일 전송 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
		
	});

    // 취소
    $('#photo_reset').click(function(){
        $('#preview-image').attr('src', defaultImage);
        $('#photo_choice').hide();
        $('#photo_btn_wrap').show();
        $('#photo').val("");
    });
    
    // 버튼 클릭하면 실제 파일 선택 input 클릭 발생시키기
    $('#custom-file-btn').click(function() {
        $('#photo').click();
    });

    // 파일 선택 시 파일 이름 표시
    $('#photo').on('change', function() {
        const fileName = this.files[0] ? this.files[0].name : "선택된 파일 없음";
        $('#file-name').text(fileName);
    });
    
    //다음에 하기 버튼 클릭 시
    $('.secondary-btn').click(function(){
    	alert('회원가입이 끝났습니다. 로그인 후 이용 바랍니다.');
    });
});
</script>

</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main" id="signup-photo-main">
		<h2 id="signup-photo-title">회원가입이 완료되었습니다</h2>
		<h3 id="signup-photo-sub">프로필 사진과 닉네임을 설정해 주세요</h3>
		<form id="register_photo_form" action="registerPhotoUser.do" method="post" enctype="multipart/form-data">
			<ul>
				<li class="photo-preview-wrap">
					<img src="${pageContext.request.contextPath}/images/face.png" class="my-photo" id="preview-image" alt="프로필 이미지">
				</li>
				<li class="photo-edit-area">
    				<div class="align-center" id="photo_btn_wrap">
        				<input type="button" value="수정" id="photo_btn">
    				</div>
    				<div class="align-center" id="photo_choice" style="display:none;">
        				<input type="file" id="photo" name="photo" accept="image/*" style="display: none;">
        				<button type="button" id="custom-file-btn">사진 선택</button>
        				<span id="file-name">선택된 파일 없음</span><br>
        				<input type="button" value="결정" id="photo_submit">
        				<input type="button" value="삭제" id="photo_reset">
    			</div>
				</li>
				<li>
					<div class="form-notice">
						<span id="nickname_notice"></span>
					</div>
					<label for="nickname">닉네임</label>
					<div class="inline-input-group">
						<input type="hidden" id="originalNickname" value="${sessionScope.nickname}">
						<input type="text" name="nickname" id="nickname" maxlength="50" value="${sessionScope.nickname}"
						autocomplete="off" class="input-check" placeholder="2~15자 사이 한글,영어,숫자만 가능" >
						<input type="button" value="중복확인" id="nickname_check">
					</div>
				</li>
			</ul> 
			<div class="align-center">
				<input type="submit" value="완료">
				<input type="button" value="다음에 하기" class="secondary-btn"
					onclick="location.href='${pageContext.request.contextPath}/user/logout.do'">
			</div>                                    
		</form>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>






