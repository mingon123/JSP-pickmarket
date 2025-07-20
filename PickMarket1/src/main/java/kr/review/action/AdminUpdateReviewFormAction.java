package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.review.dao.ReviewDAO;
import kr.review.vo.ReviewVO;
import kr.util.StringUtil;

public class AdminUpdateReviewFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if (user_auth != 9) {
			return "common/accessDenied.jsp"; 
		}
		
		long review_num = Long.parseLong(request.getParameter("re_num"));
		ReviewDAO dao = ReviewDAO.getInstance();
		ReviewVO review = dao.getReviewDetailByAdmin(review_num);
		
		review.setRe_content(StringUtil.useBrNoHtml(review.getRe_content()));
		
		request.setAttribute("review", review);

		return "review/admin_updateReviewForm.jsp";
	}
}
