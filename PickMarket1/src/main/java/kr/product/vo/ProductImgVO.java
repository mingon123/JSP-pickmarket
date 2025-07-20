package kr.product.vo;

public class ProductImgVO {
	private long product_num; //중고상품 번호
	private String filename; //파일명
	
	//getters and setters 생성
	public long getProduct_num() {
		return product_num;
	}
	public void setProduct_num(long product_num) {
		this.product_num = product_num;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
} //class
