package kr.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.product.vo.CategoryVO;
import kr.util.DBUtil;

public class CategoryDAO {
	//싱글턴 패턴
	private static CategoryDAO instance = new CategoryDAO();
	
	public static CategoryDAO getInstance() {
		return instance;
	}
	private CategoryDAO() {}
	
	//카테고리 중복 체크
	public boolean checkUniqueInfo(String category_name) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM category WHERE category_name=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, category_name);
						
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true; //category 중복
			} // if
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false; //category 미중복
	} //checkUniqueInfo
		
	//TODO 카테고리 등록
	public void insertCategory(CategoryVO category) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO category (category_num, category_name) VALUES (category_seq.nextval,?)";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, category.getCategory_name());
			//SQL문 실행
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //insertCategory
	
	//TODO 카테고리 상세 (카테고리 삭제시 카테고리 번호 체크 용도로 사용)
	public CategoryVO getCategory(long category_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CategoryVO category = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM category WHERE category_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, category_num);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				category = new CategoryVO();
				category.setCategory_num(rs.getLong("category_num"));
			} // if
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return category;
	} //getCategory
	
	//TODO 카테고리 상태 수정 (목록에서 바로 수정 가능)
	public void modifyCategoryStatus(long category_num, int category_status) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();			
			//SQL문 작성
			sql = "UPDATE category SET category_status=? WHERE category_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, category_status);
			pstmt.setLong(2, category_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		
	} //modifyCategoryStatus
	
	//카테고리 삭제 (목록에서 바로 삭제 가능)
	public void deleteCategory(long category_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			//TODO: 상품 삭제 먼저 진행후 가능하다는 알림 띄우기..?
			//SQL문 작성
			sql = "DELETE FROM category WHERE category_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, category_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //deleteCategory
	
	//TODO 전체 카테고리 개수/검색 카테고리 개수
	public int getCategoryCount(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
		
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE category_name LIKE '%' || ? || '%'";				
			}//if
			
			//SQL문 작성
			sql = "SELECT COUNT(*) FROM category " + sub_sql;
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);
			}//if
			
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			} // if
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return count;
	} //getCategoryCount
	
	//TODO 전체 카테고리 목록/검색 카테고리 목록
	public List<CategoryVO> getListCategory(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CategoryVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE category_name LIKE '%' || ? || '%'";				
			}//if
		
			//SQL문 작성
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM category "
					+ sub_sql + " ORDER BY category_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <=?";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}//if
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<CategoryVO>();
			while (rs.next()) {				
				CategoryVO category = new CategoryVO();
				category.setCategory_num(rs.getLong("category_num"));
				category.setCategory_name(rs.getString("category_name"));
				category.setCategory_status(rs.getInt("category_status"));
				
				list.add(category);
			} // while
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return list;
	} //getListCategory
	
	//TODO 전체 카테고리(등록 폼에 카테고리 선택지 뿌려주기 위함.)
	public List<CategoryVO> getAllCategories() throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CategoryVO> categoryList = null;
		String sql = null;
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
		
			//SQL문 작성
			sql = "SELECT * FROM category WHERE CATEGORY_STATUS = 1 ORDER BY category_num ";
			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			//SQL문 실행
			rs = pstmt.executeQuery();
			categoryList = new ArrayList<CategoryVO>();
			while (rs.next()) {				
				CategoryVO category = new CategoryVO();
				category.setCategory_num(rs.getLong("category_num"));
				category.setCategory_name(rs.getString("category_name"));
				category.setCategory_status(rs.getInt("category_status"));
				
				categoryList.add(category);
			} // while
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return categoryList;
		
	} //getAllCategory
	
	// 카테고리 숨기기X 만 레코드
	public List<CategoryVO> getCategoryStatus(int category_status) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    List<CategoryVO> list = new ArrayList<>();
	    
	    try {
	        conn = DBUtil.getConnection();
	        sql = "SELECT * FROM category WHERE category_status=?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, category_status);
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
				CategoryVO category = new CategoryVO();
				category.setCategory_num(rs.getLong("category_num"));
				category.setCategory_name(rs.getString("category_name"));

				list.add(category);
	        }
	    }catch (Exception e) {
	    	throw new Exception(e);
	    } finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }
	    return list;
	}
	
} //class
