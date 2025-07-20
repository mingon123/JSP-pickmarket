<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 목록</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_searchword.css">
<script type="text/javascript">
    // 서버에서 받은 지역 목록
    var locationList = [
        <c:forEach var="loc" items="${locationList}" varStatus="loop">
            "${loc.region_nm}"<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];

    $(function () {
    	$('#search_form').submit(function () {
    	    if ($('#keyword').val().trim() == "") {
    	        alert('검색어를 입력하세요!');
    	        $('#keyword').val('').focus();
    	        return false;
    	    }
    	    let regionVal = $('#region').val();
    	    if (regionVal === '전체지역') {
    	        $('#region').val('ALL');
    	    }
    	    $('#search-region').val(regionVal);
    	});
    	
        // 지역 선택 모달 열기
        $('#openLocationModal').click(function () {
            $('#locationModal').show();
            $('#search-area').val('');
            $('#autocomplete-suggestions').empty().hide();
            $('#recommend-wrapper').show();
        });

        // 모달 닫기
        $('#closeLocationModal').click(function () {
        	$('#locationModal').hide();
        	$('#search-area').val('');
        	$('#autocomplete-suggestions').empty().hide();
        	$('#recommend-wrapper').show();
        });

        // 모달 외부 클릭 시 닫기
        $('#locationModal').on('click', function (e) {
            if (!$(e.target).closest('.modal-content').length) {
                $('#locationModal').hide();
            	$('#search-area').val('');
            	$('#autocomplete-suggestions').empty().hide();
            	$('#recommend-wrapper').show();
            }
        });

        // 자동완성 검색
        $('#search-area').on('input', function () {
            var userInput = $(this).val().trim();
            const suggestionsContainer = $('#autocomplete-suggestions');
            const recommendWrapper = $('#recommend-wrapper');

            suggestionsContainer.empty();
            recommendWrapper.hide();

            if (userInput.length > 0) {
                var filteredLocations = locationList.filter(function (location) {
                    return location.toLowerCase().includes(userInput.toLowerCase());
                });

                if (filteredLocations.length > 0) {
                    filteredLocations.forEach(function (location) {
                        var suggestionItem = $('<div>')
                            .addClass('autocomplete-suggestion')
                            .text(location)
                            .on('click', function () {
                                $('#search-area').val(location);
                                suggestionsContainer.empty().hide();
                                recommendWrapper.show();
                                $('#region').val(location);
                                $('#selected-location').text(location.split(' ').pop());
                                $('#locationModal').hide();
                                fetchFilteredProducts(location);
                            });
                        suggestionsContainer.append(suggestionItem);
                    });
                } else {
                    suggestionsContainer.append(
                        $('<div>').addClass('autocomplete-suggestion').text('결과 없음')
                    );
                }
                suggestionsContainer.show();
            } else {
                suggestionsContainer.empty().hide();
                recommendWrapper.show();
            }
        });

        // 추천 지역 클릭
		$('.recommend-list li').click(function () {
		    var selectedLocation = $(this).text().trim();
		    
		    if (selectedLocation === "전체지역") {
		        $('#search-area').val(selectedLocation);
		        $('#region').val('ALL');  // 명확히 ALL 넘기기
		        $('#selected-location').text('전체지역');
		        $('#locationModal').hide();
		        fetchFilteredProducts('ALL');
		    } else {
		        $('#search-area').val(selectedLocation);
		        $('#region').val(selectedLocation);
		        $('#selected-location').text(selectedLocation.split(' ').pop());
		        $('#locationModal').hide();
		        fetchFilteredProducts(selectedLocation);
		    }
		});

        // 현재 위치 사용 버튼 클릭
		$('#currentLocation').click(function () {
		    var fullRegion = "${user.locationVO.region_nm}";
		    var parts = fullRegion.split(" ");
		    var userRegion = parts.length >= 3 ? parts[2] : parts[parts.length - 1];
		    $('#selected-location').text(userRegion);
		    $('#region').val(fullRegion);
		    $('#locationModal').hide();
		    
		    fetchFilteredProducts(fullRegion);
		});

     	// 전체지역 선택 시
		function changeRegion(region) {
		    let currentUrl = new URL(window.location.href);
		    if (region === 'ALL') {
		        currentUrl.searchParams.set('region_nm', 'ALL');
		    } else {
		        currentUrl.searchParams.set('region_nm', region);
		    }
		    window.location.href = currentUrl.toString();
		}
     	
		$(function () {
		    $('#recommend-list li').on('click', function () {
		        const selectedRegion = $(this).data('region');
		        if (selectedRegion) {
		            changeRegion(selectedRegion);
		        }
		    });
		});
     	
        // AJAX로 필터링된 상품 리스트 가져오기
		function fetchFilteredProducts(region) {
		    $.ajax({
		        url: 'userProductList.do',
		        type: 'GET',
		        data: {
		            region_nm: region,
		            keyfield: $('#search_form select[name="keyfield"]').val() || '',
		            keyword: $('#search_form input[name="keyword"]').val() || '',
		            pageNum: 1
		        },
		        success: function (response) {
		            let parsedHTML = $('<div>').html(response);
		            let productListHtml = parsedHTML.find('.product-space').html();
		            let paginationHtml = parsedHTML.find('.pagination').html();
		            let count = parsedHTML.find('.product-space').children().length;
		
		            if (region === 'ALL') {
		                $('#regionName').text('전체지역 중고거래');
		            } else {
		                $('#regionName').text(region + " 중고거래");
		            }
		
		            if (count == 0 || !productListHtml) {
		                $('.product-space').html('<div class="result-display">표시할 상품이 없습니다.</div>');
		                $('.pagination').html('');
		            } else {
		                $('.product-space').html(productListHtml);
		                $('.pagination').html(paginationHtml);
		            }
		        },
		        error: function (xhr, status, error) {
		            console.error("상품 목록 가져오기 실패:", error);
		        }
		    });
		}
        
        <%-- 최근검색어 관련 js 시작 --%>
        let logoutAlertShown = false;
        
    	 // 검색창 포커스 시 AJAX로 최근 검색어 가져오기
        $('#keyword').on('focus', function(){
            const keyfield = $('select[name="keyfield"]').val();

            $.ajax({
                url: '${pageContext.request.contextPath}/searchword/searchWordList.do',
                type: 'get',
                data: { keyfield: keyfield },
                dataType: 'json',
                success: function(param){
                	if(param.result == 'logout') {
                        if (!logoutAlertShown) {
                            alert('로그인 후 사용하세요!');
                            logoutAlertShown = true;
                        }
     	            }else if(param.result === 'success'){
                        const list = param.list;
                        const $listContainer = $('#recent-keyword-list');
                        $listContainer.empty();
                        if (list.length === 0) {
                            $listContainer.append('<li class="no-keyword">최근 검색어가 없습니다.</li>');
                        } else {
                            list.forEach(function(item){
                            	 const li = $('<li></li>');
                            	 const icon = $('<i class="fa-solid fa-magnifying-glass recent-icon"></i>');
                            	 const span = $('<span class="word"></span>').text(item.s_word); 
                            	 const btn = $('<button class="delete-keyword">×</button>').attr('data-num', item.s_num);
                            	 li.append(icon).append(span).append(btn);
                            	 $listContainer.append(li);
                            });
                        }
                        $('#recent-keyword-box').show();
                        $('#recent-keywords-box').css({
                        	  display: 'block',
                        	  opacity: 1,
                        	  visibility: 'visible'
                        	});
                    }
                },
    	        error: function () {
    	            alert('네트워크 오류 발생');
    	        }
            });
        });

        // 검색창 또는 최근검색어 박스 외 클릭 시 숨기기
        $(document).on('click', function(e){
            if (!$(e.target).closest('#keyword, #recent-keyword-box').length){
                $('#recent-keyword-box').hide();
                $('#recent-keywords-box').css({
                	  display: 'none',
                	  opacity: 0,
                	  visibility: 'hidden'
                	});
            }
        });

        // 검색어 삭제
        $(document).on('click', '.delete-keyword', function(e){
        	e.preventDefault();
            e.stopPropagation(); // 부모 클릭 막기
            const s_num = $(this).data('num');

            $.ajax({
                url: '${pageContext.request.contextPath}/searchword/deleteSearchWord.do',
                type: 'post',
                data: { s_num: s_num },
                dataType: 'json',
                success: function(param){
                	if(param.result == 'logout') {
     	                alert('로그인 후 사용하세요!');
     	            }else if(param.result == 'success'){
     	            	$('#keyword').trigger('focus');
                    }
                },
    	        error: function () {
    	            alert('네트워크 오류 발생');
    	        }
            });
        });
        
        $('#search_form').on('submit', function(e) {
        	e.preventDefault();
        	
            const keyword = $('#keyword').val().trim();
            const keyfield = $('select[name="keyfield"]').val();

            if (keyword !== '') {
                $.ajax({
                    url: '${pageContext.request.contextPath}/searchword/writeSearchWord.do',
                    type: 'post',
                    data: {
                        word: keyword,
                        keyfield: keyfield
                    },
                    dataType: 'json',
                    async: false, //동기 처리해야 검색 전에 저장 완료됨
                    success: function(param){
                    	if(param.result == 'logout') {
         	                alert('로그인 후 사용하세요!');
         	            }else if(param.result == 'success'){
         	            	$('#search_form')[0].submit();
                        }
                    },
        	        error: function () {
        	            alert('네트워크 오류 발생');
        	        }
                });
            }
        });

     	// 최근 검색어 클릭 시 검색 실행
        $(document).on('click', '.word', function(e){
            const selectedWord = $(this).text();
            $('#keyword').val(selectedWord);
            $('#search_form').submit();
        });

        <%-- 최근검색어 관련 js 끝 --%>
        
        //판매중만 보기 필터링
        $('#filter-sale').change(function() {
        	const isChecked = $(this).is(':checked');
            
            $('.horizontal-area').each(function() {
            	const state = $(this).data('state');
            	if (isChecked) {
                	if (state === 0 || state === "0") {
                  		$(this).show();
                	} else {
                  		$(this).hide();
                	}
              	} else {
                	// 전체 다시 보여주기
                	$(this).show();
              	}
            });
         });
        
    });
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="content-main">
		<h2 id="page-title">중고상품 목록</h2>
		<!-- 지역처리 -->
		<div class="search-container">
		    <button type="button" class="location-btn" id="openLocationModal">
		        <i class="fa-solid fa-location-dot"></i>
		        <span id="selected-location">
				    <c:choose>
				        <c:when test="${param.region_nm == 'ALL'}">
				            전체지역
				        </c:when>
				        <c:when test="${not empty param.region_nm}">
				            <c:set var="parts" value="${fn:split(param.region_nm, ' ')}" />
				            ${parts[fn:length(parts) - 1]}
				        </c:when>
				        <c:when test="${not empty user.locationVO.region_nm}">
				            <c:set var="userParts" value="${fn:split(user.locationVO.region_nm, ' ')}" />
				            ${userParts[fn:length(userParts) - 1]}
				        </c:when>
				        <c:otherwise>
				            전체지역
				        </c:otherwise>
				    </c:choose>
				</span>
		        <i class="fa-solid fa-chevron-down"></i>
		    </button>
			
		    <div id="locationModal" class="location-modal">
		        <div class="modal-content">
		            <div class="modal-header">
		                <strong>지역 변경</strong>
		                <span class="close-modal" id="closeLocationModal">&times;</span>
		            </div>
		            <input type="text" id="search-area" placeholder="지역이나 동네로 검색하기" class="search-area" autocomplete="off">
		            <input type="hidden" name="region_nm" id="search-region"> 
		            <c:if test="${!empty user.user_num}">
	        		<button class="current-location-btn" id="currentLocation">📍 현재 내 위치 사용하기</button>	
	        		</c:if>	
		            <div class="suggestion-container">
			            <div id="autocomplete-suggestions" class="autocomplete-suggestions" style="display: none;"></div>
		            
				        <div id="recommend-wrapper">
					        <div class="recommend-title">추천</div>
					        <ul id="recommend-list" class="recommend-list">
					        	<li data-region="ALL">전체지역</li>
    							<li data-region="서울특별시 마포구 서교동">서울특별시 마포구 서교동</li>
				                <li data-region="인천광역시 연수구 송도동">인천광역시 연수구 송도동</li>
				                <li data-region="서울특별시 강남구 역삼동">서울특별시 강남구 역삼동</li>
				                <li data-region="경상남도 양산시 물금읍">경상남도 양산시 물금읍</li>
				                <li data-region="경기도 화성시 봉담읍">경기도 화성시 봉담읍</li>
				                <li data-region="충청남도 아산시 배방읍">충청남도 아산시 배방읍</li>
				                <li data-region="서울특별시 서초구 서초동">서울특별시 서초구 서초동</li>
				                <li data-region="경기도 양주시 옥정동">경기도 양주시 옥정동</li>
				                <li data-region="서울특별시 관악구 신림동">서울특별시 관악구 신림동</li>
				                <li data-region="충청남도 천안시 서북구 불당동">충청남도 천안시 서북구 불당동</li>
				                <li data-region="경기도 화성시 향남읍">경기도 화성시 향남읍</li>
				                <li data-region="서울특별시 강남구 청담동">서울특별시 강남구 청담동</li>
				                <li data-region="경기도 남양주시 다산동">경기도 남양주시 다산동</li>
				                <li data-region="경기도 남양주시 별내동">경기도 남양주시 별내동</li>
				                <li data-region="경기도 남양주시 화도읍">경기도 남양주시 화도읍</li>
				                <li data-region="대구광역시 달성군 다사읍">대구광역시 달성군 다사읍</li>
				                <li data-region="서울특별시 강서구 마곡동">서울특별시 강서구 마곡동</li>
				                <li data-region="서울특별시 강남구 압구정동">서울특별시 강남구 압구정동</li>
				                <li data-region="경기도 시흥시 배곧동">경기도 시흥시 배곧동</li>
				                <li data-region="경기도 평택시 고덕동">경기도 평택시 고덕동</li>
				                <li data-region="충청북도 청주시 청원구 오창읍">충청북도 청주시 청원구 오창읍</li>
				            </ul>
				        </div>
				    </div>
		        </div>
			</div>
			
			<!-- 검색창 -->
			<div class="search-wrapper">	
			<form id="search_form" action="userProductList.do" method="get" class="search-bar">
				<input type="hidden" id="region" name="region_nm" value="${param.region_nm}">
			     
			     <div class="search-box">
			    	<select name="keyfield" class="keyfield">
			        	<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>상품제목</option>
			         	<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>카테고리</option>
			        	<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>닉네임</option>
			     	</select>
			     	
			        <input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}" placeholder="검색어를 입력해주세요" autocomplete="off">
			        	
			        <button type="submit" class="search-btn">
      					<i class="fa fa-search"></i>
    				</button>

    				<div id="recent-keywords-box" class="recent-box" style="display: none;">
      					<ul id="recent-keyword-list" class="keyword-list"></ul>
    				</div>
  				</div>
			         
			      <button type="button" class="reset-btn" onclick="location.href='userProductList.do'" id="reset-btn">초기화</button>
			</form> 
			</div>
			<!-- 검색 끝 -->
			
		</div> <!-- 지역 끝 -->
		
		<c:choose>
			<c:when test="${empty regionName}">
				<h2 id="product-regionName" style="padding: 10px 0 0 20px;">전체지역 중고거래</h2>
			</c:when>
			<c:otherwise>
				<h2 id="product-regionName" class="regionName" style="padding: 10px 0 0 20px;"><strong>${regionName}</strong> 중고거래</h2>
			</c:otherwise>
		</c:choose>
		
		<div class="filter-bar">
			<div class="filter-sale">
  				<label>
    				<input type="checkbox" id="filter-sale" ${param.state == '0' ? 'checked' : ''}>
    				<span class="filter-label-text">판매중만 보기</span>
  				</label>
			</div>
		
			<div class="list-space align-right">
				<input type="button" value="등록" onclick="location.href='productWriteForm.do'">
			</div>
		</div>
		
		<c:if test="${count == 0}">
		<div class="result-display">
			표시할 상품이 없습니다.
		</div>
		</c:if>
		<c:if test="${count > 0}">
		<div class="product-space">
			<c:forEach var="product" items="${list}">
			<div class="horizontal-area" data-state="${product.state}">
				<a href="userProductDetail.do?product_num=${product.product_num}">
					<c:if test="${product.thumbnail_img == null}">
						<img src="${pageContext.request.contextPath}/images/NoProductImg.png">
					</c:if>
					<c:if test="${product.thumbnail_img != null}">
						<img src="${pageContext.request.contextPath}/upload/${product.thumbnail_img}" 
							onerror="this.onerror=null; 
							this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
							this.nextElementSibling.style.display='flex';" 
							alt="썸네일 이미지">
						<div class="error-text">이미지 로드 실패</div>
					</c:if>
					<div class="product_status">
		                <c:choose>
		                    <c:when test="${product.state == 0}">
		                        <span class="product-status-badge status-sale">판매중</span>
		                    </c:when>
		                    <c:when test="${product.state == 1}">
		                        <span class="product-status-badge status-reserved">예약중</span>
		                    </c:when>
		                    <c:when test="${product.state == 2}">
		                        <span class="product-status-badge status-soldout">판매완료</span>
		                    </c:when>
		                </c:choose>	
		            </div>
					<div class="product-info">
						<b>${product.title}</b>
						<br>
						<b><fmt:formatNumber value="${product.price}"/>원</b>
						<br>
						<input type="hidden" value="${user.user_num}">
						<small>${product.region_nm} · 
						<c:if test="${product.up_date != null}">끌올 ${product.up_date}</c:if>
						<c:if test="${product.up_date == null}">${product.reg_date}</c:if>
						</small>
					</div>
				</a>
			</div>
			</c:forEach>
			<div class="float-clear align-center">${page}</div>
		</div>
		</c:if>
	</div>
	<input type="hidden" id="region" name="region_nm" value="${param.region_nm}">
	</div>
	<jsp:include page="/WEB-INF/views/common/footer.jsp"/>	
</body>
</html>
