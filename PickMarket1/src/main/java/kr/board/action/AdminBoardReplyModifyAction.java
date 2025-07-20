/*package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class AdminBoardReplyModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		Integer user_auth = (Integer)session.getAttribute("auth");
		if(user_auth != 9) {//로그인이 되지 않은 경우
			return "common/accessDenied.jsp";
		}
		//관리자로 로그인 한 경우
		long board_num = Long.parseLong(request.getParameter("board_num"));
		//DB에 저장된 정보 읽기
		BoardDAO dao = BoardDAO.getInstance();
		BoardReplyVO db_board = dao.getReplyBoard(board_num);
		
		//전송된 정보 저장
		BoardReplyVO board = new BoardReplyVO();
		board.setBoard_num(board_num);
		board.setUser_num(board_num);
		board.setBreply_ip(request.getRemoteAddr());
		board.setBreply_content(request.getParameter("breply_content"));
		board.setBreply_modidate(null);
		board.setBreply_date(null);
			
		dao.updateReplyBoard(board);

		return "redirect:/board/adminBoardReplyDetail.do?board_num="+board_num;
		
			}

		}

*/
/*package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class AdminBoardModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		Integer user_auth = (Integer)session.getAttribute("auth");
		if(user_auth != 9) {//로그인이 되지 않은 경우
			return "common/accessDenied.jsp";
		}
		//관리자로 로그인 한 경우
		long board_num = Long.parseLong(request.getParameter("board_num"));
		//DB에 저장된 정보 읽기
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO db_board = dao.getBoard(board_num);
        if (db_board == null) {
            return "common/accessDenied.jsp";
        }
		
		//전송된 정보 저장
		BoardVO board = new BoardVO();
		board.setBoard_num(board_num);
		board.setBhit(request.getContentLength());
		board.setBtitle(request.getParameter("btitle"));
		board.setBcontent(request.getParameter("bcontent"));
		board.setNickname(request.getParameter("nickname"));
		board.setBfilename(FileUtil.uploadFile(request, "bfilename"));
		board.setBmodi_date(null);
		board.setBreg_date(null);
			
		dao.updateBoard(board);
		
		//새파일로 교체할 때 원래 파일 제거
				if(board.getBfilename()!=null && !"".equals(board.getBfilename())) {
					FileUtil.removeFile(request, db_board.getBfilename());
				}
	
		return "redirect:/board/adminBoardDetail.do?board_num="+board_num;
		
			}

		}
		*/
package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.controller.Action;

	public class AdminBoardReplyModifyAction implements Action{

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			HttpSession session = request.getSession();
			Long user_num = (Long)session.getAttribute("user_num");
			if(user_num == null) {//로그인이 되지 않은 경우
				return "redirect:/user/loginForm.do";
			}
			Integer user_auth = (Integer)session.getAttribute("user_auth");
			if(user_auth != 9) {//로그인이 되지 않은 경우
				return "common/accessDenied.jsp";
			}
			//관리자로 로그인 한 경우
			long breply_num = Long.parseLong(request.getParameter("breply_num"));
			//DB에 저장된 정보 읽기
			BoardDAO dao = BoardDAO.getInstance();
			BoardReplyVO db_board = dao.getReplyBoard(breply_num);
			
			//전송된 정보 저장
			BoardReplyVO board = new BoardReplyVO();
			board.setBreply_num(breply_num);
			board.setBreply_ip(request.getRemoteAddr());
			board.setNickname(request.getParameter("nickname"));
			board.setBreply_content(request.getParameter("breply_content"));
			board.setBreply_modidate(null);
			board.setBreply_date(null);
					
			dao.updateReplyBoard(board);
				
			System.out.println("!!!!!!!!!!");

			return "redirect:/board/adminBoardReplyDetail.do?breply_num=" + breply_num;
	    }
	}
		
		