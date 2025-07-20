package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.chat.dao.ProductChatDAO;
import kr.controller.Action;
import kr.util.StringUtil;

public class UpdateProcessedStatusAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long user_num = (Long)request.getSession().getAttribute("user_num");
		long chat_num = Long.parseLong(request.getParameter("chat_num"));

		Map<String, Object> mapAjax = new HashMap<>();
		if (user_num == null) {
			mapAjax.put("result", "logout");
		} else {
			ProductChatDAO dao = ProductChatDAO.getInstance();
			dao.updateProcessedStatus(chat_num);
			mapAjax.put("result", "success");
		}

		return StringUtil.parseJSON(request, mapAjax);
	}

}
