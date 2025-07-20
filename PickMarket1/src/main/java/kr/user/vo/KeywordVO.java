package kr.user.vo;

public class KeywordVO {
	// 최근 검색어
	private long k_num;
	private long user_num;
	private String k_word;
	
	public long getK_num() {
		return k_num;
	}
	public void setK_num(long k_num) {
		this.k_num = k_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getK_word() {
		return k_word;
	}
	public void setK_word(String k_word) {
		this.k_word = k_word;
	}
	
}
