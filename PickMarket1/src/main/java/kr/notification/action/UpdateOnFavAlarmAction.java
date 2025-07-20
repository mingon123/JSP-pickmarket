package kr.notification.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.notification.dao.NotificationDAO;
import kr.util.StringUtil;

public class UpdateOnFavAlarmAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long user_num = (Long) request.getSession().getAttribute("user_num");
		long product_num = Long.parseLong(request.getParameter("product_num"));

		Map<String, String> mapAjax = new HashMap<>();
		if (user_num == null) {
			mapAjax.put("result", "logout");
		} else {
			NotificationDAO dao = NotificationDAO.getInstance();
			dao.updateOnFavAlarm(user_num, product_num);
			mapAjax.put("result", "success");
		}

		return StringUtil.parseJSON(request, mapAjax);
	}


}
