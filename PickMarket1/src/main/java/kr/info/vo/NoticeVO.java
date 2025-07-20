package kr.info.vo;

import java.sql.Date;

public class NoticeVO {
	private long noti_num;
	private String noti_title;
	private String noti_content;
	private long noti_view;
	private Date noti_date;
	
	
	public long getNoti_num() {
		return noti_num;
	}
	public void setNoti_num(long noti_num) {
		this.noti_num = noti_num;
	}
	public String getNoti_title() {
		return noti_title;
	}
	public void setNoti_title(String noti_title) {
		this.noti_title = noti_title;
	}
	public String getNoti_content() {
		return noti_content;
	}
	public void setNoti_content(String noti_content) {
		this.noti_content = noti_content;
	}
	public long getNoti_view() {
		return noti_view;
	}
	public void setNoti_view(long noti_view) {
		this.noti_view = noti_view;
	}
	public Date getNoti_date() {
		return noti_date;
	}
	public void setNoti_date(Date noti_date) {
		this.noti_date = noti_date;
	}
	
}
