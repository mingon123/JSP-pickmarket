<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#delete_form').submit(function(){
			const items = document.querySelectorAll('input[type="text"],input[type="password"]');
			for(let i=0;i<items.length;i++){
				if(items[i].value.trim()==''){
					let label = document.querySelector('label[for="'+items[i].id+'"]');
					alert(label.textContent + ' 입력 필수');
					items[i].value = '';
					items[i].focus();
					return false;
				} // end of if
			} // end of for

			if($('#passwd').val()!=$('#cpasswd').val()){
				alert('비밀번호와 비밀번호 확인이 불일치');
				$('#passwd').val('').focus();
				$('#cpasswd').val('');
				return false;
			} // end of if
		}); // end of submit
		
		// 비밀번호 확인까지 한 후 비밀번호를 수정하려고 하면 비밀번호 확인을 초기화
		$('#passwd').keyup(function(){
			$('#cpasswd').val('');
			$('#message_cpasswd').text('');
		});
		
		// 비밀번호와 비밀번호 확인 일치 여부 체크
		$('#cpasswd').keyup(function(){
			if($('#cpasswd').val()!='' && $('#passwd').val()==$('#cpasswd').val()){
				$('#message_cpasswd').text('비밀번호 일치');
			}else{
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
		<h2>회원탈퇴</h2>
		<form action="deleteUser.do" method="post" id="delete_form">
			<ul>
				<li>
					<label for="id">아이디</label>
					<input type="text" name="id" id="id" maxlength="50" autocomplete="off">
				</li>
				<li>
					<label for="name">이름</label>
					<input type="text" name="name" id="name" maxlength="30" autocomplete="off">
				</li>
				<li>
					<label for="passwd">비밀번호</label>
					<input type="password" name="passwd" id="passwd" class="user-modify-input" maxlength="12" autocomplete="off">
				</li>
				<li>
					<label for="cpasswd">비밀번호 확인</label>
					<input type="password" id="cpasswd" class="user-modify-input" maxlength="12" autocomplete="off">
					<span id="message_cpasswd"></span>
				</li>
			</ul>
			<div class="user-modify-align-center">
				<input type="submit" value="회원탈퇴">
				<input type="button" value="마이페이지" onclick="location.href='myPage.do'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>