package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatDAO;
import kr.chat.vo.ProductChatVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class AdminChatDeleteAction implements Action{

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
		
		long chat_num = Long.parseLong(request.getParameter("chat_num"));
		Map<String, String> mapAjax = new HashMap<String, String>();
		
		ProductChatDAO dao = ProductChatDAO.getInstance();
        ProductChatVO chat = dao.getChatByNum(chat_num);
		
        dao.deleteMessageChat(chat);
        dao.adminDeleteChat(chat_num);
        mapAjax.put("result", "success");
        
		return StringUtil.parseJSON(request, mapAjax);
	}

}
