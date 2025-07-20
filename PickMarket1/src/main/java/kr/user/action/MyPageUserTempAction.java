package kr.user.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.review.dao.MannerRateDAO;
import kr.review.dao.ReviewDAO;
import kr.review.vo.MannerRateVO;
import kr.review.vo.ReviewVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.DurationFromNow;
import kr.util.LocationUtil;

public class MyPageUserTempAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		// 회원정보
		UserDAO dao = UserDAO.getInstance();
		UserVO user = dao.getUser(user_num);
		// 온도
		double user_temp = Math.round((36.5 + (-100 + dao.getUserTemperature(user_num)) / 6.0)*10)/10.0;
		
		// 판매상품
        ProductDAO productDAO = ProductDAO.getInstance();
        List<ProductVO> productList = productDAO.getMyListProduct(1, 3, null, null, user_num);
        int productCount = productDAO.getMyProductCount(null,null,user_num);        
		for (ProductVO product : productList) {
			String regionName = LocationUtil.getRegionName(product.getX_loc(), product.getY_loc());
			String[] size = regionName.split(" ");
	        product.setRegion_nm(regionName.split(" ")[size.length-1]);
	    }
        
		// 매너평가
		MannerRateDAO mannerDAO = MannerRateDAO.getInstance();
		List<MannerRateVO> mannerList = mannerDAO.getListManner(1,3,user_num);
		
		for (MannerRateVO manner : mannerList) {
            String mannerOpText = manner.getMannerOpText(manner.getMannerOp());
            manner.setMannerOp(mannerOpText);
        }

		// 리뷰
		ReviewDAO reviewDAO = ReviewDAO.getInstance();
		List<ReviewVO> reviewList = reviewDAO.getListReview(1, 3, "2", user_num.toString());
		for (ReviewVO r : reviewList) {
		    r.setRe_date(DurationFromNow.getTimeDiffLabel(r.getRe_date()));
		}
		// 받은 리뷰 개수
		int reviewCount = reviewDAO.getTotalReviewCount(user_num);
		
        request.setAttribute("user", user);
		request.setAttribute("user_temp", user_temp);
        request.setAttribute("productList", productList);
        request.setAttribute("productCount", productCount);
        request.setAttribute("mannerList", mannerList);
        request.setAttribute("reviewList", reviewList);
        request.setAttribute("reviewCount", reviewCount);
        
        return "user/myPageUserTemp.jsp";
    }

}
