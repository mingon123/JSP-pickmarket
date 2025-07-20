package kr.review.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.product.vo.ProductVO;
import kr.review.vo.ReviewVO;
import kr.user.vo.UserVO;
import kr.util.DBUtil;
import kr.util.DurationFromNow;
import kr.util.StringUtil;

public class ReviewDAO {
	private static ReviewDAO instance = new ReviewDAO();
	public static ReviewDAO getInstance() {
		return instance;
	}
	private ReviewDAO() {}

	// 리뷰 작성
	public void insertReview(ReviewVO review)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO review (re_num,re_content,re_writer_num,re_getter_num,product_num) "
					+ "VALUES (review_seq.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, review.getRe_content());
			pstmt.setLong(2, review.getRe_writer_num());
			pstmt.setLong(3, review.getRe_getter_num());
			pstmt.setLong(4, review.getProduct_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 닉네임 -> 회원번호
	public Long getUserNumByNickname(String nickname) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Long user_num = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT user_num FROM users_detail WHERE nickname = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nickname);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user_num = rs.getLong("user_num");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return user_num;
	}
	
	// 내가 작성한/받은 리뷰레코드 확인 1:작성한리뷰 2:받은리뷰
	public int getReviewCount(String keyfield,String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "r.re_writer_num";
				else if(keyfield.equals("2")) sub_sql += "r.re_getter_num";
			}
			sql = "SELECT COUNT(*) FROM review r JOIN users_detail ud ON "+sub_sql+"=ud.user_num WHERE "+sub_sql+"=?";
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	
	// 내가 받은 총 리뷰 개수
	public int getTotalReviewCount(long re_getter_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
	    int count = 0;
		
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM review WHERE re_getter_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, re_getter_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 목록 1:작성한리뷰 2:받은리뷰
	public List<ReviewVO> getListReview(int start,int end,String keyfield,String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ReviewVO> list = null;
		String sql = null;
		String joinColumn = "";  // join 조건용 컬럼
		String whereColumn = ""; // where 조건용 컬럼
		int cnt = 0;		
		
		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					joinColumn = "r.re_getter_num"; 
					whereColumn = "r.re_writer_num";
				} else if (keyfield.equals("2")) {
					joinColumn = "r.re_writer_num";
					whereColumn = "r.re_getter_num";
				}
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
				+ "(SELECT r.*, ud.nickname, ud.photo FROM review r "
				+ "JOIN users_detail ud ON " + joinColumn + " = ud.user_num "
				+ "WHERE " + whereColumn + " = ? ORDER BY r.re_num DESC) a) "
				+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs = pstmt.executeQuery();
			list = new ArrayList<ReviewVO>();
			while (rs.next()) {
	            ReviewVO review = new ReviewVO();
	    		review.setRe_num(rs.getLong("re_num"));
				review.setRe_content(StringUtil.useNoHtml(rs.getString("re_content")));
				review.setRe_date(rs.getString("re_date"));
				review.setRe_writer_num(rs.getLong("re_writer_num"));
				review.setRe_getter_num(rs.getLong("re_getter_num"));
				review.setProduct_num(rs.getLong("product_num"));
	            
				UserVO userVO = new UserVO();
				userVO.setNickname(rs.getString("nickname"));
				userVO.setPhoto(rs.getString("photo"));
				review.setWriterVO(userVO);
				
	            list.add(review);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	//리뷰 작성을 했는지 확인
	public int checkReviewCount(long writer_num, long getter_num, long product_num) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    String sql = null;
	    ResultSet rs = null;
	    int count = 0;

	    try {
	        conn = DBUtil.getConnection();
	        sql = "SELECT COUNT(*) FROM review WHERE re_writer_num = ? AND re_getter_num = ? AND product_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, writer_num);
	        pstmt.setLong(2, getter_num);
	        pstmt.setLong(3, product_num);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }
	    return count;
	}

	// 리뷰 작성했는 지 레코드 확인
	public boolean isReviewWritten(long writer_num, long getter_num, long product_num) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    boolean review = false;
	    
	    try {
	        conn = DBUtil.getConnection();
	        sql = "SELECT re_num FROM review WHERE re_writer_num=? AND re_getter_num=? AND product_num=?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, writer_num);
	        pstmt.setLong(2, getter_num);
	        pstmt.setLong(3, product_num);
	        rs = pstmt.executeQuery();
	        if(rs.next()) {
	        	review = true;
	        }
	    } finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }
	    return review;
	}
	
	// 관리자
	// 관리자 카운트 가져옴
	public int getAdminReviewCount(String keyfield, String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE ud1.nickname LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "WHERE ud2.nickname LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "WHERE r.re_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM review r "
				+ "JOIN users_detail ud1 ON r.re_writer_num = ud1.user_num "
				+ "JOIN users_detail ud2 ON r.re_getter_num = ud2.user_num " + sub_sql;
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	
	// 관리자 목록
	public List<ReviewVO> getAdminListReview(int start, int end, String keyfield, String keyword)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ReviewVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;		
		
		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE ud1.nickname LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "WHERE ud2.nickname LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "WHERE r.re_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
				+ "(SELECT r.*, "
				+ "ud1.nickname AS writer_nickname, ud2.nickname AS getter_nickname "
				+ "FROM review r "
				+ "JOIN users_detail ud1 ON r.re_writer_num = ud1.user_num "
				+ "JOIN users_detail ud2 ON r.re_getter_num = ud2.user_num "
				+ sub_sql
				+ " ORDER BY r.re_num DESC) a) "
				+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs = pstmt.executeQuery();
			
			list = new ArrayList<ReviewVO>();
			while (rs.next()) {
				ReviewVO review = new ReviewVO();
				review.setRe_num(rs.getLong("re_num"));
				review.setRe_content(StringUtil.useNoHtml(rs.getString("re_content")));
				review.setRe_date(rs.getString("re_date"));
				review.setRe_writer_num(rs.getLong("re_writer_num"));
				review.setRe_getter_num(rs.getLong("re_getter_num"));
				review.setProduct_num(rs.getLong("product_num"));
	            
				// 작성자 정보
				UserVO writer = new UserVO();
				writer.setNickname(rs.getString("writer_nickname"));
				review.setWriterVO(writer);

				// 피작성자 정보
				UserVO getter = new UserVO();
				getter.setNickname(rs.getString("getter_nickname"));
				review.setGetterVO(getter);
				
	            list.add(review);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	// 관리자 - 상세
	public ReviewVO getReviewDetailByAdmin(long re_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ReviewVO review = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT r.*,p.*, ud1.nickname AS writer_nickname, ud2.nickname AS getter_nickname "
				+ "FROM review r "
				+ "JOIN users_detail ud1 ON r.re_writer_num = ud1.user_num "
				+ "JOIN users_detail ud2 ON r.re_getter_num = ud2.user_num "
				+ "JOIN product p ON p.product_num = r.product_num "
				+ "WHERE r.re_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, re_num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				review = new ReviewVO();
				review.setRe_num(rs.getLong("re_num"));
				review.setRe_content(StringUtil.useNoHtml(rs.getString("re_content")));
				review.setRe_date(rs.getString("re_date"));
				review.setRe_writer_num(rs.getLong("re_writer_num"));
				review.setRe_getter_num(rs.getLong("re_getter_num"));
				review.setProduct_num(rs.getLong("product_num"));
	            
				// 작성자 정보
				UserVO writer = new UserVO();
				writer.setNickname(rs.getString("writer_nickname"));
				review.setWriterVO(writer);

				// 피작성자 정보
				UserVO getter = new UserVO();
				getter.setNickname(rs.getString("getter_nickname"));
				review.setGetterVO(getter);
				
				// 상품
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("title"));
				product.setContent(rs.getString("content"));
				review.setProductVO(product);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return review;		
	}
	
	// 관리자 - 리뷰 수정
	public void updateReviewByAdmin(ReviewVO review) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE review SET re_content=? WHERE re_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, review.getRe_content());
			pstmt.setLong(2, review.getRe_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 관리자 - 리뷰 삭제
	public void deleteReviewByAdmin(long review_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM review WHERE re_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, review_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	
}
