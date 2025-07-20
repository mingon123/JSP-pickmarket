package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductReplyVO;
import kr.user.dao.UserDAO;
import kr.util.StringUtil;

public class WriteProductReplyAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,String> mapAjax = new HashMap<String,String>();
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인되지 않은 경우
			mapAjax.put("result", "logout");
		} else { //로그인된 경우
			ProductReplyVO reply = new ProductReplyVO();
			reply.setUser_num(user_num); //댓글 작성자 회원번호
			reply.setReply_content(request.getParameter("reply_content"));
			reply.setReply_ip(request.getRemoteAddr());
			//댓글의 부모 글번호
			reply.setProduct_num(Long.parseLong(request.getParameter("product_num")));
			
			UserDAO userDAO = UserDAO.getInstance();
			userDAO.modifyTemperatureUser(user_num, 1);
			
			ProductDAO dao = ProductDAO.getInstance();
			dao.insertReplyProduct(reply);
			
			mapAjax.put("result", "success");
		} //if
		
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}

} //class