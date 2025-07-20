package kr.qna.vo;

import java.sql.Date;

public class QnaVO {
	private long qna_num; // 문의번호
	private String qna_title; // 제목
	private String qna_content; // 내용
	private String qna_re; // 답변
	private Date q_date; // 작성일
	private Date a_date; // 답변일
	private long user_num; // 회원번호
	private String nickname;
	
	
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public long getQna_num() {
		return qna_num;
	}
	public void setQna_num(long qna_num) {
		this.qna_num = qna_num;
	}
	public String getQna_title() {
		return qna_title;
	}
	public void setQna_title(String qna_title) {
		this.qna_title = qna_title;
	}
	public String getQna_content() {
		return qna_content;
	}
	public void setQna_content(String qna_content) {
		this.qna_content = qna_content;
	}
	public String getQna_re() {
		return qna_re;
	}
	public void setQna_re(String qna_re) {
		this.qna_re = qna_re;
	}
	public Date getQ_date() {
		return q_date;
	}
	public void setQ_date(Date q_date) {
		this.q_date = q_date;
	}
	public Date getA_date() {
		return a_date;
	}
	public void setA_date(Date a_date) {
		this.a_date = a_date;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	
}
