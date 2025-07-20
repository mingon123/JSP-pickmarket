/*package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

public class AdminBoardReplyModifyFormAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num =(Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("auth");
		if(user_auth !=9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		//관리자로 로그인한 경우
		// 댓글 번호 파라미터 받기
        String breplyNumParam = request.getParameter("breply_num");
        if (breplyNumParam == null || breplyNumParam.trim().isEmpty()) {
            return "common/errorPage.jsp"; // 댓글 번호가 없다면 에러 페이지로 리다이렉트
        }

        long breply_num = 0;
        try {
            breply_num = Long.parseLong(breplyNumParam);  // 댓글 번호
        } catch (NumberFormatException e) {
            return "common/errorPage.jsp"; // 잘못된 댓글 번호 형식 처리
        }

        // DB에서 댓글 정보 가져오기
        BoardDAO dao = BoardDAO.getInstance();
        BoardReplyVO board = dao.getReplyBoard(breply_num);  // 댓글 번호로 댓글 조회

        if (board == null) {
            return "common/errorPage.jsp";  // 댓글이 없으면 에러 페이지로 이동
        }

        // 댓글 정보를 request에 저장
        request.setAttribute("board", board);

        // 댓글 수정 폼 페이지로 리다이렉트
        return "board/adminBoardReplyModifyForm.jsp";
    }
}
*/
package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.controller.Action;

public class AdminBoardReplyModifyFormAction implements Action{
	
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
		long breply_num = Long.parseLong(request.getParameter("breply_num"));
		BoardDAO dao = BoardDAO.getInstance();
		BoardReplyVO board = dao.getReplyBoard(breply_num);
		request.setAttribute("board", board);
		
		request.setAttribute("notice_msg", "정상적으로 수정되었습니다");
		request.setAttribute("notice_url", request.getContextPath()+"/board/adminBoardReplyModifyForm.do?board_num="+breply_num);
		return "board/adminBoardReplyModifyForm.jsp";
	}

}
