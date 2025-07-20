package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.chat.dao.ProductChatRoomDAO;
import kr.controller.Action;
import kr.util.StringUtil;

public class GetUnreadCountAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long user_num = (Long)request.getSession().getAttribute("user_num");
	    long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));
	    
	    Map<String, Object> mapAjax = new HashMap<>();
	    if (user_num == null) {
	        mapAjax.put("result", "logout");
	    } else {
	        ProductChatRoomDAO dao = ProductChatRoomDAO.getInstance();
	        int unreadCount = dao.countUnreadMessages(chatroom_num, user_num);
	        mapAjax.put("result", "success");
	        mapAjax.put("unreadCount", unreadCount);
	    }

	    return StringUtil.parseJSON(request, mapAjax);
	}

}
