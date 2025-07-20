<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/layout.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#photo_btn').click(function(){
			$('#photo_choice').show();
			$(this).hide();//수정 버튼 감추기
		});
		
		//이미지 미리보기
		//처음 화면에 보여지는 이미지 저장
		//(이미지 선택을 취소할 때 원상복귀)
		let photo_path = $('.my-photo').attr('src');
		$('#photo').change(function(){
			let my_photo = this.files[0];
			if(!my_photo){
				//선택을 취소하면 원래 처음 화면으로 되돌림
				$('.my-photo').attr('src',photo_path);
				return;
			}
			
			if(my_photo.size > 1024 * 1024){
				alert(Math.round(my_photo.size/1024) 
						 + 'kbytes(1024kbytes까지만 업로드 가능)');
				$('.my-photo').attr('src',photo_path);
				$(this).val('');//선택한 파일 정보 지우기
				return;
			}
			
			//화면에서 이미지 미리보기
			const reader = new FileReader();
			reader.readAsDataURL(my_photo);
			
			reader.onload=function(){
				$('.my-photo').attr('src',reader.result);
			};
		});//end of change
		
		// 취소버튼 클릭시
		$('#photo_reset').click(function(){
			$('.my-photo').attr('src', photo_path);
			$('#photo_choice').hide();
			$('#photo_btn').show();
			$('#photo').val('');
			return;
		});
		
		//이미지 전송
		$('#photo_submit').click(function(){
			if($('#photo').val()==''){
				alert('파일을 선택하세요!');
				$('#photo').focus();
				return;
			}//end of if
			
			//파일 전송
			const form_data = new FormData();
			/*
			document.getElementById('photo').files[0]
			*/
			form_data.append('photo',$('#photo')[0].files[0]);
			//서버와 통신
			$.ajax({
				url:'updateUserPhoto.do',
				type:'post',
				data:form_data,
				dataType:'json',
				contentType:false,//데이터 객체를 문자열로 바꿀지에 대한 값.true면 일반문자
				processData:false,//해당 타입을 true로 하면 일반 text로 구분
				success:function(param){
					if(param.result == 'logout'){
						alert('로그인 후 사용하세요!');
					}else if(param.result == 'success'){
						alert('프로필 사진이 수정되었습니다.');
						//수정된 이미지 정보 저장
						photo_path = $('.my-photo').attr('src');
						$('#photo').val('');
						$('#photo_choice').hide();
						$('#photo_btn').show();//수정 버튼 표시
					}else{
						alert('파일 전송 오류 발생');
					}
				},
				error:function(){
					alert('네트워크 오류 발생');
				}
			});
		});
		
	});
