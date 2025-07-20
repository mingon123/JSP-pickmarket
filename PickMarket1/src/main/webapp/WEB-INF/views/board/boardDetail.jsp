<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>커뮤니티 글 상세</title>
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"	href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jw_community_detail.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/board.fav.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/board.reply.js"></script>

</head>
<body>
<div class="page-main">
  <jsp:include page="/WEB-INF/views/common/header.jsp" />
  <div class="content-main">
    <div class="post-header">
      <c:choose>
        <c:when test="${not empty board.photo}">
          <img src="${pageContext.request.contextPath}/upload/${board.photo}" class="profile-img"
          	onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/face.png';">
        </c:when>
        <c:otherwise>
          <img src="${pageContext.request.contextPath}/images/face.png" class="profile-img">
        </c:otherwise>
      </c:choose>
      <div class="profile-info">
        <span class="nickname">${board.nickname}</span>
        <span class="reg-date">
          <c:if test="${not empty board.bmodi_date}">
            ${board.bmodi_date}(수정됨)
          </c:if>
          <c:if test="${empty board.bmodi_date}">
            ${board.breg_date}
          </c:if>
        </span>
      </div>
    </div>

    <h3 class="post-title">${board.btitle}</h3>

    <div class="post-content">
      ${board.bcontent}
    </div>
    
    <c:if test="${!empty board.bfilename}">
      <div class="post-image">
        <img src="${pageContext.request.contextPath}/upload/${board.bfilename}" class="detail-img"
        	onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/free-icon-photo-gallery-4503645.png';">
      </div>
    </c:if>


    <div class="post-meta">
      <span class="hit">조회 ${board.bhit}</span>
      <span id="reply-count" class="reply-count-text">댓글 <span id="reply-total">0</span></span>
      <span class="likes">
      	<span class="like-label">좋아요</span>
        <img id="output_fav" data-num="${board.board_num}" src="${pageContext.request.contextPath}/images/unheart.png" width="20">
        <span id="output_fcount">0</span>
      </span>
    </div>

    <div class="align-center post-buttons">
    	<input type="button" value="목록" onclick="location.href='boardList.do'">
    	<c:if test="${user_num == board.user_num}">
        <input type="button" value="수정" onclick="location.href='boardModifyForm.do?board_num=${board.board_num}'">
        <input type="button" value="삭제" id="delete_btn">
        <script>
          document.getElementById('delete_btn').onclick = function () {
            if (confirm('삭제하시겠습니까?')) {
              location.replace('delete.do?board_num=${board.board_num}');
            }
          }
        </script>
    	</c:if>
    </div>

    <hr size="1" noshade width="100%">
    
		<!-- 댓글 시작 -->
		<div id="reply_div">
			<span class="re-title">댓글 달기</span>
			<form id="re_form">
				<input type="hidden" name="board_num" value="${board.board_num}" id="board_num">
				<textarea rows="3" cols="50" name="breply_content" id="breply_content" class="rep-content"
					<c:if test="${empty user_num}">disabled="disabled"</c:if>><c:if test="${empty user_num}">로그인해야 작성할 수 있습니다</c:if></textarea>
				<c:if test="${!empty user_num}">
					<div id="re_first">
						<span class="letter-count">300/300</span>
					</div>
					<div id="re_second" class="align-right">
						<input type="submit" value="댓글등록" >
					</div>
				</c:if>
			</form>
		</div>
		<!-- 댓글 목록 출력 시작 -->
		<div id="output" class="output">
		<div class="paging-button" style="display: none;">
			<input type="button" value="다음글 보기">
		</div>
		<div id="loading" style="display: none;">
			<img src="${pageContext.request.contextPath}/images/loading.gif" width="50" height="50">
		</div>
		<!-- 댓글 끝 -->
	</div>
	</div>
	</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>