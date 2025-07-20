package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductReplyVO;

public class ProductReplyDetailAction implements Action {

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
		long reply_num = Long.parseLong(request.getParameter("reply_num"));
		
		ProductDAO dao = ProductDAO.getInstance();
		ProductReplyVO reply = dao.getReplyProductDetailByAdmin(reply_num);
		
		String fullDate = reply.getReply_date();
        reply.setReply_date(fullDate.substring(0, 10).replace("-", "."));
		
		request.setAttribute("reply", reply);
		
		return "product/admin_productReplyDetail.jsp";
	}

} ///class
