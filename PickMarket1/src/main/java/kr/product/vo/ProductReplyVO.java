package kr.product.vo;

public class ProductReplyVO {	
	private long reply_num;
	private String reply_content;
	private String reply_date;
	private String reply_modidate;
	private String reply_ip;
	private long product_num;	
	private long user_num;
	
	private String nickname;
	private String title;
	
	private ProductVO productVO;
	
	//getters and setters 생성
	public long getReply_num() {
		return reply_num;
	}
	public void setReply_num(long reply_num) {
		this.reply_num = reply_num;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public String getReply_date() {
		return reply_date;
	}
	public void setReply_date(String reply_date) {
		this.reply_date = reply_date;
	}
	public String getReply_modidate() {
		return reply_modidate;
	}
	public void setReply_modidate(String reply_modidate) {
		this.reply_modidate = reply_modidate;
	}
	public String getReply_ip() {
		return reply_ip;
	}
	public void setReply_ip(String reply_ip) {
		this.reply_ip = reply_ip;
	}
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
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ProductVO getProductVO() {
		return productVO;
	}
	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}
	
} //class
