<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_searchword.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jw_communityList.css">
<script type="text/javascript">
	//서버에서 받은 지역 목록
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
		        regionVal = 'ALL';
		        $('#region').val(regionVal);
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
		        $('#region').val('ALL');
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
		    currentUrl.searchParams.set('region_nm', region);
		    history.replaceState(null, '', currentUrl.toString());
		
		    fetchFilteredProducts(region);
		}
     	
		$('#recommend-list li').on('click', function () {
		    const selectedRegion = $(this).data('region');
		    if (selectedRegion) {
		        changeRegion(selectedRegion);
		    }
		});
	 	
		// 페이지 처리
	    $(document).on('click', '.pagination a', function (e) {
	        e.preventDefault();
	        const pageNum = new URL($(this).attr('href'), location.href).searchParams.get('pageNum');
	        const region = $('#region').val();
	        fetchFilteredProducts(region, pageNum);
	    });
		
	    // AJAX로 필터링된 상품 리스트 가져오기
		function fetchFilteredProducts(region, pageNum = 1) {		
		    $.ajax({
		        url: 'boardList.do',
		        type: 'GET',
		        data: {
		            region_nm: region,
		            keyfield: $('#search_form select[name="keyfield"]').val() || '',
		            keyword: $('#search_form input[name="keyword"]').val() || '',
		            pageNum: pageNum
		        },
		        success: function (response) {		        	
		            let parsedHTML = $('<div>').html(response);
		            let productListHtml = parsedHTML.find('.community-list').html();
		            let paginationHtml = parsedHTML.find('.pagination').html();
		            let productCount = parsedHTML.find('.community-list').children().length;

		            if (region === 'ALL') {
		                $('#regionName').html('<b>${user.nickname}</b> 님의 동네는 <b>전체지역</b> 입니다');
		            } else {
		                $('#regionName').html('<b>${user.nickname}</b> 님의 동네는 <b>' + region + '</b> 입니다');
		            }

		            if (productCount === 0 || !productListHtml || $.trim(productListHtml) === '') {
		                $('.community-list').html('<div class="result-display">표시할 글이 없습니다.</div>');
		                $('.pagination').html('');
		            } else {
		                $('.community-list').html(productListHtml);
		                $('.pagination').html(paginationHtml);
		            }
		        },
		        error: function (xhr, status, error) {
		            console.error("글 목록 가져오기 실패:", error);
		        }
		    });
		}
	});
</script>
</head>
<body>
    <div class="page-main">
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <div class="content-main">
        	<h3 style="text-align: center;" id="regionName" class="regionName"><b>${user.nickname}</b> 님의 동네는 <b>
			<c:choose>
				<c:when test="${empty regionName}">
					전체지역
				</c:when>
				<c:otherwise>
					${regionName}
				</c:otherwise>
			</c:choose>
        	</b> 입니다.</h3><br>

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

            <div class="search-wrapper">
            	<div class="search-bar-container">
            		<form id="search_form" action="boardList.do" method="get" class="search-bar">
                		<input type="hidden" id="region" name="region_nm" value="${param.region_nm}">
               
           				<div class="search-box">
                			<select name="keyfield" class="keyfield">
                    			<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>제목</option>
                        		<option value="2" <c:if test="${param.keyfield==2}">selected</c:if>>작성자</option>
                    			<option value="3" <c:if test="${param.keyfield==3}">selected</c:if>>내용</option>
                    		</select>
               
                    		<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}" placeholder="검색어를 입력해주세요" autocomplete="off">
               
                    		<button type="submit" class="search-btn">
	 							<i class="fa fa-search"></i>
				    		</button>
				    	</div>
				    	
           				<button type="button" class="reset-btn" onclick="location.href='boardList.do'" id="reset-btn">초기화</button>
            		</form>
	            	<input type="button" class="register-btn" value="글 등록" onclick="location.href='writeForm.do'">
            	</div>
            </div>
         </div>

            <c:if test="${count == 0}">
                <div class="result-display">표시할 글이 없습니다</div>
            </c:if>
            
            <%-- 
			<c:if test="${count > 0}">
			<div class="product-space">
	        	<c:forEach var="board" items="${board}">
	            	<table class="board-table" >
	                	<tr>
	                    	<td>
	                        	<div style="float: left;"><b>${board.btitle}</b></div>
	                        	<!-- 글제목 -->
	                        	<div style="float: right;">
								    ${board.nickname} •
								    <fmt:formatDate value="${board.breg_date}" pattern="yyyy-MM-dd"/> 
								    <c:if test="${not empty board.bmodi_date}">
								        /<fmt:formatDate value="${board.bmodi_date}" pattern="yyyy-MM-dd"/>
								    </c:if>
								</div>
		                        <!-- 글작성자 --><!-- 글작성일 --><!-- 글수정일-->
		                        <br><hr size="1" noshade width="100%">
		                        <a href="boardDetail.do?board_num=${board.board_num}">${board.bcontent}</a>
		                        <!-- 글내용 -->
		                        <div style="float: right;">조회수 ${board.bhit} 좋아요 ${board.like_count}
		                        </div>
	                    	</td>
	                	</tr>
	            	</table>
	        	</c:forEach>
			<div class="align-center">${page}</div>
			</div>
			</c:if>
			--%>
			<c:if test="${count > 0}">
  				<div class="community-list">
    				<c:forEach var="board" items="${board}">
    				
      					<a href="boardDetail.do?board_num=${board.board_num}" class="post-item">
      					<div class="post-left">
        					<div class="post-header">
          						<div class="post-title">
          						<c:choose>
    									<c:when test="${fn:length(board.btitle) > 20}">
      											${fn:substring(board.btitle, 0, 20)}...
    									</c:when>
    									<c:otherwise>
      											${board.btitle}
    									</c:otherwise>
  									</c:choose>
          						</div>
          						<div class="post-meta">
            						${board.nickname} · ${board.breg_date}
          						</div>
        					</div>
        					
        					<div class="post-content">
        						<c:choose>
    								<c:when test="${fn:length(board.bcontent) > 40}">
      										${fn:substring(board.bcontent, 0, 40)}...
    								</c:when>
    								<c:otherwise>
      										${board.bcontent}
    								</c:otherwise>
 								 </c:choose>
        					</div>
        					<div class="post-footer">
          						조회수 ${board.bhit} · 좋아요 ${board.like_count} · 댓글 ${board.reply_count}
        					</div>
        					</div>
        					
        					<div class="post-thumbnail">
        						<c:choose>
        							<c:when test="${not empty board.bfilename}">
      									<img src="${pageContext.request.contextPath}/upload/${board.bfilename}" alt="썸네일 이미지" 
      										onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png';">
        							</c:when>
        							<c:otherwise>
        								<div class="image-placeholder"></div>
        							</c:otherwise>
        						</c:choose>
  							</div>
      					</a>
      					
    				</c:forEach>
    				<div class="align-center">${page}</div>
  				</div>
			</c:if>
			
        </div>
    </div>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
