package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
import kr.product.vo.ProductImgVO;
import kr.product.vo.ProductVO;

public class ProductModifyFormAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//관리자&사용자 공통 (TODO: 공통 사용여부 재검토요망)
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
				
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if
		
		//로그인한 경우
		//사용자별 버튼 동작 설정 위함
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		long product_num = Long.parseLong(request.getParameter("product_num"));
		
		ProductDAO dao = ProductDAO.getInstance();
		ProductVO product = dao.getProduct(product_num);
		request.setAttribute("product", product);
		
		//카테고리 정보 불러오기(수정 폼에서 옵션 목록 중 선택받으려고)		
		CategoryDAO categoryDao = CategoryDAO.getInstance();
		List<CategoryVO> categoryList = categoryDao.getAllCategories();
		request.setAttribute("categoryList", categoryList);
		
		//상품 이미지 목록 가져오기
		List<ProductImgVO> productImgList = dao.getProductImages(product_num);
		request.setAttribute("productImgList", productImgList);
		
		return "product/productModifyForm.jsp";
	}

} //class
