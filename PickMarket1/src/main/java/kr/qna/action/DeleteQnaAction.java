package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;

public class DeleteQnaAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		long qna_num = Long.parseLong(request.getParameter("qna_num"));
		QnaDAO dao = QnaDAO.getInstance();
		QnaVO db_qna = dao.getQna(qna_num);
		if(user_num != db_qna.getUser_num()) {
			return "common/accessDenied.jsp";
		}
		
		dao.deleteQna(qna_num);
		
		request.setAttribute("notice_msg", "질문 삭제 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/qna/qnaList.do");

		return "common/alert_view.jsp";
	}
}
