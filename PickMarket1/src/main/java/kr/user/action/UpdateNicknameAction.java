package kr.user.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.util.StringUtil;

public class UpdateNicknameAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,String> mapAjax = new HashMap<String, String>();
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		String id = (String)session.getAttribute("id");
		String nickname = request.getParameter("nickname");
		
		if(id == null && user_num == null) {
			mapAjax.put("result", "logout");
		}else {
			if(user_num == null) {
				UserDAO user = UserDAO.getInstance();
				user_num = user.findUserNumByUserId(id);
			}
			
			UserDAO dao = UserDAO.getInstance();
			dao.updateNickname(user_num, nickname);
			
			mapAjax.put("result", "success");
		}
		
		
		
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}

}
