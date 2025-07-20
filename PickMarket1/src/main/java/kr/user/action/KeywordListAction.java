package kr.user.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.KeywordVO;
import kr.util.PagingUtil;

public class KeywordListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		UserDAO dao = UserDAO.getInstance();
        List<KeywordVO> list = null;

        list = dao.getListKeyword(user_num);
        
        int count = list.size();
		
		request.setAttribute("count", count);
        request.setAttribute("list", list);
        
		return "user/keywordList.jsp";
	}
}
