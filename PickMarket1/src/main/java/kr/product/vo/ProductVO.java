package kr.product.vo;

import java.util.List;

public class ProductVO {
	private long product_num; //중고상품 번호
	private String title; //제목
	private String content; //내용
	private int state; //게시글 상태 (default 0 / 0판매중, 1예약중, 2판매완료)
	private int hit; //조회수 (default 0)
	private String reg_date; //등록일 (default SYSDATE)
	private String modi_date; //내용 수정일
	private long user_num; //판매자
	private String ip;
	private int price; //상품가격
	private int up_count; //끌올 횟수(default 0 max 3)
	private String up_date; //끌올 날짜(등록일과 끌올 중 더 나중 날짜 이용 정렬)
	private long category_num; //카테고리 번호
	private double x_loc; //희망거래장소 위도
	private double y_loc; //희망거래장소 경도
	private String thumbnail_img; //썸네일 이미지
	private int hide_status; // default 0 / 0보이기 1숨기기
	
	private String nickname;
	private String category_name;
	private int category_status;
	
	private ProductImgVO productImgVO; //상품 관련 이미지
	private List<ProductImgVO> productImgList; // 상품 관련 이미지(여러장)
	
	private String region_nm; // 지역명
	private String region_cd; // 지역코드
	
	//getters and setters 생성	
	public ProductImgVO getProductImgVO() {
		return productImgVO;
	}
	public void setProductImgVO(ProductImgVO productImgVO) {
		this.productImgVO = productImgVO;
	}
	public List<ProductImgVO> getProductImgList() {
		return productImgList;
	}
	public void setProductImgList(List<ProductImgVO> productImgList) {
		this.productImgList = productImgList;
	}
	
	public long getProduct_num() {
		return product_num;
	}	
	public void setProduct_num(long product_num) {
		this.product_num = product_num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getModi_date() {
		return modi_date;
	}
	public void setModi_date(String modi_date) {
		this.modi_date = modi_date;
	}
	public long getUser_num() {
		return user_num;
	}
	public void setUser_num(long user_num) {
		this.user_num = user_num;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getUp_count() {
		return up_count;
	}
	public void setUp_count(int up_count) {
		this.up_count = up_count;
	}
	public String getUp_date() {
		return up_date;
	}
	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
	public long getCategory_num() {
		return category_num;
	}
	public void setCategory_num(long category_num) {
		this.category_num = category_num;
	}
	public double getX_loc() {
		return x_loc;
	}
	public void setX_loc(double x_loc) {
		this.x_loc = x_loc;
	}
	public double getY_loc() {
		return y_loc;
	}
	public void setY_loc(double y_loc) {
		this.y_loc = y_loc;
	}
	public String getThumbnail_img() {
		return thumbnail_img;
	}
	public void setThumbnail_img(String thumbnail_img) {
		this.thumbnail_img = thumbnail_img;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public int getHide_status() {
		return hide_status;
	}
	public void setHide_status(int hide_status) {
		this.hide_status = hide_status;
	}
	public String getRegion_nm() {
		return region_nm;
	}
	public void setRegion_nm(String region_nm) {
		this.region_nm = region_nm;
	}
	public String getRegion_cd() {
		return region_cd;
	}
	public void setRegion_cd(String region_cd) {
		this.region_cd = region_cd;
	}
	
} //class
