package kr.user.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.block.dao.BlockDAO;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
import kr.product.vo.ProductVO;
import kr.review.dao.MannerRateDAO;
import kr.review.dao.ReviewDAO;
import kr.review.vo.MannerRateVO;
import kr.review.vo.ReviewVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.LocationUtil;

public class UserDetailAction implements Action	{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		// 다른 회원 프로필
		Long target_user = Long.parseLong(request.getParameter("user_num"));
		UserDAO dao = UserDAO.getInstance();
		UserVO targetUser = dao.getUser(target_user);
		BlockDAO blockDAO = BlockDAO.getInstance();
		
		// 차단
		// 로그인 사용자와 상대방이 다를 경우에만 차단 여부 확인
		if (!user_num.equals(target_user)) {
			boolean isBlocked = blockDAO.isBlocked(user_num, target_user);
			request.setAttribute("isBlocked", isBlocked);
		}
	    
		// 판매상품
		CategoryDAO categoryDAO = CategoryDAO.getInstance();
		List<CategoryVO> categoryList = categoryDAO.getCategoryStatus(1);
		Set<Long> yesCategory = new HashSet<>();
		for (CategoryVO c : categoryList) {
			yesCategory.add(c.getCategory_num());
		}
		
        ProductDAO productDAO = ProductDAO.getInstance();
        List<ProductVO> allProductList = productDAO.getMyListProduct(1, 20, null, null, target_user);
        List<ProductVO> productList = new ArrayList<>();
        for (ProductVO p : allProductList) {
            if (yesCategory.contains(p.getCategory_num()) && p.getHide_status() == 0 && productList.size() < 3) {
                productList.add(p);
            }
        }
		for (ProductVO product : productList) {
			String regionName = LocationUtil.getRegionName(product.getX_loc(), product.getY_loc());
			String[] size = regionName.split(" ");
	        product.setRegion_nm(regionName.split(" ")[size.length-1]);
	    }
		
		// 매너평가
		MannerRateDAO mannerDAO = MannerRateDAO.getInstance();
		List<MannerRateVO> mannerList = mannerDAO.getListManner(1,3,target_user);
		
	    // 매너평가 상태 체크
	    int mannerCount = mannerDAO.getMannerCount(user_num, target_user);
	    boolean isAlreadyRated = mannerCount > 0;
		
		for (MannerRateVO manner : mannerList) {
            String mannerOpText = manner.getMannerOpText(manner.getMannerOp());
            manner.setMannerOp(mannerOpText);
        }

		// 리뷰
		ReviewDAO reviewDAO = ReviewDAO.getInstance();
		List<ReviewVO> reviewList = reviewDAO.getListReview(1, 3, "2", target_user.toString());
		// 받은 리뷰 개수
		int reviewCount = reviewDAO.getTotalReviewCount(target_user);
		
		// 온도
		double user_temp;
		if (user_num.equals(target_user)) {
			user_temp = Math.round((36.5 + (-100 + dao.getUserTemperature(user_num)) / 6.0)*10)/10.0;
		} else {
			user_temp = Math.round((36.5 + (-100 + dao.getUserTemperature(target_user)) / 6.0)*10)/10.0;
		}
		
		request.setAttribute("user_temp", user_temp);
		request.setAttribute("targetUser", targetUser); // VO
        request.setAttribute("target_user", target_user); // user_num
        request.setAttribute("productList", productList);
        request.setAttribute("productCount", allProductList.size());
        request.setAttribute("mannerList", mannerList);
        request.setAttribute("isAlreadyRated", isAlreadyRated);
        request.setAttribute("reviewList", reviewList);
        request.setAttribute("reviewCount", reviewCount);
        
		return "user/userDetail.jsp";
	}
}
 