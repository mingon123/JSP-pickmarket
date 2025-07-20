package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductReplyVO;
import kr.util.StringUtil;

public class UpdateProductReplyAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//댓글 번호
		long reply_num = Long.parseLong(request.getParameter("reply_num"));
		
		ProductDAO dao = ProductDAO.getInstance();
		ProductReplyVO db_reply = dao.getReplyProduct(reply_num);
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		Map<String,String> mapAjax = new HashMap<String,String>();
		if (user_num == null) { //로그인 되지 않은 경우
			mapAjax.put("result", "logout");
		} else if (user_num == db_reply.getUser_num()) {
			//로그인한 회원번호와 작성자 회원번호 일치
			//자바빈(VO) 생성
			ProductReplyVO reply = new ProductReplyVO();
			reply.setReply_num(reply_num);
			reply.setReply_content(request.getParameter("reply_content"));
			reply.setReply_ip(request.getRemoteAddr());
			
			dao.updateReplyProduct(reply);
			
			mapAjax.put("result", "success");
		} else {
			//로그인한 회원번호와 작성자 회원번호 불일치
			mapAjax.put("result", "wrongAccess");
		}// if
		
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}

} //class
