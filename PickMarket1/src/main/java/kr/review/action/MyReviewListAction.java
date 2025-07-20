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
import kr.util.PagingUtil;

public class MyReviewListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		String target_num = request.getParameter("user_num");
		Long target = (target_num != null) ? Long.parseLong(target_num) : user_num;
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) pageNum = "1";
		
		String keyfield = "1"; // 쓰는 사람 기준 기준
		String keyword = target_num;
		
		ReviewDAO dao = ReviewDAO.getInstance();
		UserDAO userDao = UserDAO.getInstance();
		int count = dao.getReviewCount(keyfield, keyword);
		
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"reviewList.do","&user_num=" + target);
		
		List<ReviewVO> list = dao.getListReview(1,20,keyfield,keyword);
		if(count > 0) {
			list = dao.getListReview(page.getStartRow(), page.getEndRow(), keyfield, keyword);
			
		    for (ReviewVO r : list) {
		        String fullDate = r.getRe_date();
		        if (fullDate != null && fullDate.length() >= 10) {
		        	r.setRe_date(fullDate.substring(0, 10).replace("-", "."));
		        }
		        
		        if (r.getUserVO() == null) {
		            Long writerNum = r.getRe_writer_num();
		            UserVO writer = userDao.getUser(writerNum);
		            r.setUserVO(writer);
		        }
		        
		        Long getterNum = r.getRe_getter_num();
		        UserVO getter = userDao.getUser(getterNum);
		        r.setUserVO(getter);
			}
		}
        
		int reviewCount = dao.getTotalReviewCount(target);
		UserVO login_user = userDao.getUser(user_num);
		
		
        request.setAttribute("list", list);
        request.setAttribute("count", count);
        request.setAttribute("page", page.getPage());
        request.setAttribute("startRow", page.getStartRow());
        request.setAttribute("reviewCount", reviewCount);
        
        request.setAttribute("userNum", user_num); // 해당페이지 회원번호
        request.setAttribute("user", login_user); // 현재 접속한 계정
        
		return "review/myReviewList.jsp";
	}
}
