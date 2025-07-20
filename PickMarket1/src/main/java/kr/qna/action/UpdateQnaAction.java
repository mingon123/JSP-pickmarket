package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;

public class UpdateQnaAction implements Action{
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
		
		QnaVO qna = new QnaVO();
		qna.setQna_num(qna_num);
		qna.setQna_title(request.getParameter("qna_title"));
		qna.setQna_content(request.getParameter("qna_content"));
		qna.setUser_num(user_num);
		
		dao.updateQna(qna);
		
		return "redirect:/qna/qnaDetail.do?qna_num="+qna_num;
	}
}
