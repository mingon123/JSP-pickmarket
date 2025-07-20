package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.review.dao.ReviewDAO;
import kr.review.vo.ReviewVO;
import kr.user.dao.UserDAO;

public class WriteReviewAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}		
		
		ReviewDAO dao = ReviewDAO.getInstance();
		
		String nickname = request.getParameter("getter");
		Long getter_num = dao.getUserNumByNickname(nickname);
		if(getter_num == null) {
			request.setAttribute("notice_msg", "해당 닉네임을 가진 사용자가 없습니다.");
			request.setAttribute("notice_url", request.getContextPath()+"/review/writeReviewForm.do");
			return "common/alert_view.jsp";
		}
				
		ReviewVO review = new ReviewVO();
		review.setRe_getter_num(getter_num);
		review.setRe_content(request.getParameter("content"));
		review.setRe_writer_num(user_num);
		
		String productNumStr = request.getParameter("product_num");
		if (productNumStr == null || productNumStr.trim().isEmpty()) {
		    request.setAttribute("notice_msg", "상품 정보가 없습니다.");
		    request.setAttribute("notice_url", request.getContextPath()+"/main/main.do");
		    return "common/alert_view.jsp";
		}
		review.setProduct_num(Long.parseLong(productNumStr));
		
		// 온도처리
		UserDAO userDAO = UserDAO.getInstance();
		userDAO.modifyTemperatureUser(user_num, 4);
		
		dao.insertReview(review);
		
		request.setAttribute("notice_msg", "리뷰 등록 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/main/main.do");		
		
		return "common/alert_view.jsp";
	}
}	
