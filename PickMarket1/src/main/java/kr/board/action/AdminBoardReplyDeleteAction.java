package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class AdminBoardReplyDeleteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session=request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
	Integer user_auth = (Integer)session.getAttribute("user_auth");
	if(user_auth != 9) {//관리자로 로그인하지 않은 경우
		return "common/accessDenied.jsp";
	}
	//관리자로 로그인 한 경우
	Long board_num = Long.parseLong(request.getParameter("board_num"));
	//DB에서 상품 정보 읽기
	BoardDAO dao = BoardDAO.getInstance();
	//BoardReplyVO db_board = dao.getReplyBoard(board_num);
	
		dao.deleteReplyBoard(board_num);
		request.setAttribute("notice_msg", "글 삭제 완료");
		request.setAttribute("notice_url", request.getContextPath() + "/board/adminBoardReplyList.do?board_num=" + board_num);
		return "common/alert_view.jsp";
	}

}
