<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카테고리 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/il.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==""){
				alert('검색어를 입력하세요!');
				$('#keyword').val('').focus();
				return false;
			}	
		}); //submit
		
		//카테고리 삭제
		$(document).on('click','.delete-btn',function(){
			//카테고리 번호
			let cg_num = $(this).attr('data-cgnum');
			//서버와 통신
			$.ajax({
				url:'categoryDelete.do',
				type:'post',
				data:{category_num:cg_num},
				dataType:'json',
				success:function(param){
					if(param.result == 'logout'){
						alert('관리자로 로그인해야 삭제할 수 있습니다.');
					}else if(param.result == 'success'){
						alert('삭제 완료!');
						//페이지 새로고침
						location.reload();
					}else if(param.result == 'wrongAccess'){
						alert('잘못된 접근입니다.');
					}else{
					 	alert('카테고리 삭제 오류 발생')
					}
				},
				error:function(){
					//TODO 해당 카테고리 게시글 있는 경우 삭제 불가
					alert('네트워크 오류 발생');
				}
			});	//ajax
		});	//카테고리 삭제 끝
		
		//카테고리 활성여부 변경
		$(document).on('change', '.status-radio', function() {
		    const categoryNum = $(this).data('category');		   
		    const categoryName = $(this).data('name');
		    const newStatus = $(this).val();
		    const statusText = newStatus == 1 ? '활성' : '비활성';

		    if (!confirm('카테고리 ' +categoryName+ '의 상태를 ' +statusText+ '으로 변경하시겠습니까?')) {
		        location.reload(); // 변경 취소 시 원상 복귀
		        return;
		    }

		    $.ajax({
		        url: 'categoryModifyStatus.do',
		        type: 'post',
		        data: {
		            category_num: categoryNum,
		            category_status: newStatus
		        },
		        dataType: 'json',
		        success: function(response) {
		            if (response.result === 'success') {
		                alert('상태가 변경되었습니다.');
		            } else if (response.result === 'logout') {
		                alert('로그인이 필요합니다.');
		                location.href = 'loginForm.do';
		            }else if(response.result == 'wrongAccess'){
						alert('잘못된 접근입니다.');
					} else {
		                alert('카테고리 상태 변경 오류 발생');
		                location.reload();
		            }
		        },
		        error: function() {
		            alert('네트워크 오류 발생');
		            location.reload();
		        }
		    });
		});
		
	});
</script>
</head>
<body>
<div class="page-main">
	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<div class="admin-content-main">
		<%-- 아래 전체 코드 수정요망 --%>
		<h2>카테고리 관리</h2>
		<form id="search_form" action="categoryList.do" method="get">
			<ul class="search">
				<li>
					<select name="keyfield">
						<%-- TODO: 활성여부도 검색 가능하게 하면 좋지 않을까? --%>
						<option value="1" <c:if test="${param.keyfield==1}">selected</c:if>>카테고리명</option>
					</select>
				</li>		
				<li>
					<input type="search" size="16" name="keyword" id="keyword" value="${param.keyword}">
				</li>	
				<li>
					<input type="submit" value="검색">
				</li>
				<li>
					<input type="button" value="초기화" onclick="location.href='categoryList.do'">
				</li>
			</ul>
		</form>

		<c:if test="${count == 0}">
			<div class="result-display">
				표시할 상품이 없습니다.
			</div>
		</c:if>
		<c:if test="${count > 0}">
			<table>
				<tr class="align-center">
					<th>카테고리 번호</th>
					<th>카테고리명</th>
					<th>활성 여부</th>
					<th>삭제</th>
				</tr>
				<c:forEach var="category" items="${list}">
				<tr>					
					<%-- 
					TODO 바로 수정폼으로 갈 수 있도록 함 -> 수정페이지 별도 작성? or 현재페이지에서 변경 가능하도록 함? 
					<td><a href="categorytModifyForm.do?item_num=${category.category_num}">${category.category_name}</a></td>
					--%>
					<td>${category.category_num}</td>
					<td>${category.category_name}</td>					
					<td class="align-center">
						<input type="radio" name="status${category.category_num}" value="1" class="status-radio" 
							data-category="${category.category_num}" data-name="${category.category_name}" 
								<c:if test="${category.category_status == 1}">checked</c:if>>활성

						<input type="radio" name="status${category.category_num}" value="0" class="status-radio" 
							data-category="${category.category_num}" data-name="${category.category_name}" 
								<c:if test="${category.category_status == 0}">checked</c:if>>비활성
					</td>
					<td class="align-center">
						<input type="button" data-cgnum="${category.category_num}" value="삭제" class="delete-btn">
					</td>
				</tr>		
				</c:forEach>
			</table>
			<div class="align-center">${page}</div>
		</c:if>
		<div class="list-space align-right">
			<input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/user/adminPage.do'">
			<input type="button" value="등록" onclick="location.href='categoryWriteForm.do'">
			<input type="button" value="메인" onclick="location.href='${pageContext.request.contextPath}/main/main.do'">     
		</div>
	</div>
</div>	
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>