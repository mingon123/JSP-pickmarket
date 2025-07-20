<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function() {
	    $('#password_form').submit(function() {
	        const items = document.querySelectorAll('input[type="text"],input[type="password"]');
	        
	        for(let i = 0; i < items.length; i++) {
	            if(items[i].value.trim() == '') {
	                let label = document.querySelector('label[for="'+items[i].id+'"]');
	                alert(label.textContent + ' 입력 필수');
	                items[i].value = '';
	                items[i].focus();
	                return false;
	            }
	
	            if(items[i].id == 'passwd' && !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+{}[\]|\\:;\"'<>,.?/~`]).{8,16}$/.test(items[i].value)) {
	                alert('비밀번호는 영문, 숫자, 특수문자를 포함한 8~16자로 입력하세요.');
	                items[i].value = '';
	                items[i].focus();
	                return false;
	            }
	        }
	
	        if($('#passwd').val() != $('#cpasswd').val()) {
	            alert('새비밀번호와 새비밀번호 확인이 일치하지 않습니다.');
	            $('#passwd').val('').focus();
	            $('#cpasswd').val('');
	            return false;
	        }
	    });
	
	    $('#passwd').keyup(function() {
	        $('#cpasswd').val('');
	        $('#message_cpasswd').text('');
	    });
	
	    $('#cpasswd').keyup(function() {
	        if($('#cpasswd').val() != '' && $('#passwd').val() == $('#cpasswd').val()) {
	            $('#message_cpasswd').text('새비밀번호 일치');
	        } else {
	            $('#message_cpasswd').text('');
	        }
	    });
	});
</script>
</head>
<body>
<div class="page-main2">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main2" id="user-modify-main">
		<h2>비밀번호 수정</h2>
		<form action="modifyPassword.do" method="post" id="password_form">
			<ul>
				<li>
					<label for="id">아이디</label>
					<input type="email" name="id" id="id" maxlength="50" size="16" value="${user.id}" readonly>
				</li>
				<li>
					<label for="origin_passwd">현재 비밀번호</label>
					<input type="password" name="origin_passwd" id="origin_passwd" class="user-modify-input" maxlength="12" autocomplete="off">
				</li>
				<li>
					<label for="passwd">새비밀번호</label>
					<input type="password" name="passwd" id="passwd" class="user-modify-input" maxlength="12" autocomplete="off">
				</li>
				<li>
					<label for="cpasswd">새비밀번호 확인</label>
					<input type="password" id="cpasswd" class="user-modify-input" maxlength="12" autocomplete="off">
					<span id="message_cpasswd"></span>
				</li>
			</ul>
			<div class="user-modify-align-center">
				<input type="submit" value="비밀번호 수정">
				<input type="button" value="마이페이지" onclick="location.href='myPage.do'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>