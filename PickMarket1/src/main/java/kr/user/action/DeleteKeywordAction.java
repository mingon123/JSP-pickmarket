package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.KeywordVO;

public class DeleteKeywordAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		String k_word = request.getParameter("k_word");
        if (k_word == null || k_word.isEmpty()) {
            return "redirect:/user/keywordList.do";
        }
		
		UserDAO dao = UserDAO.getInstance();
		KeywordVO keywordVO = new KeywordVO();
		keywordVO.setK_word(k_word);
		keywordVO.setUser_num(user_num);
		
		dao.deleteKeyword(keywordVO);
		
		request.setAttribute("notice_msg", "키워드 삭제 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/user/keywordList.do");
		
		return "common/alert_view.jsp";
	}
}
