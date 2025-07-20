package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.vo.CategoryVO;
import kr.util.PagingUtil;

public class CategoryListAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do"; //TODO 로그인 화면 주소 확인요망
		} // if
		
		//관리자 여부 체크
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if (user_auth != 9) { //관리자로 로그인하지 않은 경우
			//forward 방식으로 호출
			return "common/accessDenied.jsp"; 
		} // if
		
		//관리자로 로그인한 경우
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		CategoryDAO dao = CategoryDAO.getInstance();
		int count = dao.getCategoryCount(keyfield, keyword);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, 
				Integer.parseInt(pageNum), count, 10, 10, "categoryList.do");
		
		List<CategoryVO> list = null;
		if (count > 0) {
			list = dao.getListCategory(page.getStartRow(), page.getEndRow(), keyfield, keyword);
		} // if
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
		
				
		return "product/categoryList.jsp";
	}

} //class
