package kr.product.vo;

public class ProductFavVO {
	private long product_num;
	private long user_num;
	private String fav_date;
	private int alarm_flag; //default 0 (0:알림 허용, 1:알림 거부)
	
	private ProductVO productVO;

	//생성자 생성
	public ProductFavVO() {}
	
	public ProductFavVO(long product_num, long user_num) {
		this.product_num = product_num;
		this.user_num = user_num;
	}
	
	//getters and setters 생성	
	public long getProduct_num() {
		return product_num;
	}
	public void setProduct_num(long product_num) {
		this.product_num = product_num;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getFav_date() {
		return fav_date;
	}
	public void setFav_date(String fav_date) {
		this.fav_date = fav_date;
	}
	public int getAlarm_flag() {
		return alarm_flag;
	}
	public void setAlarm_flag(int alarm_flag) {
		this.alarm_flag = alarm_flag;
	}
	
	public ProductVO getProductVO() {
		return productVO;
	}
	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}
	
} //class
