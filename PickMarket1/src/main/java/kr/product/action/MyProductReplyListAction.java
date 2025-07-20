package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductReplyVO;
import kr.util.PagingUtil;

public class MyProductReplyListAction implements Action {

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

        ProductDAO dao = ProductDAO.getInstance();
        
        int count = dao.getMyProductReplyCount(user_num);
        
        PagingUtil page = 
        		new PagingUtil(Integer.parseInt(pageNum),count,10,10,"myProductReplyList.do");
        
		List<ProductReplyVO> list = null;
		if(count > 0) {
			list = dao.getMyProductReplyList(page.getStartRow(), page.getEndRow(), user_num);
		}
        
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
        
		return "product/myProductReplyList.jsp";
	}

} //class
