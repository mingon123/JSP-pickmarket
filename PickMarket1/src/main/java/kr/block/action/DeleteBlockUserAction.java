package kr.block.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.block.dao.BlockDAO;
import kr.controller.Action;

public class DeleteBlockUserAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		long blocked_num = Long.parseLong(request.getParameter("blocked_num"));
		
		BlockDAO dao = BlockDAO.getInstance();
		dao.deleteBlockUser(user_num, blocked_num);
		
		request.setAttribute("notice_msg", "차단 해제 완료");
//		request.setAttribute("notice_url", request.getContextPath()+"/user/userDetail.do?user_num="+blocked_num);
		
		// 이전페이지로 반환
		String referer = request.getHeader("referer");
		if (referer == null) {
			referer = request.getContextPath() + "/user/blockUserList.do";
		}
		request.setAttribute("notice_url", referer);
		
		return "common/alert_view.jsp";
	}
}
