package kr.board.vo;

import java.sql.Date;

public class BoardVO {
	private long board_num;
	private String btitle;
	private String bcontent;
	private int bhit;
	private String breg_date;
	private String bmodi_date;
	private String bfilename;
	private String bip;
	private long user_num;
	private String nickname;
	private String photo;
	private String region_cd;
	private String region_nm;
	
	private int reply_count;
	private int like_count;
	
	
	
	public String getBmodi_date() {
		return bmodi_date;
	}
	public void setBmodi_date(String bmodi_date) {
		this.bmodi_date = bmodi_date;
	}
	public String getBreg_date() {
		return breg_date;
	}
	public void setBreg_date(String breg_date) {
		this.breg_date = breg_date;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public int getLike_count() {
		return like_count;
	}
	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}
	public String getRegion_cd() {
		return region_cd;
	}
	public void setRegion_cd(String region_cd) {
		this.region_cd = region_cd;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public long getBoard_num() {
		return board_num;
	}
	public void setBoard_num(long board_num) {
		this.board_num = board_num;
	}
	public String getBtitle() {
		return btitle;
	}
	public void setBtitle(String btitle) {
		this.btitle = btitle;
	}
	public String getBcontent() {
		return bcontent;
	}
	public void setBcontent(String bcontent) {
		this.bcontent = bcontent;
	}
	public int getBhit() {
		return bhit;
	}
	public void setBhit(int bhit) {
		this.bhit = bhit;
	}
	public String getBfilename() {
		return bfilename;
	}
	public void setBfilename(String bfilename) {
		this.bfilename = bfilename;
	}
	public String getBip() {
		return bip;
	}
	public void setBip(String bip) {
		this.bip = bip;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getRegion_nm() {
		return region_nm;
	}
	public void setRegion_nm(String region_nm) {
		this.region_nm = region_nm;
	}
	
	
}
