<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원등록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script type="text/javascript">
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
					$('#idChecked').val('1');
					alert('등록 가능합니다.');
					$('#auth_get').removeAttr('disabled');
				}else if(param.result == 'idDuplicated'){
					$('#idChecked').val('0');
					alert('중복된 이메일입니다.');
					$('#id').val('').focus();
				}else{
					$('#idChecked').val('0');
					alert('이메일 중복 체크 오류 발생');
				}
			},
			error:function(){
				$('#idChecked').val('0');
				alert('네트워크 오류 발생');
			}
		});
	});
	
	// 닉네임 중복 확인
	$('#nickname_check').click(function(){
    	const current = $('#nickname').val().trim();
    	const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,15}$/;
    
    	if (!nicknameRegex.test(current)) {
        	alert('닉네임은 한글, 영문, 숫자만 가능하며 2~15자 사이여야 합니다.');
        	$('#nickname').focus();
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
            		$('#nicknameChecked').val('1');
                	alert('사용가능한 닉네임입니다.');
            	} else if(param.result === 'nicknameDuplicated'){
                	$('#nicknameChecked').val('0');
                	alert('중복된 닉네임입니다.');
                	$('#nickname').val('').focus();
            	} else {
            	 	$('#nicknameChecked').val('0');
                	alert('닉네임 중복 체크 오류 발생');
            	}
        	},
        	error: function(){
        		$('#nicknameChecked').val('0');
            	alert('네트워크 오류 발생');
        	}
    	});
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

	// 파일 선택 시 파일 이름 표시
	$('#photo').on('change', function() {
    	const fileName = this.files[0] ? this.files[0].name : "선택된 파일 없음";
    	$('#file-name').text(fileName);
	});

	$('#register_form').submit(function(){
		if ($('input[name="auth"]:checked').length == 0) {
		    alert('등급을 선택해주세요.');
		    return false;
		}
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
			if(items[i].id == 'id' && ($('#idChecked').val() !== '1')){
				alert('아이디(이메일) 중복체크 필수');
				return false;
			}
			const current = $('#nickname').val().trim();
	        const nicknameRegex = /^[a-zA-Z0-9가-힣]{2,15}$/;
			if (!nicknameRegex.test(current)) {
	            alert('닉네임은 한글, 영문, 숫자만 가능하며 2~15자 사이여야 합니다.');
	            $('#nickname').focus();
	            return false;
	        }
			if(items[i].id == 'nickname' && ($('#nicknameChecked').val() !== '1')){
				alert('닉네임 중복체크 필수');
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
			if(items[i].id === 'phone' && !/^01[016789]-?\d{3,4}-?\d{4}$/.test($('#phone').val())) {
				alert('전화번호 형식이 올바르지 않습니다.');
				$('#phone').focus();
				return false;
			}
		    if ($("#region").val() === "") {
		        alert("정확한 지역명을 입력하세요.");
		        $("#region_view").focus();
		        return false; 
		    }
		}
    	
		return true;
	});

	// 지역 리스트
	var locationList = [
	    <c:forEach var="loc" items="${locationList}" varStatus="loop">
	        "${loc.region_nm}"<c:if test="${!loop.last}">,</c:if>
	    </c:forEach>
	];
	// 지역 이름 → 코드 매핑용 Map 객체
	var regionMap = {
	    <c:forEach var="loc" items="${locationList}" varStatus="loop">
	        "${loc.region_nm}": "${loc.region_cd}"<c:if test="${!loop.last}">,</c:if>
	    </c:forEach>
	};

	// 자동완성 설정 - 최대 10개만 표시
	$("#region_view").autocomplete({
	    source: function(request, response) {
	        const term = $.trim(request.term).toLowerCase();
	        const filtered = locationList.filter(loc => loc.toLowerCase().includes(term));
	        response(filtered.slice(0, 10));
	    },
	    select: function(event, ui) {
	        $("#region_view").val(ui.item.value);
	        $("#region").val(regionMap[ui.item.value]);
	        return false;
	    }
	});

	$("#region_view").on("click", function() {
	    $(this).autocomplete("search", "");
	});
	
	// 수동 입력 혹은 포커스 아웃 시 유효성 검사
	$("#region_view").on("change blur", function() {
	    const inputVal = $(this).val();
	    if (regionMap.hasOwnProperty(inputVal)) {
	        $("#region").val(regionMap[inputVal]);
	    } else {
	        $("#region").val("");
	    }
	});

	
});
</script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>회원등록</h2>
		<form id="register_form" action="adminUserRegister.do" method="post" enctype ="multipart/form-data">
			<ul>
				<li>
					<label for="auth">등급</label>
					<input type="radio" name="auth" id="auth0" value="0">탈퇴
					<input type="radio" name="auth" id="auth1" value="1">정지
					<input type="radio" name="auth" id="auth2" value="2">일반
					<input type="radio" name="auth" id="auth9" value="9">관리자
				</li>
				<li>
					<input type="hidden" id="idChecked" name="idChecked" value="0">
					<label for="id">이메일(ID)</label>
					<input type="text" name="id" id="id" maxlength="40" class="input-check" autocomplete="off" >
					<input type="button" value="중복확인" id="id_check" class="simple-button">
				</li>
				<li>
					<label for="name">이름</label>
					<input type="text" name="name" id="name" maxlength="10" class="input-check" autocomplete="off" >
				</li>
				<li>
					<input type="hidden" id="nicknameChecked" name="nicknameChecked" value="0">
					<label for="nickname">닉네임</label>
					<input type="text" name="nickname" id="nickname" maxlength="20" class="input-check" autocomplete="off" >
					<input type="button" value="중복확인" id="nickname_check" class="simple-button">
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
					<input type="text" name="phone" id="phone" maxlength="15" class="input-check" autocomplete="off" >
				</li>
				<li>
					<label for="region_view">주소</label>
					<input type="text" id="region_view" value="${region_nm}" maxlength="60" class="input-check" autocomplete="off">
					<input type="hidden" name="region" id="region" value="${target.region_cd}">
				</li>
				
				<li>
					<label for="photo">사진</label>
					<img src="${pageContext.request.contextPath}/images/face.png" width="80" height="80" id="preview-image">
					<input type="file" id="photo" name="photo" accept="image/*">
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="등록" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminUserList.do'" class="simple-button">
			</div>                                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
</body>
</html>
