package kr.board.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;

public class WriteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {//로그인이 되지 않은 경우
			return "redirect:/users/loginForm.do";
		}
		//로그인 된 경우
		BoardVO board = new BoardVO();
		board.setBtitle(request.getParameter("btitle"));
		board.setBcontent(request.getParameter("bcontent"));
		board.setBip(request.getRemoteAddr());
		board.setBfilename(FileUtil.uploadFile(request, "bfilename"));
		board.setUser_num(user_num);
		
		UserDAO userDAO = UserDAO.getInstance();
		UserVO user = userDAO.getUser(user_num);		
		board.setRegion_cd(user.getRegion_cd());
		
		userDAO.modifyTemperatureUser(user_num, 2);
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.insertBoard(board);
		
		request.setAttribute("notice_msg", "글쓰기 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/board/boardList.do");
		
		return "common/alert_view.jsp";
	}

}
