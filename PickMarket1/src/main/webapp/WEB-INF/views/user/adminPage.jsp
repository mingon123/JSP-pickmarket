<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>관리자 메인페이지</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/mg.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
</head>
<body>
<div class="admin-main">
	<div class="page-main1">
		<jsp:include page="/WEB-INF/views/common/header.jsp" />
		<div class="mypage-div">
			<h1>관리자 메인페이지</h1>
			<div class="section">
				<h2>중고상품 관련</h2>
				<div class="btn-grid">
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/product/adminProductList.do'">상품관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/product/productReplyList.do'">상품댓글관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/product/categoryList.do'">카테고리관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/review/adminReviewList.do'">리뷰관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/chat/adminChatRoomList.do'">채팅방관리</button>
				</div>
			</div>
	
			<div class="section">
				<h2>커뮤니티 관련</h2>
				<div class="btn-grid">
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/board/adminBoardList.do'">커뮤니티 관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/board/adminBoardReplyList.do'">커뮤니티 댓글관리</button>
				</div>
			</div>
	
			<div class="section">
				<h2>문의/공지 관련</h2>
				<div class="btn-grid">
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/qna/adminQnaList.do'">1:1문의 관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/info/adminNoticeList.do'">공지사항 관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/info/adminOPList.do'">운영정책 관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/info/adminFAQList.do'">FAQ 관리</button>
				</div>
			</div>
	
			<div class="section">
				<h2>회원관리 관련</h2>
				<div class="btn-grid">
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/user/adminUserList.do'">회원관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/report/adminReportList.do'">신고관리</button>
					<button class="admin-btn" onclick="location.href='${pageContext.request.contextPath}/block/adminBlockList.do'">차단관리</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div style="padding-bottom: 50px;"></div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>