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
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.FileUtil;

public class BoardModifyAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}

		long board_num = Long.parseLong(request.getParameter("board_num"));
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO db_board = dao.getBoard(board_num);

		// 새로 업로드된 파일
		String filename = FileUtil.uploadFile(request, "bfilename");
		// 기존 파일
		String original = db_board.getBfilename();
		// 삭제 여부
		String deleteFlag = request.getParameter("delete_image");

		//전송된 정보 저장
		BoardVO board = new BoardVO();
		board.setBoard_num(board_num);
		board.setBtitle(request.getParameter("btitle"));
		board.setBcontent(request.getParameter("bcontent"));
		board.setBip(request.getRemoteAddr());
		if ("true".equals(deleteFlag)) {
			// 사용자가 X 눌러서 삭제한 경우
			FileUtil.removeFile(request, original);
			board.setBfilename(null);
		} else if (filename != null && !"".equals(filename)) {
			// 새로 업로드된 파일이 있는 경우
			FileUtil.removeFile(request, original);
			board.setBfilename(filename);
		} else {
			// 기존 파일 유지
			board.setBfilename(original);
		}

		dao.updateBoard(board);
		
		request.setAttribute("notice_msg", "수정이 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/board/boardDetail.do?board_num="+board_num);
		
		return "common/alert_view.jsp";
	}
}


