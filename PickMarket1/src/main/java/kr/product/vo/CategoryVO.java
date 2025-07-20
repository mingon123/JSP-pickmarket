package kr.product.vo;

public class CategoryVO {
	
	private long category_num; //카테고리 번호
	private String category_name; //카테고리 이름
	private int category_status; //카테고리 상태 (default 1/ 0:비활성,1:활성)
	
	//getters and setters 생성
	public long getCategory_num() {
		return category_num;
	}
	public void setCategory_num(long category_num) {
		this.category_num = category_num;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getCategory_status() {
		return category_status;
	}
	public void setCategory_status(int category_status) {
		this.category_status = category_status;
	}
	
} //class
