package kr.block.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.block.dao.BlockDAO;
import kr.block.vo.BlockVO;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class AdminBlockDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인 되지 않은 경우
			return "redirect:/member/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		
		long blocker_num = Long.parseLong(request.getParameter("blocker_num"));
		long blocked_num = Long.parseLong(request.getParameter("blocked_num"));
		
		BlockDAO dao = BlockDAO.getInstance();
		BlockVO block = dao.getDetailBlockByAdmin(blocker_num,blocked_num);
		
		request.setAttribute("block", block);
		
		return "block/adminBlockDetail.jsp";
	}

}





