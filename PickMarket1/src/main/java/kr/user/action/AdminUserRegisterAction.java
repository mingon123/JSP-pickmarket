package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class AdminUserRegisterAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		
		UserVO user = new UserVO();
		
		user.setId(request.getParameter("id"));
		user.setAuth(Integer.parseInt(request.getParameter("auth")));
		user.setName(request.getParameter("name"));
		user.setNickname(request.getParameter("nickname"));
		user.setPasswd(request.getParameter("passwd"));
		String input = request.getParameter("phone");
		user.setPhone(input.replaceAll("[-.\\s]", ""));
		user.setRegion_cd(request.getParameter("region"));
		user.setPhoto(request.getParameter("photo"));
		
		UserDAO dao = UserDAO.getInstance();
		dao.insertUserByAdmin(user);
		
		request.setAttribute("notice_msg", "등록이 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/user/adminUserList.do");

		return "common/alert_view.jsp";
		
	}

}






