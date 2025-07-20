package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.StringUtil;

public class UserProductDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//비회원도 상품상세정보 조회 가능
		
		//전송된 상품번호 반환
		long product_num = Long.parseLong(request.getParameter("product_num"));

		//1건의 데이터를 읽어옴
		ProductDAO dao = ProductDAO.getInstance();
		//조회수 증가
		dao.updateProductCount(product_num);
		ProductVO product = dao.getProduct(product_num);
		
		if (product == null) {
		    String referer = request.getHeader("Referer"); // 이전 페이지 주소
		    if (referer == null || referer.trim().isEmpty()) {
		        referer = request.getContextPath() + "/main/main.do"; // 대체 URL
		    }

		    request.setAttribute("notice_msg", "삭제된 상품입니다.");
		    request.setAttribute("notice_url", referer);
		    return "common/alert_view.jsp";
		}
		
		//상품 설명 줄바꿈 처리(HTML 태그 허용) -> TODO: 내용 재확인요망
		product.setContent(StringUtil.useBrHtml(product.getContent()));
		
		//카테고리 정보 불러오기(카테고리 상태 확인할때 필요)	
		CategoryDAO categoryDao = CategoryDAO.getInstance();
		List<CategoryVO> categoryList = categoryDao.getAllCategories();
		
		//판매자 정보 불러오기
		UserDAO userDao = UserDAO.getInstance();
		UserVO seller = userDao.getUser(product.getUser_num());
		
		// 최근 본 게시물 저장
	    HttpSession session = request.getSession();
	    Long user_num = (Long)session.getAttribute("user_num");
	    if (user_num != null) {
	        dao.saveProductView(user_num, product_num);
	    }
	    
		request.setAttribute("product", product);		
		request.setAttribute("categoryList", categoryList);
		request.setAttribute("productImgList", product.getProductImgList());
		request.setAttribute("seller", seller);
		
		return "product/user_productDetail.jsp";
	}

} //class
