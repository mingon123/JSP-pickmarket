package kr.block.vo;

import java.sql.Date;

public class BlockVO {
	private long blocker_num;
	private long blocked_num;
	private String block_content;
	private Date block_date;
	private int rnum; // 차단번호(순서)
	private String nickname;
	private String blocker_nickname;
	private String photo;
	
	
	public String getBlocker_nickname() {
		return blocker_nickname;
	}
	public void setBlocker_nickname(String blocker_nickname) {
		this.blocker_nickname = blocker_nickname;
	}
	public long getBlocker_num() {
		return blocker_num;
	}
	public void setBlocker_num(long blocker_num) {
		this.blocker_num = blocker_num;
	}
	public long getBlocked_num() {
		return blocked_num;
	}
	public void setBlocked_num(long blocked_num) {
		this.blocked_num = blocked_num;
	}
	public String getBlock_content() {
		return block_content;
	}
	public void setBlock_content(String block_content) {
		this.block_content = block_content;
	}
	public Date getBlock_date() {
		return block_date;
	}
	public void setBlock_date(Date block_date) {
		this.block_date = block_date;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getRnum() {
		return rnum;
	}
	public void setRnum(int rnum) {
		this.rnum = rnum;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
