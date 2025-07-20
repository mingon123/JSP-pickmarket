package kr.chat.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.chat.dao.ProductChatRoomDAO;
import kr.chat.vo.ProductChatRoomVO;
import kr.controller.Action;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductVO;
import kr.review.dao.MannerRateDAO;
import kr.review.dao.ReviewDAO;
import kr.review.vo.MannerRateVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class ChatMessageAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		String chatroom_num = request.getParameter("chatroom_num");
		ProductChatRoomDAO roomDAO = ProductChatRoomDAO.getInstance();
		ProductDAO productDAO = ProductDAO.getInstance();
		UserDAO userDAO = UserDAO.getInstance();
		ProductChatRoomVO room;
		ProductVO product = null;
		
		// 채팅 시작은 구매자가 먼저 하므로, 여기서 저장되어있는 user_num은 구매자
		if(chatroom_num == null) { //채팅방 번호가 없는 경우
			long product_num = Long.parseLong(request.getParameter("product_num"));
			product = productDAO.getProduct(product_num);
			UserVO buyer = userDAO.getUser(user_num);
			UserVO seller = userDAO.getUser(product.getUser_num());
			room = roomDAO.checkChatRoom(product_num, user_num);
			
			if (product.getUser_num() == user_num) {
			    request.setAttribute("message", "자신의 상품에는 채팅을 생성할 수 없습니다.");
			    request.setAttribute("redirectUrl", request.getContextPath() + "/main/main.do");
			    return "/common/accessDenied.jsp";
			}
			if(room == null) { //개설된 채팅방이 없는 경우
				ProductChatRoomVO vo = new ProductChatRoomVO();
				vo.setProduct_num(product_num);
				vo.setSeller_num(product.getUser_num());
				vo.setBuyer_num(user_num);
				//채팅방 생성
				roomDAO.insertChatRoom(vo);
				//채팅방 생성 여부를 확인하고 생성된 채팅방 정보 반환
				room = roomDAO.checkChatRoom(product_num, user_num);
			}
			
		}else { //채팅방 번호가 있는 경우
			room = roomDAO.selectChatRoom(Long.parseLong(chatroom_num));
			if (room == null || (room.getBuyer_num() != user_num && room.getSeller_num() != user_num)) {
				request.setAttribute("message", "해당 채팅방에 접근할 수 없습니다.");
				request.setAttribute("redirectUrl", request.getContextPath() + "/main/main.do");
				return "/common/accessDenied.jsp";
			}
			product = productDAO.getProduct(room.getProduct_num());
		}
		
		//제목 HTML 태그 불허 - product에서 처리하므로 패스?
		//room.setTitle(StringUtil.useNoHtml(room.getTitle()));
		
		// 상대방 유저 번호 구하기
		long target_user;
		if (room.getBuyer_num() == user_num) {
		    target_user = room.getSeller_num();
		} else {
		    target_user = room.getBuyer_num();
		}
		
		// 매너 평가 상태 확인
		MannerRateDAO mannerDAO = MannerRateDAO.getInstance();
		int mannerCount = mannerDAO.getMannerCount(user_num, target_user);
		boolean isAlreadyRated = mannerCount > 0;
		UserVO targetUser = userDAO.getUser(target_user);
		request.setAttribute("isAlreadyRated", isAlreadyRated);
		request.setAttribute("targetUser", targetUser);
		
		request.setAttribute("product", product);
		request.setAttribute("productChatRoomVO", room);
		
		//후기 작성 상태 확인
		ReviewDAO reviewDAO = ReviewDAO.getInstance();
		int  reviewCount =  reviewDAO.checkReviewCount(user_num, target_user, product.getProduct_num());
		request.setAttribute("reviewCount", reviewCount);
		
		return "chat/chat_detail.jsp";
	}

}
