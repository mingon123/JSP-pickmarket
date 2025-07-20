package kr.notification.vo;

import java.sql.Date;

public class NotificationVO {
	private long notifi_num;
	private long user_num;
	private String message;
	private int is_read;
	private Date created_date;
	private String type;
	private long product_num;
	private long opponent_num;
	
	
	
	public long getOpponent_num() {
		return opponent_num;
	}
	public void setOpponent_num(long opponent_num) {
		this.opponent_num = opponent_num;
	}
	public long getProduct_num() {
		return product_num;
	}
	public void setProduct_num(long product_num) {
		this.product_num = product_num;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getNotifi_num() {
		return notifi_num;
	}
	public void setNotifi_num(long notifi_num) {
		this.notifi_num = notifi_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	
	
}
