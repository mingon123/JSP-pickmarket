package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.vo.CategoryVO;
import kr.user.dao.UserDAO;

public class ProductWriteFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//관리자&사용자 공통
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if

		//로그인한 경우
		//카테고리 정보 불러오기(등록 폼에서 옵션 목록 중 선택받으려고)
		CategoryDAO dao = CategoryDAO.getInstance();
		List<CategoryVO> categoryList = dao.getAllCategories();
		
		// 지역명 전달
		UserDAO userDAO = UserDAO.getInstance();
    	String regionNm = userDAO.getRegionNmByUser(user_num);
    	
    	request.setAttribute("region", regionNm);
		request.setAttribute("categoryList", categoryList);
		
		return "product/productWriteForm.jsp";
	}

} //class
