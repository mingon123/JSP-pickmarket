package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;

public class AdminDeleteProductReplyAction implements Action{

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
		Long reply_num = Long.parseLong(request.getParameter("reply_num"));

		ProductDAO dao = ProductDAO.getInstance();
		dao.deleteReplyProductByAdmin(reply_num);
		
		request.setAttribute("notice_msg", "삭제가 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/product/productReplyList.do");

		return "common/alert_view.jsp";
	}

} //class
