package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.KeywordVO;

public class InsertKeywordAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) { 
			return "redirect:/user/loginForm.do";
		}
		
		KeywordVO keyword = new KeywordVO();
		keyword.setUser_num(user_num);
		keyword.setK_word(request.getParameter("k_word"));
			
		UserDAO dao = UserDAO.getInstance();
		
        // 키워드 중복 체크
        if (dao.isKeywordExists(user_num, keyword.getK_word())) {
            request.setAttribute("notice_msg", "이미 등록된 키워드입니다.");
            request.setAttribute("notice_url", request.getContextPath() + "/user/keywordList.do");
            return "common/alert_view.jsp";
        }
		
        // 키워드 개수 확인
	    int keywordCount = dao.getKeywordCount(user_num);
	    if (keywordCount >= 3) {
			request.setAttribute("notice_msg", "최대 3개의 키워드만 등록할 수 있습니다.");
			request.setAttribute("notice_url", request.getContextPath()+"/user/keywordList.do");
	    } else {
	        dao.insertKeyword(keyword);
	        request.setAttribute("notice_msg", "키워드가 추가되었습니다.");
			request.setAttribute("notice_url", request.getContextPath()+"/user/keywordList.do");
	    }

		return "common/alert_view.jsp";
	}
}
