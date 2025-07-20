package kr.chat.vo;

import java.sql.Date;
import java.sql.Timestamp;

import kr.product.vo.ProductVO;
import kr.user.vo.UserVO;

public class ProductChatRoomVO {
	private long chatroom_num; //채팅방 번호
	private long product_num; //중고상품 번호
	private long seller_num; //작성자(판매자) 회원번호
	private long buyer_num; //구매 희망자 회원번호
	private Date room_date; //채팅방 생성일
	private Timestamp deal_datetime; //거래시간
	private double deal_x_loc; //거래장소 x좌표
	private double deal_y_loc; //거래장소 y좌표
	
	private ProductVO product;    // 상품 정보
    private UserVO seller;     // 판매자 정보
    private UserVO buyer;     // 구매자 정보
    private ProductChatVO lastChat; // 마지막 채팅 메시지
    
    private int unreadCount; //읽지 않은 메세지 수 총합
    
	
	
	public int getUnreadCount() {
		return unreadCount;
	}
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	public Timestamp getDeal_datetime() {
		return deal_datetime;
	}
	public void setDeal_datetime(Timestamp deal_datetime) {
		this.deal_datetime = deal_datetime;
	}
	public UserVO getSeller() {
		return seller;
	}
	public void setSeller(UserVO seller) {
		this.seller = seller;
	}
	public UserVO getBuyer() {
		return buyer;
	}
	public void setBuyer(UserVO buyer) {
		this.buyer = buyer;
	}
	public ProductVO getProduct() {
		return product;
	}
	public void setProduct(ProductVO product) {
		this.product = product;
	}
	public ProductChatVO getLastChat() {
		return lastChat;
	}
	public void setLastChat(ProductChatVO lastChat) {
		this.lastChat = lastChat;
	}
	public double getDeal_x_loc() {
		return deal_x_loc;
	}
	public void setDeal_x_loc(double deal_x_loc) {
		this.deal_x_loc = deal_x_loc;
	}
	public double getDeal_y_loc() {
		return deal_y_loc;
	}
	public void setDeal_y_loc(double deal_y_loc) {
		this.deal_y_loc = deal_y_loc;
	}
	public long getChatroom_num() {
		return chatroom_num;
	}
	public void setChatroom_num(long chatroom_num) {
		this.chatroom_num = chatroom_num;
	}
	public long getProduct_num() {
		return product_num;
	}
	public void setProduct_num(long product_num) {
		this.product_num = product_num;
	}
	public long getSeller_num() {
		return seller_num;
	}
	public void setSeller_num(long seller_num) {
		this.seller_num = seller_num;
	}
	public long getBuyer_num() {
		return buyer_num;
	}
	public void setBuyer_num(long buyer_num) {
		this.buyer_num = buyer_num;
	}
	public Date getRoom_date() {
		return room_date;
	}
	public void setRoom_date(Date room_date) {
		this.room_date = room_date;
	}
	
	
}
