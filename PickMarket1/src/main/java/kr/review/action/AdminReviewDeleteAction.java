package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.qna.dao.QnaDAO;
import kr.review.dao.ReviewDAO;

public class AdminReviewDeleteAction implements Action {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if (user_auth != 9) {
			return "common/accessDenied.jsp"; 
		}

		Long review_num = Long.parseLong(request.getParameter("re_num"));

		ReviewDAO dao = ReviewDAO.getInstance();
		dao.deleteReviewByAdmin(review_num);
		
		request.setAttribute("notice_msg", "삭제가 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/review/adminReviewList.do");

		return "common/alert_view.jsp";

	}
}
