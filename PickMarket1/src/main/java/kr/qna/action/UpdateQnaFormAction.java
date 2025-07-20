package kr.qna.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;
import kr.util.StringUtil;

public class UpdateQnaFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		long qna_num = Long.parseLong(request.getParameter("qna_num"));
		QnaDAO dao = QnaDAO.getInstance();
		QnaVO qna = dao.getQna(qna_num);
		if(user_num != qna.getUser_num()) {
			return "common/accessDenied.jsp";
		}
		
		qna.setQna_title(StringUtil.parseQuot(qna.getQna_title()));
		qna.setQna_num(qna_num);
		
		request.setAttribute("qna", qna);
		
		return "qna/updateQnaForm.jsp";
	}
}
