package kr.report.vo;

import java.sql.Date;

import kr.user.vo.UserVO;

public class ReportVO {
	private long report_num;
	private long reporter_num;
	private long suspect_num;
	private String report_content;
	private String report_img;
	private String report_title;
	private Date report_date;
	
	private String nickname;
	private String reporter_nickname;
	
	
	
	public String getReporter_nickname() {
		return reporter_nickname;
	}
	public void setReporter_nickname(String reporter_nickname) {
		this.reporter_nickname = reporter_nickname;
	}
	public long getReport_num() {
		return report_num;
	}
	public void setReport_num(long report_num) {
		this.report_num = report_num;
	}
	public long getReporter_num() {
		return reporter_num;
	}
	public void setReporter_num(long reporter_num) {
		this.reporter_num = reporter_num;
	}
	public long getSuspect_num() {
		return suspect_num;
	}
	public void setSuspect_num(long suspect_num) {
		this.suspect_num = suspect_num;
	}
	public String getReport_content() {
		return report_content;
	}
	public void setReport_content(String report_content) {
		this.report_content = report_content;
	}
	public String getReport_img() {
		return report_img;
	}
	public void setReport_img(String report_img) {
		this.report_img = report_img;
	}
	public String getReport_title() {
		return report_title;
	}
	public void setReport_title(String report_title) {
		this.report_title = report_title;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	
}
