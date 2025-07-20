package kr.review.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.review.dao.ReviewDAO;
import kr.review.vo.ReviewVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.DurationFromNow;
import kr.util.PagingUtil;

public class ReviewListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		String target_num = request.getParameter("user_num");
		Long userNum = (target_num != null) ? Long.parseLong(target_num) : user_num;
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) pageNum = "1";
		
		String keyfield = "2"; // 받는사람 기준
		String keyword = target_num;
		
		ReviewDAO dao = ReviewDAO.getInstance();
		int count = dao.getReviewCount(keyfield, keyword);
		
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"reviewList.do","&user_num=" + userNum);
		
		List<ReviewVO> list = dao.getListReview(1,20,keyfield,keyword);
		if(count > 0) {
			list = dao.getListReview(page.getStartRow(), page.getEndRow(), keyfield, keyword);
			for (ReviewVO r : list) {
				r.setRe_date(DurationFromNow.getTimeDiffLabel(r.getRe_date()));
			}
		}
		UserDAO userDao = UserDAO.getInstance();
		int reviewCount = dao.getTotalReviewCount(userNum);
		UserVO target_user = userDao.getUser(userNum);
		String nickname = target_user.getNickname();
		UserVO login_user = userDao.getUser(user_num);
		
		
        request.setAttribute("list", list);
        request.setAttribute("count", count);
        request.setAttribute("page", page.getPage());
        request.setAttribute("reviewCount", reviewCount);
        request.setAttribute("nickname", nickname);
        request.setAttribute("userNum", userNum); // 해당페이지 회원번호
        request.setAttribute("user", login_user); // 현재 접속한 계정
        
		return "review/reviewList.jsp";
	}
}
