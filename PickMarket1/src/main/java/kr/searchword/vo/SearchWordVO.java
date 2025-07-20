package kr.searchword.vo;

import java.sql.Date;

public class SearchWordVO {
	private long s_num;
	private long user_num;
	private String s_word;
	private Date s_date;
	private int keyfield;
	
	
	public int getKeyfield() {
		return keyfield;
	}
	public void setKeyfield(int keyfield) {
		this.keyfield = keyfield;
	}
	public long getS_num() {
		return s_num;
	}
	public void setS_num(long s_num) {
		this.s_num = s_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getS_word() {
		return s_word;
	}
	public void setS_word(String s_word) {
		this.s_word = s_word;
	}
	public Date getS_date() {
		return s_date;
	}
	public void setS_date(Date s_date) {
		this.s_date = s_date;
	}
	
	
	
}
