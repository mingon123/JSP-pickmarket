package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;

public class RegisterPhotoUserFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		UserDAO user = UserDAO.getInstance();
		String id = (String) session.getAttribute("id");
		session.setAttribute("id", id);
		String nickname = user.findNicknameByUserId(id);
		
		session.setAttribute("nickname", nickname);
		
		return "user/registerPhotoUserForm.jsp";
	}

}
