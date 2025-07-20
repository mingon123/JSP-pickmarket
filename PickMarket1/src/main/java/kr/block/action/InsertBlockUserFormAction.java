package kr.block.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;

public class InsertBlockUserFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}

		Long blocked_num = Long.parseLong(request.getParameter("blocked_num"));
		
		UserDAO dao = UserDAO.getInstance();
		String nickname = dao.getUserNickname(blocked_num);

		request.setAttribute("blocked_num", blocked_num);
		request.setAttribute("nickname", nickname);
		
		return "block/insertBlockUserForm.jsp";
	}
}
