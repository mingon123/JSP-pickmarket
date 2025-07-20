package kr.chat.action;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.chat.dao.ProductChatRoomDAO;
import kr.controller.Action;
import kr.util.StringUtil;


public class SetDealAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> mapAjax = new HashMap<>();

        try {
            long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));
            String datetimeStr = request.getParameter("deal_datetime");
            double deal_x_loc = Double.parseDouble(request.getParameter("deal_x_loc"));
			double deal_y_loc = Double.parseDouble(request.getParameter("deal_y_loc"));

            // 날짜와 시간 파싱
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr, formatter);
            Timestamp dealTimestamp = Timestamp.valueOf(localDateTime);

            ProductChatRoomDAO dao = ProductChatRoomDAO.getInstance();
            dao.updateDealInfo(chatroom_num, dealTimestamp, deal_x_loc, deal_y_loc);
            mapAjax.put("result", "success");
            
        } catch (Exception e) {
        	e.printStackTrace(); 
            mapAjax.put("result", "error");
        }

        return StringUtil.parseJSON(request, mapAjax);
	}

}
 
