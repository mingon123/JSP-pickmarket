package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductFavVO;
import kr.util.PagingUtil;

public class MyProductFavListAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if
		
		//로그인한 경우
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) pageNum = "1";
        String keyfield = request.getParameter("keyfield");
        String keyword = request.getParameter("keyword");

        ProductDAO dao = ProductDAO.getInstance();
        //내가 찜한 상품 개수 조회
        int count = dao.getMyProductFavCount(keyfield, keyword, user_num);
        PagingUtil page = 
        		new PagingUtil(Integer.parseInt(pageNum),count,10,10,"myProductFavList.do");
        
		List<ProductFavVO> list = null;
		if(count > 0) {
			list = dao.getMyProductFavList(keyfield, keyword, page.getStartRow(), page.getEndRow(), user_num);
		}
        
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
        
		return "product/myProductFavList.jsp";
	}
	
} //class
