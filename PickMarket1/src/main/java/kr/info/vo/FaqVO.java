package kr.info.vo;

import java.sql.Date;

public class FaqVO {
	private long faq_num;
	private String faq_title;
	private String faq_content;
	private Date faq_date;
	private Date faq_modidate;
	
	
	public long getFaq_num() {
		return faq_num;
	}
	public void setFaq_num(long faq_num) {
		this.faq_num = faq_num;
	}
	public String getFaq_title() {
		return faq_title;
	}
	public void setFaq_title(String faq_title) {
		this.faq_title = faq_title;
	}
	public String getFaq_content() {
		return faq_content;
	}
	public void setFaq_content(String faq_content) {
		this.faq_content = faq_content;
	}
	public Date getFaq_date() {
		return faq_date;
	}
	public void setFaq_date(Date faq_date) {
		this.faq_date = faq_date;
	}
	public Date getFaq_modidate() {
		return faq_modidate;
	}
	public void setFaq_modidate(Date faq_modidate) {
		this.faq_modidate = faq_modidate;
	}
	
	
}
