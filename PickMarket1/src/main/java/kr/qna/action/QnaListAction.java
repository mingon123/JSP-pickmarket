package kr.qna.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.qna.dao.QnaDAO;
import kr.qna.vo.QnaVO;
import kr.util.PagingUtil;

public class QnaListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		QnaDAO dao = QnaDAO.getInstance();
		int count = dao.getQnaCountByUser(user_num);
		
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"qnaList.do");
				
		List<QnaVO> list = null;
		if(count > 0) {
			list = dao.getListQna(page.getStartRow(), page.getEndRow(), user_num);
		}
		
		
		request.setAttribute("count", count);
        request.setAttribute("list", list);
        request.setAttribute("page", page.getPage());
        request.setAttribute("startRow", page.getStartRow());

		return "qna/qnaList.jsp";
	}
}
