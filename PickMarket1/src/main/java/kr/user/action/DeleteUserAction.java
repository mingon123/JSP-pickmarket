package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;

public class DeleteUserAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String passwd = request.getParameter("passwd");
		
		String user_id = (String)session.getAttribute("user_id");
		
		UserDAO dao = UserDAO.getInstance();
		UserVO db_user = dao.checkMember(id);
		boolean check = false;

		if(db_user!=null && id.equals(user_id) && name.equals(db_user.getName())) {
			// 비밀번호 일치 여부 체크
			check = db_user.isCheckedPassword(passwd);
		}
		if(check) { // 인증 성공
			// 회원정보 삭제
			dao.deleteUser(user_num);
			// 프로필 사진 삭제
			FileUtil.removeFile(request, db_user.getPhoto());
			// 로그아웃
			session.invalidate();
		}
		
		request.setAttribute("check", check);

		return "user/deleteUser.jsp";
	}
}
