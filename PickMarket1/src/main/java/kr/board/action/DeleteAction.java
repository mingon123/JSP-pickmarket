package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class DeleteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = 
				(Long)session.getAttribute("user_num");
		if(user_num==null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		//로그인이 된 경우
		long board_num = Long.parseLong(
				         request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO db_board = dao.getBoard(board_num);
		if(user_num != db_board.getUser_num()) {
			//로그인한 회원번호와 작성자 회원번호가 불일치
			return "common/accessDenied.jsp";
		}
		
		//로그인한 회원번호와 작성자 회원번호 일치
		dao.deleteBoard(board_num);
		//파일 삭제
		FileUtil.removeFile(request, db_board.getBfilename());
		
		request.setAttribute("notice_msg", "글 삭제 완료");
		request.setAttribute("notice_url", 
				  request.getContextPath()+"/board/boardList.do");
		
		return "common/alert_view.jsp";
	}

}





