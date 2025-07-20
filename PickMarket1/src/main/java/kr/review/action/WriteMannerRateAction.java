package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.review.dao.MannerRateDAO;
import kr.review.vo.MannerRateVO;

public class WriteMannerRateAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		Long rated_num = Long.parseLong(request.getParameter("rated_num"));
		String[] selectOpsStr = request.getParameterValues("manner");
		int[] selectedOps = new int[selectOpsStr.length];
		for (int i = 0; i < selectOpsStr.length; i++) {
			selectedOps[i] = Integer.parseInt(selectOpsStr[i]);
		}

		MannerRateDAO dao = MannerRateDAO.getInstance();
		int count = dao.getMannerCount(user_num, rated_num);

		if (count == 0) {
			dao.insertMannerRate(user_num, rated_num, selectedOps);
		} else {
			dao.updateManner(user_num, rated_num, selectedOps);
		}

		String from = request.getParameter("from");
		String redirectUrl;
		if ("chat".equals(from)) {
			Long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));
		    redirectUrl = request.getContextPath() + "/chat/chatDetail.do?chatroom_num=" + chatroom_num;
		} else {
		    redirectUrl = request.getContextPath() + "/user/userDetail.do?user_num=" + rated_num;
		}
		
		request.setAttribute("notice_msg", "매너평가 등록성공");
		request.setAttribute("notice_url", redirectUrl);

		return "common/alert_view.jsp";
	}
}
