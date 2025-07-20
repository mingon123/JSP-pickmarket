package kr.chat.action;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatRoomDAO;
import kr.chat.vo.ProductChatRoomVO;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;

public class AdminChatDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		
		String chatroom_num = request.getParameter("chatroom_num");
		ProductChatRoomDAO roomDAO = ProductChatRoomDAO.getInstance();
		ProductDAO productDAO = ProductDAO.getInstance();
		ProductChatRoomVO room;
		ProductVO product = null;
		
		room = roomDAO.selectChatRoom(Long.parseLong(chatroom_num));
		product = productDAO.getProduct(room.getProduct_num());
		
		request.setAttribute("product", product);
		request.setAttribute("productChatRoomVO", room);
		
		return "chat/adminChatDetail.jsp";
	}

}
