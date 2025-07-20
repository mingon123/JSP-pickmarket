package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.block.dao.BlockDAO;
import kr.block.vo.BlockVO;
import kr.controller.Action;
import kr.info.dao.FaqDAO;
import kr.info.dao.NoticeDAO;
import kr.info.vo.FaqVO;
import kr.info.vo.NoticeVO;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class AdminQnaDetailAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}
		
		long qna_num = Long.parseLong(request.getParameter("qna_num"));
		
		QnaDAO dao = QnaDAO.getInstance();
		QnaVO qna = dao.getQnaDetailByAdmin(qna_num);
		
		request.setAttribute("qna", qna);
		
		return "qna/adminQnaDetail.jsp";
	}

}





