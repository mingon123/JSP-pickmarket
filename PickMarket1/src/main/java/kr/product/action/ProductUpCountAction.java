package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;

public class ProductUpCountAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Long user_num = (Long) session.getAttribute("user_num");
        if (user_num == null) {
            return "redirect:/user/loginForm.do";
        }
        
        long product_num = Long.parseLong(request.getParameter("product_num"));
        ProductDAO dao = ProductDAO.getInstance();
        int up_count = dao.getUpCount(product_num);
        
        if (up_count >= 3) {
            request.setAttribute("notice_msg", "끌올은 최대 3번까지만 가능합니다.");
            request.setAttribute("notice_url", request.getContextPath() + "/product/myProductList.do");
        } else {
            dao.updateUpCount(product_num);
            request.setAttribute("notice_msg", "끌올이 완료되었습니다.");
            request.setAttribute("notice_url", request.getContextPath() + "/product/myProductList.do");
        }
        
        return "/common/alert_view.jsp";
	}
}
