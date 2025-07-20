package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.chat.dao.ProductChatRoomDAO;
import kr.controller.Action;
import kr.util.StringUtil;

public class DeleteDealAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> mapAjax = new HashMap<>();
		try {
			long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));

			ProductChatRoomDAO dao = ProductChatRoomDAO.getInstance();
			dao.clearDealInfo(chatroom_num);

			mapAjax.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			mapAjax.put("result", "error");
		}
		return StringUtil.parseJSON(request, mapAjax);
	}

}
