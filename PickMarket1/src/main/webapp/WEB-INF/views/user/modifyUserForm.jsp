<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#modify_form').submit(function(){
			const items = document.querySelectorAll('.input-check');
			for(let i=0;i<items.length;i++){
				if(items[i].value.trim()==''){
					const label = document.querySelector('label[for="'+items[i].id+'"]');
					alert(label.textContent + ' 입력 필수');
					items[i].value='';
					items[i].focus();
					return false;
				}
			}
		});
	});
</script>
</head>
<body>
<div class="page-main2">
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <div class="content-main2" id="user-modify-main">
        <h2>회원정보 수정</h2>
        <form id="modify_form" action="modifyUser.do" method="post">
            <input type="hidden" name="user_num" value="${user.user_num}">
            <ul>
                <li>           
                    <label for="id">이메일(ID)</label>
                    <input type="email" name="id" id="id" maxlength="50" value="${user.id}" readonly>
                </li>
                <li>
                    <label for="name">이름</label>
                    <input type="text" name="name" id="name" maxlength="10" class="user-modify-input" value="${user.name}" autocomplete="off">
                </li>
                <li>
                    <label for="nickname">닉네임</label>
                    <input type="text" name="nickname" id="nickname" maxlength="30" class="user-modify-input" value="${user.nickname}" autocomplete="off">
                </li>
                <li>
                    <label for="phone">전화번호</label>
                    <input type="text" name="phone" id="phone" maxlength="15" class="user-modify-input" value="${fn:substring(user.phone,0,3)}-${fn:substring(user.phone,3,7)}-${fn:substring(user.phone,7,11)}" autocomplete="off"><!-- class 변경 -->
                </li>
				<li>
			        <label for="address">주소</label>
			        <div class="address-input-group">
			           <input type="text" id="address" name="address" class="input-check" readonly>
			           <input type="hidden" id="region_cd" name="region_cd">
			           <input type="button" value="현재 위치로 지역 입력" onclick="getCurrentLocation()" class="location-btn">
			        </div>
				</li>
            </ul>
            <div class="user-modify-align-center">
                <input type="submit" value="수정하기">
                <input type="button" value="마이페이지" onclick="location.href='myPage.do'">
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
    function getCurrentLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var lat = position.coords.latitude;  // 위도
                var lon = position.coords.longitude; // 경도

                console.log("현재 위치 - 위도: " + lat + ", 경도: " + lon);

                // Kakao Geocoder 사용하여 주소 변환
                var geocoder = new kakao.maps.services.Geocoder();
                var locPosition = new kakao.maps.LatLng(lat, lon);

                // 좌표로 주소를 변환
                geocoder.coord2RegionCode(locPosition.getLng(), locPosition.getLat(), function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var region_nm = result[0].region_1depth_name + ' ' +
                        				result[0].region_2depth_name + ' ' +
                        				result[0].region_3depth_name;

                        document.getElementById("address").value = region_nm;
                        
                        $.ajax({
                            url: '${pageContext.request.contextPath}/location/getRegionCd.do',
                            type: 'GET',
                            data: { region_nm: region_nm },
                            dataType: 'json',
                            success:function(param){
            					if(param.result == 'success'){
            						console.log("받아온 region_cd:", param);
                                    $("#region_cd").val(param.region_cd);
            					}else if(param.result == 'region_nmIsNull'){
            						alert('지역명 없음');
            					}else if(param.result == 'region_cdNotFound'){
            						alert('지역코드 없음');
            					}else{
            						alert('지역 에러')
            					}
            				},
                            error: function() {
                                alert('네트워크 오류 발생');
                            }
                        });
                    } else {
                    	alert('주소 변환 실패');
                    }
                });
            }, function(error) {
            	alert('위치 정보 오류', error)
            });
        } else {
            alert("이 브라우저에서는 위치 정보를 사용할 수 없습니다.");
        }
    }
</script>
<!-- 주소 API 끝 -->
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>