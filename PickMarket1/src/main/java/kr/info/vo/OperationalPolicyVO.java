package kr.info.vo;

import java.sql.Date;

public class OperationalPolicyVO {
	private long pol_num;
	private String pol_title;
	private String pol_content;
	private Date pol_date;
	private Date pol_modi_date;
	
	
	public long getPol_num() {
		return pol_num;
	}
	public void setPol_num(long pol_num) {
		this.pol_num = pol_num;
	}
	public String getPol_title() {
		return pol_title;
	}
	public void setPol_title(String pol_title) {
		this.pol_title = pol_title;
	}
	public String getPol_content() {
		return pol_content;
	}
	public void setPol_content(String pol_content) {
		this.pol_content = pol_content;
	}
	public Date getPol_date() {
		return pol_date;
	}
	public void setPol_date(Date pol_date) {
		this.pol_date = pol_date;
	}
	public Date getPol_modi_date() {
		return pol_modi_date;
	}
	public void setPol_modi_date(Date pol_modi_date) {
		this.pol_modi_date = pol_modi_date;
	}
	
	
	
}
