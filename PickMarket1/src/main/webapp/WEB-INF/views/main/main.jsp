<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="https://fonts.googleapis.com/css?family=Poppins:100,200,300,400,500,600,700,800,900&display=swap" rel="stylesheet">

    <title>P!ck Market</title>


    <!-- Additional CSS Files -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/font-awesome.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/templatemo-hexashop.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/owl-carousel.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lightbox.css">
    
    <%-- 스타일 추가 --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/il_main.css">
<!--

TemplateMo 571 Hexashop

https://templatemo.com/tm-571-hexashop

-->
    </head>
    
    <body>
    
    <!-- ***** Preloader Start ***** -->
    <div id="preloader">
        <div class="jumper">
            <div></div>
            <div></div>
            <div></div>
        </div>
    </div>  
    <!-- ***** Preloader End ***** -->
    
    
    <!-- ***** Header Area Start ***** -->

	<jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <!-- ***** Header Area End ***** -->

    <!-- ***** Main Banner Area Start ***** -->
    <div class="main-banner" id="top">
        <div class="container-fluid">
            <div class="row">
            
                <div class="col-lg-5">
                    <div class="left-content">
                        <div class="thumb" id="main-img">
                            <div class="inner-content">
                                <h4>P!ckMarket</h4>
                                <span>우리 동네 거래, 한 번에 Pick!</span>
                                <div class="main-border-button">
                                    <a href="${pageContext.request.contextPath}/product/userProductList.do">상품검색 바로가기</a>
                                </div>
                            </div>
                            <img src="${pageContext.request.contextPath}/images/mainIMG.jpg" alt="메인이미지">
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-7">
                    <div class="right-content">
                        <div class="row">
                        
                            <div class="col-lg-6">
                              <!-- 가장 많이 본 상품 + 찜한 상품 위아래로 배치 -->
							  <div class="row">
							    <!-- 가장 많이 본 상품 -->
							    <div class="col-lg-12">
							    
                                <div class="right-first-image">
                                    <div class="thumb" id="main-img">
                                        <div class="inner-content">
                                            <h4>가장 많이 본 상품</h4>
                                            <span>Most Viewed Item</span>
                                        </div>
                                        <div class="hover-content">
                                            <div class="inner">
                                                <h4>가장 많이 본 상품</h4>
                                                <p>Most Viewed Item</p>
                                                <div class="main-border-button">
                                                    <a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${mostViewedNum}">상품 자세히보기</a>
                                                </div>
                                            </div>
                                        </div>
                                        <img src="${pageContext.request.contextPath}/images/MostViewed.jpg">                                        
                                    </div>
                                </div>
                            </div>
                            
                            <div class="col-lg-12">
                                <div class="right-first-image">
                                    <div class="thumb" id="main-img">
                                        <div class="inner-content">
                                            <h4>가장 많이 찜한 상품</h4>
                                            <span>Most Favorited Items</span>
                                        </div>
                                        <div class="hover-content">
                                            <div class="inner">
                                                <h4>가장 많이 찜한 상품</h4>
                                                <p>Most Viewed Items</p>
                                                <div class="main-border-button">
                                                    <a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${mostFavoritedNum}">상품 자세히보기</a>
                                                </div>
                                            </div>
                                        </div>
                                        <img src="${pageContext.request.contextPath}/images/MostLiked.jpg">
                                    </div>
                                </div>
                            </div>
                            </div>
                            </div>
                            
                            <div class="col-lg-6">
                                <div class="right-first-image">
                                    <div class="thumb" id="main-img">
                                        <div class="inner-content">
                                            <h4></h4>
                                            <span></span>
                                        </div>
                                        <div class="hover-content">
                                            <div class="inner">
                                                <h4>P!ckMarket 앱 다운</h4>
                                                <p>지금 바로 앱 다운받고,<br>내가 찾던 상품을 Pick하세요!</p>
                                                <div class="main-border-button">
                                                    <a href="#">다운받으러 가기</a>
                                                </div>
                                            </div>
                                        </div>
                                        <img src="${pageContext.request.contextPath}/images/AppDown.png">
                                    </div>
                                </div>
                            </div>
                            
                            
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
    <!-- ***** Main Banner Area End ***** -->

    <!-- ***** 최신상품 Area Starts ***** -->
    <section class="section" id="men">
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <div class="section-heading">
                        <h2>최신 등록 상품</h2>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="men-item-carousel">
                        <div class="owl-men-item owl-carousel">
                        
                        <c:forEach var="product" items="${recentList}">
                        <div class="item">
                                <div class="thumb">
                                    <div class="hover-content">
                                        <ul>
                                            <li><a href="${pageContext.request.contextPath}/product/userProductDetail.do?product_num=${product.product_num}"><i class="fa fa-eye"></i></a></li>
                                        </ul>
                                    </div>
                                    
                                    <%-- 이미지 영역 --%>
                                    <div class="image-wrapper">
										<c:if test="${product.thumbnail_img == null}">
											<img src="${pageContext.request.contextPath}/images/NoProductImg.png" alt="${product.title}">
										</c:if>
										<c:if test="${product.thumbnail_img != null}">
											<img src="${pageContext.request.contextPath}/upload/${product.thumbnail_img}" 
												onerror="this.onerror=null; 
												this.src='${pageContext.request.contextPath}/images/NoProductImg.png'; 
												this.nextElementSibling.style.display='flex';" 
												alt="${product.title}">
											<div class="error-text">이미지 로드 실패</div>
										</c:if>
									</div>
                                </div>
                                <div class="down-content">
		                            <h4>${product.title}</h4>
								    <p><fmt:formatNumber value="${product.price}"/>원</p>
									<small>${product.region_nm} · 
									<c:if test="${product.up_date != null}">끌올 ${product.up_date}</c:if>
									<c:if test="${product.up_date == null}">${product.reg_date}</c:if>
									</small>
                                </div>
                        </div>
                        </c:forEach>
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- ***** 최신상품 Area Ends ***** -->
    
    <!-- ***** Footer Start ***** -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
    

    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>

    <!-- Bootstrap -->
    <script src="${pageContext.request.contextPath}/js/popper.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    <!-- Plugins -->
    <script src="${pageContext.request.contextPath}/js/owl-carousel.js"></script>
    <script src="${pageContext.request.contextPath}/js/accordions.js"></script>
    <script src="${pageContext.request.contextPath}/js/datepicker.js"></script>
    <script src="${pageContext.request.contextPath}/js/scrollreveal.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/waypoints.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.counterup.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/imgfix.min.js"></script> 
    <script src="${pageContext.request.contextPath}/js/slick.js"></script> 
    <script src="${pageContext.request.contextPath}/js/lightbox.js"></script> 
    <script src="${pageContext.request.contextPath}/js/isotope.js"></script> 
    
    <!-- Global Init -->
    <script src="${pageContext.request.contextPath}/js/custom.js"></script>

    <script>

        $(function() {
            var selectedClass = "";
            $("p").click(function(){
            selectedClass = $(this).attr("data-rel");
            $("#portfolio").fadeTo(50, 0.1);
                $("#portfolio div").not("."+selectedClass).fadeOut();
            setTimeout(function() {
              $("."+selectedClass).fadeIn();
              $("#portfolio").fadeTo(50, 1);
            }, 500);
                
            });
        });

    </script>

  </body>
</html>