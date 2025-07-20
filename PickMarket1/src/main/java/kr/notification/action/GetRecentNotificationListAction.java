package kr.notification.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.notification.dao.NotificationDAO;
import kr.notification.vo.NotificationVO;
import kr.util.StringUtil;

public class GetRecentNotificationListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
        Long user_num = (Long) session.getAttribute("user_num");

        Map<String, Object> mapAjax = new HashMap<>();
        if (user_num == null) {
        	mapAjax.put("result", "logout");
        }else {
        	NotificationDAO dao = NotificationDAO.getInstance();
        	List<NotificationVO> recentList = dao.getRecentNotifications(user_num, 6);
        	int unreadCount = dao.countUnreadNotificationByType(user_num, "all");
        	
        	mapAjax.put("result", "success");
            mapAjax.put("list", recentList);
            mapAjax.put("unreadCount", unreadCount);
        }
        
        return StringUtil.parseJSON(request, mapAjax);
	}

}
