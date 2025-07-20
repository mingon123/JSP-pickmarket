<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카테고리 등록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		//카테고리 중복체크	
		let categoryChecked = 0;				
		$('#category_check').click(function(){		
			if($('#category_name').val().trim() == ''){
				alert('카테고리명을 입력하세요');
				$('#category_name').val('').focus();
				return;				
			}//if
			//서버와 통신
			$.ajax({
				url:'checkUniqueInfo.do',
				type:'post',
				data:{category_name:$('#category_name').val()},
				dataType:'json',
				success:function(param){
					if (param.result == 'categoryNotFound') {
						categoryChecked = 1;
						$('#message_category').css('color','#000000').text('등록 가능');
					}else if (param.result == 'categoryDuplicated') {
						categoryChecked = 0;
						$('#message_category').css('color','red').text('중복');
						$('#category_name').val('').focus();
					}else {
						categoryChecked = 0;
						alert('카테고리 중복 체크 오류 발생');
					}
				},
				error:function(){
					categoryChecked = 0;
					alert('네트워크 오류 발생');					
				} 
			}); //ajax
		}); //click
		//카테고리 중복 안내 메시지 초기화 및 카테고리 중복 값 초기화
		$('#write_form #category_name').keydown(function(){
			isChecked = 0;
			$('#message_category').text('');
		}); //keydown
		
		//입력여부 체크
		$('#write_form').submit(function(){
			const items = document.querySelectorAll('.input-check');
			for(let i=0;i<items.length;i++){
				if(items[i].value.trim()==''){
					const label = document.querySelector('label[for="'+items[i].id+'"]');
					alert(label.textContent + '입력 필수');
					items[i].value='';
					items[i].focus();
					return false;					
				} //if
				if (items[i].id == 'category_name' && categoryChecked==0) {
					alert('카테고리명 중복체크 필수');
					return false;
				} //if
			}//for
		}); //submit
		
	});
	
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>카테고리 등록</h2>
		<form action="categoryWrite.do" method="post" id="write_form" enctype="multipart/form-data">
			<ul>
				<li>
					<label for="category_name">카테고리명</label>
					<input type="text" name="category_name" id="category_name" maxlength="15" class="input-check">
					<input type="button" value="중복체크" id="category_check">
					<span id="message_category"></span>
				</li>	
				<%-- 
				TODO: 카테고리상태 default 1(활성)인데 넣을 필요 있음? 
				<li>
					<label for="category_status">활성 여부</label>
					<input type="radio" name="category_status" value="1" id="status1">활성
					<input type="radio" name="category_status" value="2" id="status2">비활성
				</li>
				--%>
			</ul>
			<div class="btn-box align-center">
				<input type="submit" value="등록">
				<input type="button" value="목록" onclick="location.href='categoryList.do'">
			</div>
		</form>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>