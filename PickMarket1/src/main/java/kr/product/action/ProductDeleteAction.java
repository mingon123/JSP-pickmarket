package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;

public class ProductDeleteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if		
		
		//로그인 된 경우
		long product_num = Long.parseLong(request.getParameter("product_num")); 
		
		ProductDAO dao = ProductDAO.getInstance();
		ProductVO db_product = dao.getProduct(product_num);
		
		//관리자 여부 체크
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		
		if (user_auth == 9) { //관리자는 모든 회원 글 삭제 가능
			dao.deleteProduct(product_num);
		} else 
			if (user_num != db_product.getUser_num()) {
			//일반 회원 로그인한 회원번호와 작성자 회원번호가 불일치
			//forward 방식
			return "common/accessDenied.jsp";
		}
		
		//로그인한 회원번호와 작성자 회원번호 일치
		dao.deleteProduct(product_num);
		
		request.setAttribute("notice_msg", "상품 삭제 완료");
		if (user_auth == 9) {
			request.setAttribute("notice_url", request.getContextPath()+"/product/adminProductList.do");
		} else {
			request.setAttribute("notice_url", request.getContextPath()+"/product/userProductList.do");
		}
		
		return "common/alert_view.jsp";
	}

} //class
