package kr.review.vo;

import kr.product.vo.ProductVO;
import kr.user.vo.UserVO;

public class ReviewVO {
	private long re_num;
	private String re_content;
	private String re_date;
	private long re_writer_num; // 리뷰 작성자
	private long re_getter_num; // 리뷰 받은자
	private long product_num;
	
	private int count; // 리뷰 개수
	
	private UserVO userVO;
	private UserVO writerVO;
	private UserVO getterVO;
	
	private ProductVO productVO;
	
	public long getRe_num() {
		return re_num;
	}
	public void setRe_num(long re_num) {
		this.re_num = re_num;
	}
	public String getRe_content() {
		return re_content;
	}
	public void setRe_content(String re_content) {
		this.re_content = re_content;
	}
	public String getRe_date() {
		return re_date;
	}
	public void setRe_date(String re_date) {
		this.re_date = re_date;
	}
	public long getRe_writer_num() {
		return re_writer_num;
	}
	public void setRe_writer_num(long re_writer_num) {
		this.re_writer_num = re_writer_num;
	}
	public long getRe_getter_num() {
		return re_getter_num;
	}
	public void setRe_getter_num(long re_getter_num) {
		this.re_getter_num = re_getter_num;
	}
	public long getProduct_num() {
		return product_num;
	}
	public void setProduct_num(long product_num) {
		this.product_num = product_num;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public UserVO getWriterVO() {
		return writerVO;
	}
	public void setWriterVO(UserVO writerVO) {
		this.writerVO = writerVO;
	}
	public UserVO getGetterVO() {
		return getterVO;
	}
	public void setGetterVO(UserVO getterVO) {
		this.getterVO = getterVO;
	}
	public UserVO getUserVO() {
		return userVO;
	}
	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}
	public ProductVO getProductVO() {
		return productVO;
	}
	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}

	
}
