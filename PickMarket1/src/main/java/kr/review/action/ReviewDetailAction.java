package kr.review.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.review.dao.ReviewDAO;
import kr.review.vo.ReviewVO;

public class ReviewDetailAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		long review_num = Long.parseLong(request.getParameter("re_num"));
		
		ReviewDAO dao = ReviewDAO.getInstance();
		ReviewVO review = dao.getReviewDetailByAdmin(review_num);
		
        String fullDate = review.getRe_date();
        if (fullDate != null && fullDate.length() >= 10) {
        	review.setRe_date(fullDate.substring(0, 10).replace("-", "."));
        }
		
        ProductDAO productDAO = ProductDAO.getInstance();
        ProductVO product = productDAO.getProduct(review.getProductVO().getProduct_num());
        
        request.setAttribute("product", product);
		request.setAttribute("review", review);
		
		return "review/reviewDetail.jsp";
	}
}
