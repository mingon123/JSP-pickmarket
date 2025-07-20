package kr.user.vo;

import java.sql.Date;

import kr.location.vo.LocationVO;

public class UserVO {
	private long user_num;
	private String id;
	private int auth;
	private int report_count;
	private String name;
	private String nickname;
	private String passwd;
	private String phone;
	private String photo;
	private Date reg_date;
	private Date modi_date;
	private long score;
	private String region_cd;
	
	private LocationVO locationVO;
	
	//비밀번호 일치 여부 체크
	public boolean isCheckedPassword(String userPasswd) {
		//회원 등급(auth) -> 0:탈퇴,1:정지,2:일반,9:관리자
		if(auth > 1 && passwd.equals(userPasswd)) {
			return true;
		}
		return false;
	}
	
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public int getReport_count() {
		return report_count;
	}
	public void setReport_count(int report_count) {
		this.report_count = report_count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Date getReg_date() {
		return reg_date;
	}
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	public Date getModi_date() {
		return modi_date;
	}
	public void setModi_date(Date modi_date) {
		this.modi_date = modi_date;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public String getRegion_cd() {
		return region_cd;
	}
	public void setRegion_cd(String region_cd) {
		this.region_cd = region_cd;
	}
	public LocationVO getLocationVO() {
		return locationVO;
	}
	public void setLocationVO(LocationVO locationVO) {
		this.locationVO = locationVO;
	}
	
}
