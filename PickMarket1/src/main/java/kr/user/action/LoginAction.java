package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class LoginAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		
		
		UserDAO dao = UserDAO.getInstance();
		UserVO user = dao.checkMember(id);
		boolean check = false;
		
		if(user != null) {
			//비밀번호 일치 여부 체크
			check = user.isCheckedPassword(passwd);
			//정지회원의 경우 상태 표시
			request.setAttribute("auth", user.getAuth());
			
			if (user.getAuth() == 3) {
				request.setAttribute("notice_msg", "신고 누적으로 이용이 정지된 계정입니다.");
				request.setAttribute("notice_url", request.getContextPath() + "/user/loginForm.do");
				return "/common/alert_view.jsp";
			}else if(user.getAuth() == 0) {
				request.setAttribute("notice_msg", "탈퇴한 계정입니다.");
				request.setAttribute("notice_url", request.getContextPath() + "/user/loginForm.do");
				return "/common/alert_view.jsp";
			}
		}
		
		if(check) { //인증 성공
			//로그인 처리
			HttpSession session = request.getSession();
			session.setAttribute("user_num", user.getUser_num());
			session.setAttribute("user_id", user.getId());
			session.setAttribute("user_auth", user.getAuth());
			session.setAttribute("user_nickname", user.getNickname());
			session.setAttribute("user_photo", user.getPhoto());
			session.setAttribute("user_region_cd", user.getRegion_cd());
			session.setAttribute("user_score", user.getScore());
			
			//메인으로 리다이렉트
			return "redirect:/main/main.do";
		}
		
	
		return "user/login.jsp";
	}

}
