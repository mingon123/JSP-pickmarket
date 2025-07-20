package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class ModifyUserFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		UserDAO dao = UserDAO.getInstance();
		UserVO user = dao.getUser(user_num);
		
		request.setAttribute("user", user);
		
		return "user/modifyUserForm.jsp";
	}
}
