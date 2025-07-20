package kr.chat.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.chat.dao.ProductChatDAO;
import kr.chat.dao.ProductChatRoomDAO;
import kr.chat.vo.ProductChatRoomVO;
import kr.controller.Action;
import kr.util.StringUtil;

public class GetDealAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> mapAjax = new HashMap<>();

		try {
			long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));
			
			ProductChatRoomDAO dao = ProductChatRoomDAO.getInstance();
			ProductChatRoomVO vo = dao.selectChatRoom(chatroom_num); // 채팅방 정보 가져오기

			if (vo != null && vo.getDeal_datetime() != null) {
				Map<String, Object> deal = new HashMap<>();
			    deal.put("deal_datetime", vo.getDeal_datetime().toString());
			    deal.put("deal_x_loc", vo.getDeal_x_loc());
			    deal.put("deal_y_loc", vo.getDeal_y_loc());

				mapAjax.put("result", "success");
				mapAjax.put("deal", deal);
				
			} else {
				mapAjax.put("result", "nodata");
			}
		} catch (Exception e) {
			mapAjax.put("result", "error");
			e.printStackTrace();
		}

		return StringUtil.parseJSON(request, mapAjax);
	}

}
