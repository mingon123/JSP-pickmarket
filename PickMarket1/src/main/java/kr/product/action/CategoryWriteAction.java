package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.vo.CategoryVO;

public class CategoryWriteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if
		
		//관리자 여부 체크
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if (user_auth != 9) { //관리자로 로그인하지 않은 경우
			//forward 방식으로 호출
			return "common/accessDenied.jsp"; 
		} // if
		
		//관리자로 로그인한 경우
		CategoryVO category = new CategoryVO();
		category.setCategory_name(request.getParameter("category_name"));
		
		CategoryDAO dao = CategoryDAO.getInstance();
		dao.insertCategory(category);
		
		//Refresh 정보를 응답 헤더에 추가
		String url = request.getContextPath() + "/product/categoryList.do";
									//2초 뒤에 자동으로 redirect
		response.addHeader("Refresh", "2;url="+url);
		request.setAttribute("result_title", "카테고리 등록 완료");
		request.setAttribute("result_msg", "성공적으로 등록되었습니다.");
		request.setAttribute("result_url", url);
		
		return "common/result_view.jsp";
	}

} //class
