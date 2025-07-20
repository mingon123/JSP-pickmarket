package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;

public class WriteQnaAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		QnaVO qna = new QnaVO();
		qna.setQna_title(request.getParameter("qna_title"));
		qna.setQna_content(request.getParameter("qna_content"));
		qna.setUser_num(user_num);
		
		QnaDAO dao = QnaDAO.getInstance();
		dao.insertQna(qna);
		
		request.setAttribute("notice_msg", "질문등록 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/qna/qnaList.do");
		
		return "common/alert_view.jsp";
	}
}
