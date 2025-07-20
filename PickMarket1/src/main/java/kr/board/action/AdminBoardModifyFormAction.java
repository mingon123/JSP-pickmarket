package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class AdminBoardModifyFormAction implements Action{
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num =(Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth !=9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		//관리자로 로그인한 경우
		long board_num = Long.parseLong(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO board = dao.getBoard(board_num);
		
		request.setAttribute("board", board);
		if(board.getBfilename()!=null && !"".equals(board.getBfilename())) {
			FileUtil.removeFile(request, board.getBfilename());
		}
		request.setAttribute("notice_msg", "정상적으로 수정되었습니다");
		request.setAttribute("notice_url", request.getContextPath()+"/board/adminBoardModifyForm.do?board_num="+board_num);
		return "board/adminBoardModifyForm.jsp";
	}

}
