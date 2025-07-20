package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class RegisterUserAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//자바빈(VO) 생성
		UserVO user = new UserVO();
		
		user.setId(request.getParameter("id"));
		user.setName(request.getParameter("name"));
		user.setNickname(request.getParameter("name"));
		user.setPasswd(request.getParameter("passwd"));
		String input = request.getParameter("phone");
		user.setPhone(input.replaceAll("[-.\\s]", ""));
		user.setRegion_cd(request.getParameter("region_cd"));
		
		UserDAO dao = UserDAO.getInstance();
		dao.insertMember(user);
		
		HttpSession session = request.getSession();
		session.setAttribute("id", request.getParameter("id"));
		
		return "redirect:/user/registerPhotoUserForm.do";
	}

}






