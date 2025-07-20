<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>상품관련 채팅방 목록</title>
<link rel="stylesheet" type="text/css"href="${pageContext.request.contextPath}/css/jw_chatList.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	//닉네임 검색, 안읽은 채팅 필터링 
    function filterChatList() {
        const keyword = $('#chatSearchInput').val().toLowerCase();
        const unreadOnly = $('#unreadOnlyCheck').is(':checked');
        
        $('.chat-room-item').each(function(){
            const name = $(this).find('.chat-name').text().toLowerCase();
            const unread = $(this).find('.unread-badge').length > 0;
            const match = name.includes(keyword);
            
            // 읽지 않은 채팅만 보기 체크되면 unread도 만족해야 보여짐
            const shouldShow = match && (!unreadOnly || unread);
            $(this).toggle(shouldShow);
        });
    }
	
    $('#chatSearchInput').on('input', filterChatList);
    $('#unreadOnlyCheck').on('change', filterChatList);
	
});
</script>
</head>
<body>
<div class="page-main">
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <div class="chat-list-wrapper">
        <h2 class="chat-list-title"><b class="nickname">'${user_nickname}'</b> 님의 <b class="nickname">'${product.title}'</b> 상품관련 채팅 목록</h2>

		<div class="chat-controls">
    		<label class="unread-only-label">
        		<input type="checkbox" id="unreadOnlyCheck"> 안 읽은 채팅만 보기
    		</label>
    		<input type="text" id="chatSearchInput" class="chat-search-input" placeholder="닉네임 검색">
		</div>
		
        <div class="chat-room-list-container">
            <div class="chat-room-list">
                <c:choose>
                    <c:when test="${not empty chatList}">
                        <c:forEach var="room" items="${chatList}">
                <a href="${pageContext.request.contextPath}/chat/chatDetail.do?chatroom_num=${room.chatroom_num}" class="chat-room-item">
                    <c:choose>
    					<c:when test="${room.seller_num == user_num}">
        					<c:choose>
            					<c:when test="${empty room.buyer.photo}">
                					<c:set var="profilePath" value="${pageContext.request.contextPath}/images/face.png"/>
            					</c:when>
            					<c:otherwise>
                					<c:set var="profilePath" value="${pageContext.request.contextPath}/upload/${room.buyer.photo}"/>
            					</c:otherwise>
        					</c:choose>
    					</c:when>
    					<c:otherwise>
        					<c:choose>
            					<c:when test="${empty room.seller.photo}">
                					<c:set var="profilePath" value="${pageContext.request.contextPath}/images/face.png"/>
            					</c:when>
            					<c:otherwise>
                					<c:set var="profilePath" value="${pageContext.request.contextPath}/upload/${room.seller.photo}"/>
            					</c:otherwise>
        					</c:choose>
    					</c:otherwise>
					</c:choose>

						<img src="${profilePath}" alt="프로필" class="chat-profile"
     									onerror="this.src='${pageContext.request.contextPath}/images/face.png'"/>

                    <!-- 내용 -->
                    <div class="chat-content">
                        <div class="chat-name">
                            <c:choose>
                                <c:when test="${room.seller_num == user_num}">
                                    ${room.buyer.nickname}
                                </c:when>
                                <c:otherwise>
                                    ${room.seller.nickname}
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div class="chat-message">
    						<c:choose>
        						<%-- 삭제된 메시지 --%>
        						<c:when test="${room.lastChat.deleted == 0}">
            						<img src="${pageContext.request.contextPath}/images/delete.png" alt="삭제됨" style="width:14px; vertical-align:middle; margin-right:4px;">
            							${room.lastChat.message}
        						</c:when>
        						<c:otherwise>
            						<c:if test="${not empty room.lastChat.filename}">
                						<img src="${pageContext.request.contextPath}/images/photo-icon.png" alt="사진" style="width:14px; vertical-align:middle; margin-right:4px;">
            						</c:if>
            						<c:choose>
                						<c:when test="${fn:length(room.lastChat.message) > 30}">
                    						${fn:substring(room.lastChat.message, 0, 30)}...
                						</c:when>
                						<c:otherwise>
                    						${room.lastChat.message}
                						</c:otherwise>
            						</c:choose>
        						</c:otherwise>
    						</c:choose>
						</div>
                   	</div>

                    <!-- 시간 + 안읽음 -->
                    <div class="chat-meta">
                    	<span class="chat-time">
                        <c:if test="${not empty room.lastChat.chat_date}">
                            <fmt:parseDate value="${room.lastChat.chat_date}" var="parsedDate" pattern="yyyy-MM-dd HH:mm:ss"/>
                            <fmt:formatDate value="${parsedDate}" pattern="MM/dd	HH:mm"/>
                        </c:if>
                        </span>
                        <c:if test="${room.unreadCount > 0}">
                            <div class="unread-badge">${room.unreadCount}</div>
                        </c:if>
                    </div>
                </a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p style="text-align:center; margin-top:50px;">채팅방이 없습니다.</p>
        </c:otherwise>
    </c:choose>
    </div>
    </div>
    <div class="back-btn-wrapper">
    	<button onclick="history.back();" class="back-btn">이전으로</button>
    </div>
</div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>
