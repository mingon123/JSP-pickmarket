package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;

public class AdminUserDeleteAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}

		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}

		Long target_num = Long.parseLong(request.getParameter("user_num"));

		UserDAO dao = UserDAO.getInstance();
		String id = dao.findUserIdByUserNum(target_num);
		UserVO db_target = dao.checkMember(id);

		// 회원정보 삭제
		dao.deleteUser(target_num);
		// 프로필 사진 삭제
		FileUtil.removeFile(request, db_target.getPhoto());
		
		request.setAttribute("notice_msg", "삭제가 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/user/adminUserList.do");

		return "common/alert_view.jsp";
	}
}
