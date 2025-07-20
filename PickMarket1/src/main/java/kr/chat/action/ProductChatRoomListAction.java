package kr.chat.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatRoomDAO;
import kr.chat.vo.ProductChatRoomVO;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;

public class ProductChatRoomListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		Long product_num = Long.parseLong(request.getParameter("product_num"));
		
		ProductDAO productDAO = ProductDAO.getInstance();
		long seller_num = productDAO.getSellerNumByProduct(product_num);
		
		// 판매자만 접근 허용
	    if (seller_num != user_num) {
	    	request.setAttribute("message", "잘못된 접근입니다.");
	    	request.setAttribute("redirectUrl", request.getContextPath() + "/main/main.do");
	        return "/common/accessDenied.jsp";
	    }
	    
	    ProductVO productVO = productDAO.getProduct(product_num);
	    ProductChatRoomDAO roomDAO = ProductChatRoomDAO.getInstance();
        List<ProductChatRoomVO> chatList = 
        		roomDAO.getSellerChatRoomsByProduct(product_num,user_num);

        request.setAttribute("chatList", chatList);
        request.setAttribute("product_num", product_num);
        request.setAttribute("product", productVO);

        return "chat/product_chat_list.jsp";
	}

}
