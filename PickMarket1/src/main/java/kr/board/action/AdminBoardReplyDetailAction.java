package kr.board.action;

import java.io.File;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;
import kr.util.StringUtil;

public class AdminBoardReplyDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		
		}
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		//관리자로 로그인 한 경우
		long breply_num = Long.parseLong(request.getParameter("breply_num"));
		
		BoardDAO dao = BoardDAO.getInstance();
		
		BoardReplyVO board = dao.getReplyBoard(breply_num);
		
		//HTML를 허용하지 않음
		//board.setBreply_content(StringUtil.useBrNoHtml(board.getBreply_content()));
		
		//HTML를 허용하지 않으면서 줄바꿈 처리
		//board.setBcontent(StringUtil.useBrNoHtml(board.getBcontent()));
		
		request.setAttribute("board", board);
		
		return "board/adminBoardReplyDetail.jsp";
	}

}
