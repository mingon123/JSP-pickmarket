<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<style type="text/css">
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

.page-main {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-main {
  flex: 1;
}

</style>
<script type="text/javascript">
	$(function(){
		$('#id').focus(); 
		
		$('#login_form').submit(function(){
			if($('#id').val().trim()==''){
				alert('아이디(이메일)를 입력하세요');
				$('#id').val('').focus();
				return false;
			}
			if($('#passwd').val().trim()==''){
				alert('비밀번호를 입력하세요');
				$('#passwd').val('').focus();
				return false;
			}
		});
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main" id="login-content-main">
		<h2 id="login-title">로그인</h2>
		<form action="login.do" id="login_form" method="post">
			<ul>
				<li class="floating-label">
					<input type="text" class="form-input" placeholder="아이디" name="id" 
						id="id" maxlength="50" autocomplete="off">
					<label for="id">아이디</label>
				</li>
				<li class="floating-label">
					<input type="password" class="form-input" placeholder="비밀번호" 
						name="passwd" id="passwd" maxlength="30">
					<label for="passwd">비밀번호</label>
				</li>
			</ul>
			<div id="login-register-link">
            	아직 계정이 없으신가요? <a href="${pageContext.request.contextPath}/user/registerUserForm.do">회원가입하기</a>
          	</div>
			<div id="login-button-group">
				<input type="submit" value="로그인">
				<input type="button" value="비밀번호 찾기"
					onclick="location.href='sendPasswordForm.do'">
			</div>
		</form>
	</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</div>
</body>
</html>
