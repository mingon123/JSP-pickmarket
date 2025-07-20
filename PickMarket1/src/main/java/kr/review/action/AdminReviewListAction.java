package kr.review.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.review.dao.ReviewDAO;
import kr.review.vo.ReviewVO;
import kr.util.PagingUtil;

public class AdminReviewListAction implements Action {
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
		
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield"); // 1:작성자, 2:받은사람 3.내용
		String keyword = request.getParameter("keyword");
		
		ReviewDAO dao = ReviewDAO.getInstance();
		int count = dao.getAdminReviewCount(keyfield, keyword);

		PagingUtil page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum), count, 10, 10, "adminReviewList.do");
		
		List<ReviewVO> list = null;
		if (count > 0) {
			list = dao.getAdminListReview(page.getStartRow(), page.getEndRow(), keyfield, keyword);
		    for (ReviewVO r : list) {
		    	String fullDate = r.getRe_date();
		        if (fullDate != null && fullDate.length() >= 10) {
		        	r.setRe_date(fullDate.substring(0, 10));
		        }
		    }
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
		
		return "review/admin_reviewList.jsp";
	}
}
