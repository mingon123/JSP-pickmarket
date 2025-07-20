package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.review.dao.ReviewDAO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class WriteReviewFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}		
		
		Long target_user = Long.parseLong(request.getParameter("user_num"));
		Long product_num = Long.parseLong(request.getParameter("product_num"));
		
		// 이미 리뷰 작성했는지 확인
		ReviewDAO reviewDAO = ReviewDAO.getInstance();
		boolean isWritten = reviewDAO.isReviewWritten(user_num, target_user, product_num);
		if (isWritten) {
			request.setAttribute("notice_msg", "이미 리뷰를 작성한 상품입니다.");
			request.setAttribute("notice_url", request.getContextPath() + "/main/main.do");
			return "common/alert_view.jsp";
		}
		
		UserDAO dao = UserDAO.getInstance();
		UserVO targetUser = dao.getUser(target_user);
		
		request.setAttribute("targetUser", targetUser);
		request.setAttribute("product_num", product_num);
		
		return "review/writeReviewForm.jsp";
	}
}
