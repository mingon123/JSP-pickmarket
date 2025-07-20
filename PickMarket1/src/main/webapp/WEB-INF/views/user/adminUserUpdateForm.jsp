<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<script type="text/javascript">
$(function(){
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
	    	$('#nicknameChecked').val('1');
	        alert('사용가능한 닉네임입니다.');
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
	
	$('#modify_form').submit(function(){
	    if ($('#nicknameChecked').val() !== '1') {
	        alert('닉네임 중복 확인을 해주세요.');
	        $('#nickname').focus();
	        return false;
	    }
	
	    const passwd = $('#passwd').val().trim();
	    const passwd_check = $('#passwd_check').val().trim();
	
	    // 비밀번호 입력한 경우에만 체크
	    if (passwd && passwd !== passwd_check) {
	        alert('비밀번호 확인이 일치하지 않습니다.');
	        $('#passwd_check').focus();
	        return false;
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

	// 폼 제출 시 지역 선택 확인
	$("#modify_form").on("submit", function() {
	    if ($("#region").val() === "") {
	        alert("정확한 지역명을 입력하세요.");
	        $("#region_view").focus();
	        return false; 
	    }
	});

});
</script>
</head>
<body>
<div id="admin-edit" class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>${target.id} 회원수정</h2>
		<form id="modify_form" action="adminUserUpdate.do" method="post" enctype ="multipart/form-data">
			<ul>
				<li>
					<input type="hidden" name="user_num" id="user_num" value="${target.user_num}">
				</li>
				<li>
					<label for="auth">등급</label>
					<input type="radio" name="auth" id="auth0" value="0" <c:if test="${target.auth == 0}">checked</c:if>>탈퇴
					<input type="radio" name="auth" id="auth1" value="1" <c:if test="${target.auth == 1}">checked</c:if>>정지
					<input type="radio" name="auth" id="auth2" value="2" <c:if test="${target.auth == 2}">checked</c:if>>일반
					<input type="radio" name="auth" id="auth9" value="9" <c:if test="${target.auth == 9}">checked</c:if>>관리자
				</li>
				<li>
					<label for="name">이름</label>
					<input type="text" name="name" id="name" value="${target.name}"
							maxlength="10" class="input-check" autocomplete="off" >
				</li>
				<li>
					<input type="hidden" id="nicknameChecked" name="nicknameChecked" value="0">
					<label for="nickname">닉네임</label>
					<input type="hidden" id="originalNickname" value="${target.nickname}">
					<input type="text" name="nickname" id="nickname" value="${target.nickname}"
							maxlength="10" class="input-check" autocomplete="off" >
					<input type="button" value="중복확인" id="nickname_check" class="simple-button">
				</li>
				<li>
					<label for="passwd">비밀번호</label>
					<input type="password" name="passwd" id="passwd" 
							maxlength="30" class="input-check" placeholder="변경 시 입력">
				</li>
				<li>
					<label for="passwd_check">비밀번호 확인</label>
					<input type="password" name="passwd_check" id="passwd_check" 
							maxlength="30" class="input-check" placeholder="변경 시 입력">
				</li>
				<li>
					<label for="report_count">신고 횟수</label>
					<input type="number" name="report_count" id="report_count" value="${target.report_count}"
					             maxlength="5" class="input-check" autocomplete="off" min="0" max="3">
				</li>
				<li>
					<label for="phone">전화번호</label>
					<input type="text" name="phone" id="phone" value="${target.phone}"
					             maxlength="15" class="input-check" autocomplete="off" >
				</li>
				<li>
					<label for="score">점수</label>
					<input type="number" name="score" id="score" value="${target.score}"
					             maxlength="10" class="input-check" autocomplete="off" min="0">
				</li>
				<li>
					<label for="region_view">지역</label>
					<input type="text" id="region_view" value="${region_nm}" maxlength="60" class="input-check" autocomplete="off">
					<input type="hidden" name="region" id="region" value="${target.region_cd}">
				</li>
				<li>
					<label for="photo">사진</label>
					<c:if test="${not empty target.photo}">
						<img src="${pageContext.request.contextPath}/upload/${target.photo}" width="80" height="80"
								onerror="this.src='${pageContext.request.contextPath}/images/face.png'" id="preview-image">
					</c:if>
					<c:if test="${empty target.photo}">
						<img src="${pageContext.request.contextPath}/images/face.png" width="80" height="80" id="preview-image">
					</c:if>
					<input type="file" id="photo" name="photo" accept="image/*">
				</li>
			</ul> 
			<div class="btn-box align-center">
				<input type="submit" value="수정" class="simple-button">
				<input type="button" value="목록" onclick="location.href='adminUserList.do'" class="simple-button">
			</div>                                    
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
</body>
</html>
