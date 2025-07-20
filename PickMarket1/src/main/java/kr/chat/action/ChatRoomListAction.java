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

public class ChatRoomListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		// 로그인 된 경우
        ProductChatRoomDAO dao = ProductChatRoomDAO.getInstance();
        
        // user_num에 해당하는 전체 채팅방 리스트 가져오기 (페이징 없음)
        List<ProductChatRoomVO> list = dao.getChatRoomListWithDetails(user_num);
        
        request.setAttribute("chatList", list);
        
		return "chat/chat_list.jsp";
	}

}
