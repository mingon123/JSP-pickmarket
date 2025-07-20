package kr.notification.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.notification.dao.NotificationDAO;
import kr.notification.vo.NotificationVO;

public class NotificationListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
        Long user_num = (Long) session.getAttribute("user_num");

        if (user_num == null) {
            return "redirect:/user/loginForm.do";
        }

        String type = request.getParameter("type");
        NotificationDAO dao = NotificationDAO.getInstance();
        List<NotificationVO> list;
        
        if (type == null || type.equals("all")) {
            list = dao.getNotificationsByUser(user_num);
        } else {
            list = dao.getTypeNotificationsByUser(user_num, type);
        }
        int allCount = dao.countUnreadNotificationByType(user_num, "all");
        int favCount = dao.countUnreadNotificationByType(user_num, "favorite");
        int keyCount = dao.countUnreadNotificationByType(user_num, "keyword");

        request.setAttribute("allCount", allCount);
        request.setAttribute("favCount", favCount);
        request.setAttribute("keyCount", keyCount);
        request.setAttribute("list", list);
        request.setAttribute("selectedType", type == null ? "all" : type);
        
        return "notification/notificationList.jsp";
	}

}
