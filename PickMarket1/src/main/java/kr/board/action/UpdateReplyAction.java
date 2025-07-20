package kr.board.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class UpdateReplyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//댓글 번호
		long breply_num = Long.parseLong(
				          request.getParameter("breply_num"));
		
		BoardDAO dao = BoardDAO.getInstance();
		BoardReplyVO db_reply = dao.getReplyBoard(breply_num);
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		Map<String,String> mapAjax = 
				                new HashMap<String,String>();
		if(user_num == null) {//로그인이 되지 않은 경우
			mapAjax.put("result", "logout");
		}else if(user_num == db_reply.getUser_num()) {
			//로그인한 회원번호와 작성자 회원번호 일치
			//자바빈(VO) 생성
			BoardReplyVO reply = new BoardReplyVO();
			reply.setBreply_num(breply_num);
			reply.setBreply_content(request.getParameter("breply_content"));
			reply.setBreply_ip(request.getRemoteAddr());
			
			dao.updateReplyBoard(reply);
			
			mapAjax.put("result", "success");			
		}else {
			//로그인한 회원번호와 작성자 회원번호 불일치
			mapAjax.put("result", "wrongAccess");
		}
		//JSON 문자로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}
}







