package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.block.dao.BlockDAO;
import kr.controller.Action;
import kr.info.dao.FaqDAO;
import kr.info.dao.NoticeDAO;
import kr.qna.dao.QnaDAO;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;

public class AdminQnaAnswerDeleteAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}

		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}

		Long qna_num = Long.parseLong(request.getParameter("qna_num"));

		QnaDAO dao = QnaDAO.getInstance();
		dao.deleteReplyQnaByAdmin(qna_num);
		
		request.setAttribute("notice_msg", "삭제가 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/qna/adminQnaList.do");

		return "common/alert_view.jsp";

	}
}
