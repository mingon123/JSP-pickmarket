package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;

public class DeleteViewAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		long product_num = Long.parseLong(request.getParameter("product_num"));
		ProductDAO dao = ProductDAO.getInstance();
		dao.deleteView(user_num, product_num);
		
		request.setAttribute("notice_msg", "최근 본 상품 기록 삭제 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/product/productViewList.do");
		
		return "common/alert_view.jsp";
	}
}
