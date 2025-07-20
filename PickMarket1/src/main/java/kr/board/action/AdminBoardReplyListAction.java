package kr.board.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.PagingUtil;

public class AdminBoardReplyListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session =request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth !=9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";			
		}
		//관리자로 로그인한 경우
		String pageNum = request.getParameter("pageNum");
		if(pageNum==null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword= request.getParameter("keyword");
		
		//long board_num = Long.parseLong(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		int count = dao.getBoardCount(keyfield, keyword);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum),count,10,10,"adminBoardReplyList.do");
		
		List<BoardReplyVO> board=null;
		if(count > 0) {
			board = dao.getListReplyBoard(page.getStartRow(), page.getEndRow(), keyfield, keyword);
		}
		request.setAttribute("count", count);
		request.setAttribute("board", board);
		request.setAttribute("page", page.getPage());
		
		//jsp 경로
		return "board/adminBoardReplyList.jsp";
	}

}

