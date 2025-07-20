package kr.product.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatRoomDAO;
import kr.chat.vo.ProductChatRoomVO;
import kr.controller.Action;
import kr.notification.dao.NotificationDAO;
import kr.product.dao.ProductDAO;
import kr.user.dao.UserDAO;
import kr.util.StringUtil;

public class UpdateToSoldStateAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> mapAjax = new HashMap<String,String>();
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			mapAjax.put("request", "logout");
		}else {
			ProductDAO dao = ProductDAO.getInstance();
			long product_num = Long.parseLong(request.getParameter("product_num"));
			long chatroom_num = Long.parseLong(request.getParameter("chatroom_num"));
			dao.updateToSoldState(product_num);
			
			/*후기 알림 전송*/
			ProductChatRoomDAO chatroomDAO = ProductChatRoomDAO.getInstance();
	        ProductChatRoomVO room = chatroomDAO.selectChatRoom(chatroom_num);
			
	        if (room != null) {
	            Long buyer_num = room.getBuyer_num();
	            Long seller_num = room.getSeller_num();
	            String title = room.getProduct().getTitle();

	            String message = "'" + title + "' 거래가 완료되었습니다. 후기를 남겨보세요!";
	            NotificationDAO notiDAO = NotificationDAO.getInstance();
	            notiDAO.insertNotification(buyer_num, message, "review", product_num, seller_num);
	            notiDAO.insertNotification(seller_num, message, "review", product_num, buyer_num);
	            
	            UserDAO userDAO = UserDAO.getInstance();
	            userDAO.modifyTemperatureUser(buyer_num, 12);
	            userDAO.modifyTemperatureUser(seller_num, 12);
	        }
	        
			mapAjax.put("result", "success");
		}
		//JSON에 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
		
	}

}
