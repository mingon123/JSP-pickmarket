package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class WriteMannerRateFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		Long rated_num = Long.parseLong(request.getParameter("rated_num"));
		UserDAO dao = UserDAO.getInstance();
		UserVO user = dao.getUser(rated_num);
		
		String productNumParam = request.getParameter("product_num");
		ProductVO product = null;

		if (productNumParam != null && !productNumParam.isEmpty()) {
		    long product_num = Long.parseLong(productNumParam);
		    ProductDAO productDAO = ProductDAO.getInstance();
		    product = productDAO.getProduct(product_num);
		    request.setAttribute("product", product);
		}
		
        String from = request.getParameter("from");
        String chatroom_num = request.getParameter("chatroom_num");
		
		request.setAttribute("user", user);
		request.setAttribute("rated_num", rated_num);
        request.setAttribute("from", from);
        request.setAttribute("chatroom_num", chatroom_num);		
		
		return "review/writeMannerRateForm.jsp";
	}
}
