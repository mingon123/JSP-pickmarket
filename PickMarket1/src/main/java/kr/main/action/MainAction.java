package kr.main.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO1;
import kr.product.vo.ProductVO;

public class MainAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
		HttpSession session = request.getSession();
        Long user_num = (Long) session.getAttribute("user_num");
        if (user_num == null) user_num = 0L;
		
		//TODO 나중에 ProductDAO로 메서드 이동시킨 후 바꾸기
		ProductDAO1 dao = ProductDAO1.getInstance();
		List<ProductVO> recentList = dao.getRecentProduct(6, user_num);

		int mostViewedNum = dao.getTopProductNum("view", user_num);
		int mostFavoritedNum = dao.getTopProductNum("fav", user_num);
		
		request.setAttribute("mostViewedNum", mostViewedNum);
		request.setAttribute("mostFavoritedNum", mostFavoritedNum);
		request.setAttribute("recentList", recentList);
		
		//JSP 경로 반환
		return "main/main.jsp";
	}

} //class




