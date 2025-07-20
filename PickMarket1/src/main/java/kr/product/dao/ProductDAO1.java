package kr.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.product.vo.ProductVO;
import kr.util.DBUtil;
import kr.util.DurationFromNow;
import kr.util.StringUtil;

public class ProductDAO1 {
	private static ProductDAO1 instance = new ProductDAO1();
	
	public static ProductDAO1 getInstance() {
		return instance;
	}
	private ProductDAO1() {}
	
	//TODO: 나중에 ProductDAO로 옮기기
	
	//메인용 최신 상품 N개 - 검색 없음, 로그인 여부에 따라 숨김/차단 필터링만 적용
	public List<ProductVO> getRecentProduct(int count, long user_num) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<ProductVO> list = new ArrayList<>();
	    int cnt = 0;
	    
	    String sql = "SELECT p.*, c.category_name, l.region_nm FROM product p "
	               + "JOIN category c ON p.category_num = c.category_num "
	               + "JOIN location l ON p.region_cd = l.region_cd "
	               + "WHERE p.state != 2 "       	 // 판매완료 제외
	               + "AND p.hide_status != 1 "       // 숨김 상품 제외
	               + "AND c.category_status != 0 ";  // 비활성 카테고리 제외

	    if (user_num != 0) {
	        // 로그인 사용자일 경우: 차단유저 필터링
	        sql += "AND p.user_num NOT IN (SELECT blocked_num FROM block WHERE user_num = ?) ";
	    }

	    sql += "ORDER BY NVL(p.up_date, p.reg_date) DESC, p.product_num DESC "
	         + "FETCH FIRST ? ROWS ONLY"; // 최신 6개

	    try {
	        conn = DBUtil.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        
	        if (user_num != 0) {
	            pstmt.setLong(++cnt, user_num);
	        }
	        pstmt.setInt(++cnt, count);

	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            ProductVO product = new ProductVO();
	            product.setProduct_num(rs.getLong("product_num"));
	            product.setCategory_name(rs.getString("category_name"));
	            product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
	            product.setPrice(rs.getInt("price"));
	            product.setThumbnail_img(rs.getString("thumbnail_img"));
	            product.setState(rs.getInt("state"));
	            product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
	            String upDate = rs.getString("up_date");
	            if (upDate != null) {
	                product.setUp_date(DurationFromNow.getTimeDiffLabel(upDate));
	            }
	            product.setRegion_cd(rs.getString("region_cd"));
	            String name = rs.getString("region_nm");
	            String[] sub_name = name.split(" ");
	            product.setRegion_nm(sub_name[sub_name.length - 1]);
	            
	            list.add(product);
	        }
	    } catch (Exception e) {
			throw new Exception(e);
		} finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }
	    return list;
	} //getRecentProduct
	
	// 메인용 - 가장 많이 본 상품 / 가장 많이 찜한 상품 상품번호
	public int getTopProductNum(String type, long userNum) throws Exception {
	    int productNum = 0;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    
	    try {
	        conn = DBUtil.getConnection();
	        if ("view".equals(type)) {
		        sql = "SELECT p.product_num FROM product p "
		            + "WHERE p.state != 2 AND p.hide_status != 1 "
		            + "AND p.category_num IN (SELECT category_num FROM category WHERE category_status != 0) "
		            + "AND p.user_num NOT IN (SELECT blocked_num FROM block WHERE user_num = ?) "
		            + "ORDER BY p.hit DESC, p.product_num DESC "
		            + "FETCH FIRST 1 ROWS ONLY";
		    } else if ("fav".equals(type)) {
		        sql = "SELECT p.product_num FROM product p "
		            + "WHERE p.state != 2 AND p.hide_status != 1 "
		            + "AND p.category_num IN (SELECT category_num FROM category WHERE category_status != 0) "
		            + "AND p.user_num NOT IN (SELECT blocked_num FROM block WHERE user_num = ?) "
		            + "ORDER BY (SELECT COUNT(*) FROM product_fav f WHERE f.product_num = p.product_num) DESC, "
		            + "p.product_num DESC "
		            + "FETCH FIRST 1 ROWS ONLY";
		    } 
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, userNum);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            productNum = rs.getInt("product_num");
	        }
	    } catch (Exception e) {
			throw new Exception(e);
		} finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }

	    return productNum;
	} //getTopProductNum
	
} //class
