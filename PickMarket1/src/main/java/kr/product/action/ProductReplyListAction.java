package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductReplyVO;
import kr.util.PagingUtil;

public class ProductReplyListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if
		
		//관리자 여부 체크
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if (user_auth != 9) { //관리자로 로그인하지 않은 경우
			//forward 방식으로 호출
			return "common/accessDenied.jsp"; 
		} // if
		
		//관리자로 로그인한 경우
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield"); // 1:상품제목, 2.댓글 작성자
		String keyword = request.getParameter("keyword");
		
		ProductDAO dao = ProductDAO.getInstance();
		int count = dao.getAdminReplyProductCount(keyfield, keyword);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, 
			Integer.parseInt(pageNum), count, 10, 10, "productReplyList.do");
		
		List<ProductReplyVO> list = null;
		if (count > 0) {
			list = dao.getAdminListReplyProduct(page.getStartRow(), page.getEndRow(), keyfield, keyword);
		} // if
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
		
		//JSP 경로 반환
		return "product/admin_productReplyList.jsp";
	}
} //ProductReplyListAction
