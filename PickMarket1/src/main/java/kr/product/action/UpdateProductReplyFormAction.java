package kr.product.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductReplyVO;
import kr.util.StringUtil;

public class UpdateProductReplyFormAction implements Action {

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
		
		//textarea에서 보여줄 줄바꿈 처리된 내용 반환
		String reply_content = reply.getReply_content().replaceAll("<br>", "\n");
		
		request.setAttribute("reply", reply);
		request.setAttribute("reply_content", reply_content);

		return "product/admin_updateProductReplyForm.jsp";
	}

} //class
