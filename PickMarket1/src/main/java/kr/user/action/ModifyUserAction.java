package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class ModifyUserAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		UserVO user = new UserVO();
		user.setId(request.getParameter("id"));
		user.setName(request.getParameter("name"));
		user.setNickname(request.getParameter("nickname"));
		
		String phone = request.getParameter("phone").replaceAll("-", "");
		user.setPhone(phone);
		user.setUser_num(user_num);
		
		String regionCd = request.getParameter("region_cd");
		if(regionCd == null || regionCd.trim().isEmpty()){
			request.setAttribute("notice_msg", "주소를 현재 위치로 입력해주세요.");
			request.setAttribute("notice_url", request.getContextPath()+"/user/modifyUserForm.do");
			return "common/alert_view.jsp";
		}
		user.setRegion_cd(regionCd);
		
		UserDAO dao = UserDAO.getInstance();
		dao.updateUser(user);
		
		request.setAttribute("notice_msg", "회원정보 수정 완료!");
		request.setAttribute("notice_url", request.getContextPath()+"/user/myPage.do");
		// JSP 경로 반환
		return "common/alert_view.jsp";
	}
}
