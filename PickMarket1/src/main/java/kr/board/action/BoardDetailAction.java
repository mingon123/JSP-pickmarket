package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

import kr.util.StringUtil;

public class BoardDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		
		}

		long board_num = Long.parseLong(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		//조회수 증가
		dao.updateReadCount(board_num);
		BoardVO board = dao.getBoard(board_num);
		
		//HTML를 허용하지 않음
		board.setBtitle(StringUtil.useBrNoHtml(board.getBtitle()));
		//HTML를 허용하지 않으면서 줄바꿈 처리
		board.setBcontent(StringUtil.useBrNoHtml(board.getBcontent()));
		
		request.setAttribute("board", board);
		
		return "board/boardDetail.jsp";
	}

}
