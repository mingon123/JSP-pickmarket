package kr.notification.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.notification.dao.NotificationDAO;
import kr.util.StringUtil;

public class DeleteNotificationAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		long notifi_num = Long.parseLong(request.getParameter("notifi_num"));

		Map<String, String> mapAjax = new HashMap<>();
		if (user_num == null) {
			mapAjax.put("result", "logout");
		}else{
			NotificationDAO dao = NotificationDAO.getInstance();
			dao.deleteNotification(notifi_num);
			mapAjax.put("result", "success");
		}
		return StringUtil.parseJSON(request, mapAjax);
	}

}
