<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 글 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw_board_modifyform.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		//이벤트 연결
		$('#modify_form').submit(function(){
			if($('#btitle').val().trim()==''){
				alert('제목을 입력하세요');
				$('#btitle').val('').focus();
				return false;
			}
			if($('#bcontent').val().trim()==''){
				alert('내용을 입력하세요');
				$('#bcontent').val('').focus();
				return false;
			}
		});
		
		  const fileInput = document.getElementById('bfilename');
		  const deleteImageInput = document.getElementById('delete_image');
		  const previewContainer = document.querySelector('.img-preview-container');

		  function removePreview() {
		    const previewArea = document.getElementById('preview-area');
		    if (previewArea) {
		        previewArea.innerHTML = ''; 
		      }
		  }

		  // X 버튼 누르면 삭제 처리
		  $(document).on('click', '#delete-button', function() {
		    removePreview();
		    console.log("삭제 요청됨:", $('#delete_image').val());
		    $('#delete_image').val('true');
		    console.log("삭제 요청됨:", $('#delete_image').val());
		    $('#bfilename').val(''); 
		  });

		  fileInput.addEventListener('change', function (e) {
		    const file = e.target.files[0];
		    if (!file || !file.type.startsWith('image/')) return;

		    const reader = new FileReader();
		    reader.onload = function (event) {
		    	removePreview(); // 기존 이미지 제거

		    	const img = document.createElement('img');
		    	img.src = event.target.result;
		   		img.classList.add('preview-img');
		    	img.id = 'preview-image';

		    	const delBtn = document.createElement('button');
		    	delBtn.type = 'button';
		    	delBtn.classList.add('delete-btn');
		    	delBtn.id = 'delete-button';
		    	delBtn.textContent = '×';

		    	delBtn.addEventListener('click', function () {
		    		removePreview();
		    	    deleteImageInput.value = 'true';
		    	    fileInput.value = '';
		    	});

		    	const previewArea = document.getElementById('preview-area');
		    	previewArea.appendChild(img);
		    	previewArea.appendChild(delBtn);

		    	deleteImageInput.value = 'false';
		    };

		    reader.readAsDataURL(file);
		  });
	});
</script>
</head>

<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>'${board.btitle}' 글 수정</h2>
		<form action="boardModify.do" method="post" id="modify_form" enctype="multipart/form-data">
			<input type="hidden" name="board_num" value="${board.board_num}">
			<ul>
				<li>
					<label for ="btitle">제목</label>
					<input type="text" name="btitle" id="btitle" maxlength="20" value="${board.btitle}" class="input-check">
				</li>	
				<li>
					<label for="bcontent">내용</label>
					<textarea rows="5" cols="30" name="bcontent" id="bcontent" class="input-check">${board.bcontent}</textarea>
				</li>
				
				<li class="image-row">
  					<label>이미지</label>
  					<div class="img-preview-container">
    					<input type="file" name="bfilename" id="bfilename" accept="image/*">
    					<input type="hidden" name="original_filename" value="${board.bfilename}">
    					<input type="hidden" name="delete_image" id="delete_image" value="false">
    					
    					<div class="preview-wrapper" id="preview-area" data-status="${not empty board.bfilename ? 'existing' : ''}">
    					<c:if test="${not empty board.bfilename}">
        					<img src="${pageContext.request.contextPath}/upload/${board.bfilename}" alt="기존 이미지" class="preview-img"
        						onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/NoProductImg.png';">
        					<button type="button" id="delete-button" class="delete-btn">×</button>
    					</c:if>
    					</div>
  					</div>
				</li>
				
				<%-- 
				<li>
					<label for="bfilename">이미지</label>
					<input type="file" name="bfilename" id="bfilename" accept="image/gif,image/png,image/jpeg">
				</li>
				--%>
			</ul> 
			<div class="align-center">
				<input type="submit" value="수정">
				<input type="button" value="목록" onclick="location.href='boardList.do'">
			</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>