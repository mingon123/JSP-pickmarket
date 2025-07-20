package kr.block.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.block.dao.BlockDAO;
import kr.block.vo.BlockVO;
import kr.controller.Action;
import kr.user.dao.UserDAO;

public class InsertBlockUserAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}

		long blocked_num = Integer.parseInt(request.getParameter("blocked_num"));
		
		BlockVO block = new BlockVO();
		block.setBlocked_num(blocked_num);
		block.setBlocker_num(user_num);
		block.setBlock_content(request.getParameter("block_content"));
		
		BlockDAO dao = BlockDAO.getInstance();
		dao.insertBlock(block);
		
		request.setAttribute("notice_msg", "회원 차단 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/user/userDetail.do?user_num="+blocked_num);		
		
		return "common/alert_view.jsp";
	}
}