</script>
</head>
<body>
<div class="page-main1">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="mypage-div">
		<h3><a href="${pageContext.request.contextPath}/user/myPage.do">마이페이지</a></h3>
		<div class="mypage-profile-wrapper">
			<ul>
				<li class="mypage-photo">
					<c:if test="${empty user.photo}">
					<img src="${pageContext.request.contextPath}/images/face.png" width="150" height="150">
					</c:if>
					<c:if test="${!empty user.photo}">
					<img src="${pageContext.request.contextPath}/upload/${user.photo}" width="150" height="150"
						onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';">
					</c:if>
				</li>
				<li>
					<div class="align-right">
						<input type="button" value="수정" id="photo_btn">
					</div>
					<div id="photo_choice" style="display:none;">
						<input type="file" id="photo" accept="image/gif,image/png,image/jpeg"><br>
						<input type="button" value="수정" id="photo_submit">
						<input type="button" value="취소" id="photo_reset">    
					</div>
				</li>
			</ul>
			<div class="mypage-basic-info">
				<ul>
					<li>닉네임 : <b>${user.nickname}</b></li>
					<li>동네 : ${user.locationVO.region_nm}</li>
				</ul>
			</div>
			
			<div class="manner-score">
				<div class="manner-circle" onclick="location.href='myPageUserTemp.do'">
					<div class="manner-value">${user_temp}°</div>
					<div class="manner-label">매너온도</div>
				</div>
			</div>
		</div>
		<hr width="100%" size="1" noshade="noshade">

		<h4>상세정보</h4>
		<div class="detail-info-box">
		    <div class="detail-info-item">
		        <i class="fa fa-user-circle"></i> <span class="label">아이디</span>
		        <span class="value">${user.id}</span>
		    </div>
		    <div class="detail-info-item">
		        <i class="fa fa-id-badge"></i> <span class="label">이름</span>
		        <span class="value">${user.name}</span>
		    </div>
		    <div class="detail-info-item">
		        <i class="fa fa-smile-o"></i> <span class="label">닉네임</span>
		        <span class="value">${user.nickname}</span>
		    </div>
		    <div class="detail-info-item">
		        <i class="fa fa-phone"></i> <span class="label">전화번호</span>
		        <span class="value">${fn:substring(user.phone,0,3)}-${fn:substring(user.phone,3,7)}-${fn:substring(user.phone,7,11)}</span>
		    </div>
		    <div class="detail-info-item">
		        <i class="fa fa-map-marker"></i> <span class="label">지역</span>
		        <span class="value">${user.locationVO.region_nm}</span>
		    </div>
		    <div class="detail-info-item">
		        <i class="fa fa-calendar"></i> <span class="label">가입일</span>
		        <span class="value">
		            <fmt:formatDate value="${user.reg_date}" pattern="yyyy-MM-dd"/>
		        </span>
		    </div>
		    <c:if test="${!empty user.modi_date}">
		        <div class="detail-info-item">
		            <i class="fa fa-refresh"></i> <span class="label">최신 정보 수정일</span>
		            <span class="value">
		                <fmt:formatDate value="${user.modi_date}" pattern="yyyy-MM-dd"/>
		            </span>
		        </div>
		    </c:if>
		</div>
		<hr width="100%" size="1" noshade="noshade">
		
		<div class="account-box">
		    <div class="account-title">계정 관리</div>
		    <ul class="account-list">
		        <li><a href="modifyUserForm.do">회원정보 수정</a></li>
		        <li><a href="modifyPasswordForm.do">비밀번호 수정</a></li>
		        <li><a href="deleteUserForm.do">회원 탈퇴</a></li>
		        <li><a href="${pageContext.request.contextPath}/block/blockUserList.do">차단계정 관리</a></li>
		    </ul>
		</div>
		
		<div class="account-box">
		    <div class="account-title">나의 거래</div>
		    <ul class="account-list">
		        <li><a href="${pageContext.request.contextPath}/product/myProductList.do">판매 내역</a></li>
		        <li><a href="${pageContext.request.contextPath}/chat/chatRoomList.do">채팅 내역</a></li>
		        <li><a href="${pageContext.request.contextPath}/product/myProductFavList.do">찜한 상품</a></li>
		        <li><a href="${pageContext.request.contextPath}/product/productViewList.do">최근 본 상품</a></li>
		        <li><a  href="${pageContext.request.contextPath}/product/myProductReplyList.do">내가 쓴 상품 댓글</a></li>
		        <li><a href="${pageContext.request.contextPath}/review/myReviewList.do?user_num=${user.user_num}">내가 쓴 거래 후기</a></li>
		    </ul>
		</div>

		<div class="account-box">
		    <div class="account-title">나의 활동</div>
		    <ul class="account-list">
		        <li><a href="${pageContext.request.contextPath}/board/myBoardList.do">내 커뮤니티 글</a></li>
		        <li><a href="${pageContext.request.contextPath}/board/myBoardReplyList.do">내가 쓴 댓글</a></li>
		        <li><a href="${pageContext.request.contextPath}/board/myBoardFavList.do">내가 좋아요한 글</a></li>
		        <li><a href="${pageContext.request.contextPath}/qna/qnaList.do">1:1문의 내역</a></li>
		        <li><a href="${pageContext.request.contextPath}/report/reportList.do">신고 내역</a></li>
		    </ul>
		</div>
		
		<div class="account-box">
		    <div class="account-title">설정</div>
		    <ul class="account-list">
		        <li><a href="keywordList.do">키워드 알림 설정</a></li>
		    </ul>
		</div>
	</div>
</div>
</body>
</html>