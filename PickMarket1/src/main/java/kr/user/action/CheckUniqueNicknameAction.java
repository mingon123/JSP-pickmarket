package kr.user.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.util.StringUtil;

public class CheckUniqueNicknameAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nickname = request.getParameter("nickname");
		
		UserDAO dao = UserDAO.getInstance();
		boolean flag = dao.checkUniqueNickname(nickname);
		
		Map<String,String> mapAjax = new HashMap<String,String>();
		if(flag) {//중복
			mapAjax.put("result", "nicknameDuplicated");
		}else {//미중복
			mapAjax.put("result", "nicknameNotFound");
		}

		return StringUtil.parseJSON(request, mapAjax);
	}

}
