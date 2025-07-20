package kr.user.action;

import javax.tools.DocumentationTool.Location;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.location.dao.LocationDAO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class AdminUserDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		
		long target_user_num = Long.parseLong(request.getParameter("user_num"));
		
		UserDAO dao = UserDAO.getInstance();
		UserVO target = dao.getDetailMemberByAdmin(target_user_num);
		
		LocationDAO locationDAO = LocationDAO.getInstance();
		String region_nm = locationDAO.findRegionNmByCode(target.getRegion_cd());
		request.setAttribute("region_nm", region_nm);
		
		request.setAttribute("target", target);
		
		return "user/adminUserDetail.jsp";
	}

}





