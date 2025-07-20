package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.location.vo.LocationVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class MyPageAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		UserDAO dao = UserDAO.getInstance();
		UserVO user = dao.getUser(user_num);
		double user_temp = Math.round((36.5 + (-100 + dao.getUserTemperature(user_num)) / 6.0)*10)/10.0;
		
		request.setAttribute("user", user);
		request.setAttribute("user_temp", user_temp);
		
		return "user/myPage.jsp";
	}
}
