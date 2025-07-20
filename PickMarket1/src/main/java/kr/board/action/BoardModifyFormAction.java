package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class BoardModifyFormAction implements Action{
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num =(Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		long board_num = Long.parseLong(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO board = dao.getBoard(board_num);
		
		request.setAttribute("board", board);
		
		return "board/boardModifyForm.jsp";
	}

}
