package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionBindingEvent;
import kr.controller.Action;
import kr.user.dao.UserDAO;

public class WriteFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num=(Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/users/loginForm.do";
		}
		
		// 지역명 전달
		UserDAO userDAO = UserDAO.getInstance();
    	String regionNm = userDAO.getRegionNmByUser(user_num);
    	
    	request.setAttribute("region", regionNm);
		
		//로그인 된 경우
		return "board/writeForm.jsp";
	}

}
