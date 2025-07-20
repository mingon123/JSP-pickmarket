<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>중고상품 등록 폼</title><%-- 관리자/사용자 공통 사용여부 재확인요망 --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<script type="text/javascript">
	window.onload=function(){				
		//거래희망장소 관련
		var regionName = "${region}";
	    getCoordinatesByRegion(regionName);
		
		const myForm = document.getElementById('write_form')
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
			
			//카테고리 미선택 제출 방지
			const select = document.getElementById('category_num');
		    //선택된 option 가져오기
		    const selectedIndex = select.selectedIndex;
		    const selectedOption = select.options[selectedIndex];
		    if (selectedOption.disabled) {
		      alert("카테고리를 선택해주세요.");
		      return false; // 폼 제출 막기
		    } //if			
		    
		    //거래희망장소 미선택 제출 방지
		    if (document.getElementById('x_loc').value.trim() === '' || 
			   document.getElementById('y_loc').value.trim() === '') {
			   alert("거래희망장소를 지도에서 선택해주세요.");
			   return false;
		    }
		}; //onsubmit
		
		// 이미지 처리
		const imageInput = document.getElementById('imageUpload');
		const imageContainer = document.querySelector('.image-container');
		const maxImages = 5;

		function getCurrentImageCount() {
			return imageContainer.querySelectorAll('.preimage-wrapper:not(.upload-wrapper)').length;
		}

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

					const dataTransfer = new DataTransfer();
					dataTransfer.items.add(file);

					const newInput = document.createElement('input');
					newInput.type = 'file';
					newInput.name = 'filename';
					newInput.files = dataTransfer.files;
					newInput.style.display = 'none';

					// wrapper에 이미지, 버튼, input 모두 삽입
					wrapper.appendChild(img);
					wrapper.appendChild(deleteBtn);
					wrapper.appendChild(newInput);

					imageContainer.insertBefore(wrapper, document.querySelector('.upload-wrapper'));
				};
				reader.readAsDataURL(file);
			}

			imageInput.value = '';
		});

		// 이미지 삭제
		imageContainer.addEventListener('click', function (e) {
			if (e.target.classList.contains('delete-image-btn')) {
				const wrapper = e.target.closest('.preimage-wrapper');
				const type = wrapper.dataset.imageType;

				if (type === 'new') {
					const hiddenInput = wrapper.querySelector('input[type="file"][name="filename"]');
					if (hiddenInput) hiddenInput.remove();
				}

				wrapper.remove();
			}
		});	
		
	};
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2>중고상품 등록</h2>
		<form action="productWrite.do" method="post" id="write_form" enctype="multipart/form-data">
			<ul>	
				<%-- TODO 거래희망장소 좌표 입력받기 --%>			
				<li>
					<label for="title">상품제목</label>
					<input type="text" name="title" id="title" maxlength="10" class="input-check">
				</li>
				<li>
					<label for="category_num">카테고리</label>
					<select name="category_num" id="category_num" class="input-check">
				        <option disabled="disabled" selected>카테고리를 선택하세요</option>
				        <%-- 카테고리 선택하면 카테고리 번호로 저장. 활성화된 카테고리만 선택 가능 --%>
				        <c:forEach var="category" items="${categoryList}">
				        	<c:if test="${category.category_status == 1}">
				        	<option value="${category.category_num}">${category.category_name}</option>
				        	</c:if>
				        </c:forEach>       
			        </select>					
				</li>
				<li>
					<label for="price">가격</label>
					<%-- 나눔 할 수도 있으므로 가격 0 입력 가능하게 함 --%>
					<input type="number" name="price" id="price" min="0" max="999999999" class="input-check">
				</li>	
				<li>
					<label for="content">자세한 설명</label>
																					<%-- 줄바꾸면 안됨. --%>
					<textarea rows="5" cols="30" name="content" id="content" class="input-check"></textarea>
				</li>
				<%-- 상품사진은 필수 입력 아님 --%>	
				<li>
					<label for="filename">상품사진</label>
					<div class="image-container">
						<div class="preimage-wrapper upload-wrapper">
							<label for="imageUpload" class="upload-label">+</label>
							<input type="file" name="filename" id="imageUpload" class="upload-input" multiple accept="image/gif,image/png,image/jpeg" />
						</div>
					</div>
				</li>			
				<li>
				    <label for="loc">거래희망장소</label>
				    <input type="text" name="loc" id="loc" maxlength="10" class="input-check" readonly>
				    <input type="hidden" name="x_loc" id="x_loc">
    				<input type="hidden" name="y_loc" id="y_loc">
				    <div id="map" style="width: 100%; height: 400px;"></div>
				</li>
			</ul>
			<div class="btn-box align-center">
				<input type="submit" value="등록" class="simple-button">
				<input type="button" value="목록" class="simple-button" onclick="history.go(-1)">
			</div>
		</form>
	</div>
	
<!-- 주소 API 시작 -->
<script type="text/javascript">
    var kakaoApiKey = "${kakaoApiKey}";
    var script = document.createElement('script');
    script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=" + kakaoApiKey + "&autoload=false&libraries=services";
    script.onload = function () {
        kakao.maps.load(function () {
        	
        });
    };
    document.head.appendChild(script);
</script>

<script>
	var marker;
	var map;
	
	function getCoordinatesByRegion(regionName) {
	    var geocoder = new kakao.maps.services.Geocoder();
	
	    // 지역명을 좌표로 변환
	    geocoder.addressSearch(regionName, function(result, status) {
	        if (status === kakao.maps.services.Status.OK) {
	            var lat = result[0].y;  // 위도
	            var lng = result[0].x;  // 경도

	            var fullAddress = result[0].address_name;
	            document.getElementById("loc").value = fullAddress;
	            	
	            var container = document.getElementById('map');
	            var options = {
	                center: new kakao.maps.LatLng(lat, lng),
	                level: 3
	            };
	            map = new kakao.maps.Map(container, options);
	
	            kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
	                var latLng = mouseEvent.latLng;
	
	                var lat = latLng.getLat();
	                var lng = latLng.getLng();
	
	                // 위도와 경도를 각각 input에 전달
	                document.getElementById("x_loc").value = lat;  // 위도
	                document.getElementById("y_loc").value = lng;  // 경도
	
	                if (marker) {
	                    marker.setMap(null);
	                }
	
	                marker = new kakao.maps.Marker({
	                    position: latLng
	                });
	                marker.setMap(map);
	                
	                geocoder.coord2Address(lng, lat, function(result, status) {
                        if (status === kakao.maps.services.Status.OK) {
                            var address = result[0].address.address_name;
                            document.getElementById("loc").value = address;
                        }
                    });
	            });
	        } else {
	            alert('주소 변환 실패');
	        }
	    });
	}
</script>
<!-- 주소 API 끝 -->
	
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>