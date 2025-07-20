package kr.board.vo;

import java.sql.Date;

public class BoardReplyVO {
	private long breply_num;
	private String breply_content;
	private Date breply_date;
	private Date breply_modidate;
	private String breply_ip;
	private long board_num;
	private long user_num;
	
	private String nickname;
	private String btitle;
	
	private BoardVO boardVO;
	
	public String getBtitle() {
		return btitle;
	}
	public void setBtitle(String btitle) {
		this.btitle = btitle;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public long getBreply_num() {
		return breply_num;
	}
	public void setBreply_num(long breply_num) {
		this.breply_num = breply_num;
	}
	public String getBreply_content() {
		return breply_content;
	}
	public void setBreply_content(String breply_content) {
		this.breply_content = breply_content;
	}
	public Date getBreply_date() {
		return breply_date;
	}
	public void setBreply_date(Date breply_date) {
		this.breply_date = breply_date;
	}
	public Date getBreply_modidate() {
		return breply_modidate;
	}
	public void setBreply_modidate(Date breply_modidate) {
		this.breply_modidate = breply_modidate;
	}
	public String getBreply_ip() {
		return breply_ip;
	}
	public void setBreply_ip(String breply_ip) {
		this.breply_ip = breply_ip;
	}
	public long getBoard_num() {
		return board_num;
	}
	public void setBoard_num(long board_num) {
		this.board_num = board_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public BoardVO getBoardVO() {
		return boardVO;
	}
	public void setBoardVO(BoardVO boardVO) {
		this.boardVO = boardVO;
	}
	
}
