package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;

public class LogoutAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session =  request.getSession();
		//로그아웃 처리
		session.invalidate();
		//메인페이지로 리다이렉트
		return "redirect:/main/main.do";
	}

}
