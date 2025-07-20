<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>중고상품 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<script type="text/javascript">
	window.onload=function(){
		const myForm = document.getElementById('modify_form')		
		//이벤트 연결
		//폼 유효성 검사
		myForm.onsubmit=function(){		
			const items = document.querySelectorAll('.input-check');
			for(let i=0;i<items.length;i++){
				if(items[i].value.trim()==''){
					let label = document.querySelector('label[for="'+items[i].id+'"]');
					alert(label.textContent + ' 입력 필수');
					items[i].value = '';
					items[i].focus();
					return false;
				} //if
			} //for			
		}; //onsubmit
	
		//삭제 이벤트 연결
		delete_btn.onclick=function(){
			let choice = confirm('삭제하시겠습니까?');
			if (choice) {
				location.replace('productDelete.do?product_num=${product.product_num}');
			} //if
		}; //onclick
		
	};
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>중고상품 수정</h2>		
		<form action="productModify.do" method="post" id="modify_form" enctype="multipart/form-data">
			<input type="hidden" name="product_num" value="${product.product_num}">
			<ul>	
				<%-- 거래희망장소 추가요망 --%>			
				<li>
					<label for="title">상품제목</label>
					<input type="text" name="title" id="title" maxlength="10" value="${product.title}" class="input-check">
				</li>
				<li>
					<label for="category_num">카테고리</label>
					<select name="category_num" id="category_num" class="input-check">
				        <option disabled="disabled">카테고리를 선택하세요</option>
				        <%-- 카테고리 선택하면 카테고리 번호로 저장 --%>
				        <c:forEach var="category" items="${categoryList}">
				        	<option value="${category.category_num}" 
				        	<c:if test="${product.category_num == category.category_num}">selected</c:if>>${category.category_name}</option>
				        </c:forEach>   
			        </select>					
				</li>
				<li>
					<label for="nickname">닉네임</label>
					<input type="text" name="nickname" id="nickname" maxlength="10" value="${product.nickname}" readonly>
				</li>
				<li>
					<label for="price">가격</label>
					<%-- 나눔 할 수도 있으므로 가격 0 입력 가능하게 함 --%>
					<input type="number" name="price" id="price" min="0" max="999999999" value="${product.price}" class="input-check">
				</li>	
				<li>
					<label for="content">자세한 설명</label>
																					<%-- 줄바꾸면 안됨. --%>
					<textarea rows="5" cols="30" name="content" id="content" class="input-check">${product.content}</textarea>
				</li>
				
				<%-- 상품사진은 필수 요소 아니라서 class="input-check" 지움. --%>	
				<li>				
				<label>상품사진</label>
				<div class="image-container">				
				    <%-- 기존 등록사진 출력 --%>
				    <c:forEach var="img" items="${productImgList}">
				        <div class="preimage-wrapper" data-image-id="${img.filename}" data-image-type="existing">
				            <img src="${pageContext.request.contextPath}/upload/${img.filename}" class="preview-image" />
				            <button type="button" class="delete-image-btn">&times;</button>
				        </div>
				    </c:forEach>
					<%-- 새 등록사진 --%>
				    <div class="preimage-wrapper upload-wrapper">
				        <label for="imageUpload" class="upload-label">+</label>
				        <input type="file" name="filename" id="imageUpload" class="upload-input" multiple accept="image/gif,image/png,image/jpeg" />
				    </div>
				</div>
				</li>	
				
				<li>
				    <label for="loc">거래희망장소</label>
				    <input type="text" name="loc" id="loc" maxlength="10" class="input-check" readonly>
				    <input type="hidden" name="x_loc" id="x_loc" value="${product.x_loc}">
					<input type="hidden" name="y_loc" id="y_loc" value="${product.y_loc}">
				    <div id="map" style="width: 100%; height: 400px;"></div>
				</li>
				<li>
					<label for="state">판매상태</label>
					<select name="state" id="state" class="input-check">				        
				        <option value="0" <c:if test="${product.state == 0}">selected</c:if>>판매중</option>
				        <option value="1" <c:if test="${product.state == 1}">selected</c:if>>예약중</option>
				        <option value="2" <c:if test="${product.state == 2}">selected</c:if>>판매완료</option>
			        </select>					
				</li>					
			</ul>
			<div class="btn-box align-center">
				<input type="submit" value="수정">
				<c:if test="${user_auth == 9}">
				<input type="button" value="삭제" id="delete_btn">
				</c:if>
				<input type="button" value="목록" onclick="history.go(-1)">
			</div>
		</form>
	</div>
	
