package kr.chat.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatDAO;
import kr.chat.vo.ProductChatVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class ChatMessageListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> mapAjax = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			mapAjax.put("result", "logout");
			
		}else { //로그인 된 경우
			
			long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));
			System.out.println("chatroom_num : " + chatroom_num);
			ProductChatDAO dao = ProductChatDAO.getInstance();
			dao.updateReadStatus(user_num, chatroom_num);
			List<ProductChatVO> list = dao.getChatDetail(user_num, chatroom_num);
			if(list == null) list = Collections.emptyList();
			
			mapAjax.put("result", "success");
			mapAjax.put("list", list);
		}
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}

}
