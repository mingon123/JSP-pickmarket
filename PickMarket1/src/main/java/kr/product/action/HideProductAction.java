package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;

public class HideProductAction implements Action{
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) { 
			return "redirect:/user/loginForm.do";
		}
		
		long product_num = Long.parseLong(request.getParameter("product_num"));
		
		ProductDAO dao = ProductDAO.getInstance();
		int hide_status = dao.getHideStatus(product_num, user_num);

		if(hide_status == 0) {
			dao.hideProduct(product_num, user_num);
			request.setAttribute("notice_msg", "상품을 숨겼습니다.");
		}else {
			dao.unhideProduct(product_num, user_num);
			request.setAttribute("notice_msg", "숨기기 해제했습니다.");
		}
		request.setAttribute("notice_url", request.getContextPath()+"/product/myProductList.do");
		
		return "common/alert_view.jsp";
	}
}
