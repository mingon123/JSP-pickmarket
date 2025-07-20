package kr.product.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.util.PagingUtil;

public class ProductViewListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) pageNum = "1";

        String keyfield = request.getParameter("keyfield");
        String keyword = request.getParameter("keyword");

        ProductDAO dao = ProductDAO.getInstance();
        
        int count = dao.getProductViewCountByUser(keyfield, keyword, user_num);
        
        PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,10,10,"productViewList.do");
        
		List<ProductVO> list = null;
		if(count > 0) {
			list = dao.getListProductViewByUser(page.getStartRow(), page.getEndRow(), keyfield, keyword, user_num);
		}
        
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
        
		return "product/productViewList.jsp";
	}
}
