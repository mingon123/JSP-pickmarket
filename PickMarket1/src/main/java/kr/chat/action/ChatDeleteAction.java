package kr.chat.action;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatDAO;
import kr.chat.vo.ProductChatVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class ChatDeleteAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long chat_num = Long.parseLong(request.getParameter("chat_num"));
		Map<String, String> mapAjax = new HashMap<String, String>();
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		ProductChatDAO dao = ProductChatDAO.getInstance();
        ProductChatVO chat = dao.getChatByNum(chat_num);
		
        if(chat == null || chat.getUser_num() != user_num) {
        	mapAjax.put("result", "unauthorized");
        } else {
        	String chatDateStr = chat.getChat_date(); // 예: "2025-04-29 18:10:00"

        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        	LocalDateTime chatDate = LocalDateTime.parse(chatDateStr, formatter);
        	LocalDateTime now = LocalDateTime.now();

        	long minutes = ChronoUnit.MINUTES.between(chatDate, now);

            if(minutes > 5) {
            	mapAjax.put("result", "timeout");
            } else {
                dao.deleteMessageChat(chat);
                mapAjax.put("result", "success");
            }
        }
		return StringUtil.parseJSON(request, mapAjax);
	}

}