<%-- 상품사진 파일 수정 적용 처리 --%>	
<script>
document.addEventListener('DOMContentLoaded', function () {
    const imageContainer = document.querySelector('.image-container');
    const imageInput = document.getElementById('imageUpload');
    const form = document.getElementById('modify_form');
    const maxImages = 5;

    function getCurrentImageCount() {
        return imageContainer.querySelectorAll('.preimage-wrapper:not(.upload-wrapper)').length;
    }

    imageContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('delete-image-btn')) {
            const wrapper = e.target.closest('.preimage-wrapper');
            const type = wrapper.dataset.imageType;

            if (type === 'existing') {
                const imageId = wrapper.dataset.imageId;

                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'deleteImageIds';
                input.value = imageId;
                form.appendChild(input);
            }

            // 새 이미지 삭제 시 input[type=file]도 같이 제거
            if (type === 'new') {
                const index = Array.from(imageContainer.children).indexOf(wrapper);
                const hiddenInputs = imageContainer.querySelectorAll('input[type="file"][name="filename"]');
                if (hiddenInputs[index]) hiddenInputs[index].remove();
            }

            wrapper.remove();
        }
    });

    imageInput.addEventListener('change', function (e) {
        const files = Array.from(e.target.files);
        const currentCount = getCurrentImageCount();
        const availableSlots = maxImages - currentCount;

        if (files.length > availableSlots) {
            alert('이미지는 최대 ' + maxImages + '개까지 업로드할 수 있습니다.');
            imageInput.value = '';
            return;
        }

        for (let file of files) {
            if (!file.type.startsWith('image/')) continue;

            const reader = new FileReader();
            reader.onload = function (event) {
                const wrapper = document.createElement('div');
                wrapper.classList.add('preimage-wrapper');
                wrapper.dataset.imageType = 'new';

                const img = document.createElement('img');
                img.src = event.target.result;
                img.classList.add('preview-image');

                const deleteBtn = document.createElement('button');
                deleteBtn.type = 'button';
                deleteBtn.classList.add('delete-image-btn');
                deleteBtn.innerHTML = '&times;';

                wrapper.appendChild(img);
                wrapper.appendChild(deleteBtn);

                imageContainer.insertBefore(wrapper, document.querySelector('.upload-wrapper'));
            };
            reader.readAsDataURL(file);

            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(file);

            const newInput = document.createElement('input');
            newInput.type = 'file';
            newInput.name = 'filename';
            newInput.files = dataTransfer.files;
            newInput.style.display = 'none';
            imageContainer.appendChild(newInput);
        }

        imageInput.value = '';
    });
});
</script>
	
<!-- 주소 API 시작 -->
<script type="text/javascript">
    var kakaoApiKey = "${kakaoApiKey}";
    var script = document.createElement('script');
    script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=" + kakaoApiKey + "&autoload=false&libraries=services";
    script.onload = function () {
        kakao.maps.load(function () {
            var lat = parseFloat("${product.x_loc}");
            var lng = parseFloat("${product.y_loc}");

            if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
                initMapWithSavedCoordinates(lat, lng);
            } else {
            	// 서울 시청 출력
            	var lat = parseFloat(document.getElementById("x_loc").value) || 37.5665;
            	var lng = parseFloat(document.getElementById("y_loc").value) || 126.9780;

            	initMapWithSavedCoordinates(lat, lng);
            }
        });
    };
    document.head.appendChild(script);
</script>

<script>
	var marker;
	var map;
	
	function initMapWithSavedCoordinates(lat, lng) {
	    var geocoder = new kakao.maps.services.Geocoder();

	    var container = document.getElementById('map');
	    var options = {
	        center: new kakao.maps.LatLng(lat, lng),
	        level: 3
	    };
	    map = new kakao.maps.Map(container, options);

	    marker = new kakao.maps.Marker({
	        position: new kakao.maps.LatLng(lat, lng)
	    });
	    marker.setMap(map);

	    geocoder.coord2Address(lng, lat, function(result, status) {
	        if (status === kakao.maps.services.Status.OK) {
	            var address = result[0].address.address_name;
	            document.getElementById("loc").value = address;
	        }
	    });

	    kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
	        var latLng = mouseEvent.latLng;
	        var newLat = latLng.getLat();
	        var newLng = latLng.getLng();

	        document.getElementById("x_loc").value = newLat;
	        document.getElementById("y_loc").value = newLng;

	        if (marker) {
	            marker.setMap(null);
	        }

	        marker = new kakao.maps.Marker({
	            position: latLng
	        });
	        marker.setMap(map);

	        geocoder.coord2Address(newLng, newLat, function(result, status) {
	            if (status === kakao.maps.services.Status.OK) {
	                var address = result[0].address.address_name;
	                document.getElementById("loc").value = address;
	            }
	        });
	    });
	}
</script>
<!-- 주소 API 끝 -->
	
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>