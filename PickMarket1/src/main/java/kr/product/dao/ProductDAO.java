package kr.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.product.vo.ProductFavVO;
import kr.product.vo.ProductImgVO;
import kr.product.vo.ProductReplyVO;
import kr.product.vo.ProductVO;
import kr.util.DBUtil;
import kr.util.DurationFromNow;
import kr.util.StringUtil;

public class ProductDAO {
	//싱글턴 패턴
	private static ProductDAO instance = new ProductDAO();

	public static ProductDAO getInstance() {
		return instance;
	}
	private ProductDAO() {}

	/*
	//사용자 - 상품 등록
	public void insertProduct(ProductVO product, List<ProductImgVO> productImgList) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		String sql = null;
		long product_num = 0L;
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);

			//product_num 구하기
			sql = "SELECT product_seq.nextval FROM dual";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				product_num = rs.getLong(1);
			} // if

			// 썸네일 이미지 파일명 미리 선정
			String thumbnailFilename = null;
			if (productImgList != null && !productImgList.isEmpty()) {
				thumbnailFilename = productImgList.get(0).getFilename(); // 첫 번째 이미지로 지정
			}

			//상품 정보 저장
			//SQL문 작성
			sql = "INSERT INTO product (product_num, title, content, user_num, ip, price, category_num, x_loc, y_loc, thumbnail_img, region_cd) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt2 = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt2.setLong(++cnt, product_num);
			pstmt2.setString(++cnt, product.getTitle());
			pstmt2.setString(++cnt, product.getContent());
			pstmt2.setLong(++cnt, product.getUser_num()); //TODO로그인 유저번호 읽어와야 함
			pstmt2.setString(++cnt, product.getIp());
			pstmt2.setInt(++cnt, product.getPrice());
			pstmt2.setLong(++cnt, product.getCategory_num());
			pstmt2.setDouble(++cnt, product.getX_loc());
			pstmt2.setDouble(++cnt, product.getY_loc());
			pstmt2.setString(++cnt, thumbnailFilename);
			pstmt2.setString(++cnt, product.getRegion_cd());

			//SQL문 실행
			pstmt2.executeUpdate();

			//상품 이미지 저장
			//SQL문 작성
			sql = "INSERT INTO product_img (product_num, filename) VALUES (?,?)";
			//PreparedStatement 객체 생성
			pstmt3 = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			for (int i = 0; i < productImgList.size(); i++) {
				ProductImgVO productImg = productImgList.get(i);
				pstmt3.setLong(1, product_num);
				pstmt3.setString(2, productImg.getFilename());
				pstmt3.addBatch(); //쿼리를 메모리에 올림

				//계속 추가하면 outOfMemory 발생, 1000개 단위로 executeBatch()
				if (i % 1000 == 0) {
					pstmt3.executeBatch();
				} // if
			} //for i	
			pstmt3.executeBatch(); //쿼리를 전송

			//모든 SQL문이 정상 수행
			conn.commit();	

		} catch (Exception e) {
			//하나라도 SQL문이 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
	} //insertProduct
	*/
	
	//사용자 - 상품 등록
	public long insertProduct(ProductVO product, List<ProductImgVO> productImgList) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		String sql = null;
		long product_num = 0L;
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);
			
			//product_num 구하기
			sql = "SELECT product_seq.nextval FROM dual";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				product_num = rs.getLong(1);
			} // if
			
			// 썸네일 이미지 파일명 미리 선정
			String thumbnailFilename = null;
			if (productImgList != null && !productImgList.isEmpty()) {
				thumbnailFilename = productImgList.get(0).getFilename(); // 첫 번째 이미지로 지정
			}
			
			//상품 정보 저장
			//SQL문 작성
			sql = "INSERT INTO product (product_num, title, content, user_num, ip, price, category_num, x_loc, y_loc, thumbnail_img, region_cd) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt2 = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt2.setLong(++cnt, product_num);
			pstmt2.setString(++cnt, product.getTitle());
			pstmt2.setString(++cnt, product.getContent());
			pstmt2.setLong(++cnt, product.getUser_num()); //TODO로그인 유저번호 읽어와야 함
			pstmt2.setString(++cnt, product.getIp());
			pstmt2.setInt(++cnt, product.getPrice());
			pstmt2.setLong(++cnt, product.getCategory_num());
			pstmt2.setDouble(++cnt, product.getX_loc());
			pstmt2.setDouble(++cnt, product.getY_loc());
			pstmt2.setString(++cnt, thumbnailFilename);
			pstmt2.setString(++cnt, product.getRegion_cd());
			
			//SQL문 실행
			pstmt2.executeUpdate();
			
			//상품 이미지 저장
			//SQL문 작성
			sql = "INSERT INTO product_img (product_num, filename) VALUES (?,?)";
			//PreparedStatement 객체 생성
			pstmt3 = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			for (int i = 0; i < productImgList.size(); i++) {
				ProductImgVO productImg = productImgList.get(i);
				pstmt3.setLong(1, product_num);
				pstmt3.setString(2, productImg.getFilename());
				pstmt3.addBatch(); //쿼리를 메모리에 올림
				
				//계속 추가하면 outOfMemory 발생, 1000개 단위로 executeBatch()
				if (i % 1000 == 0) {
					pstmt3.executeBatch();
				} // if
			} //for i	
			pstmt3.executeBatch(); //쿼리를 전송
			
			//모든 SQL문이 정상 수행
			conn.commit();	
			
		} catch (Exception e) {
			//하나라도 SQL문이 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
		
		return product_num;
	} //insertProduct

	//상품 정보 수정 (이미지 제외)
	public void updateProduct(ProductVO product) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			//SQL문 작성
			sql = "UPDATE product SET title=?,category_num=?,price=?,"
					+ "content=?,state=?,ip=?,modi_date=SYSDATE,thumbnail_img=?,x_loc=?,y_loc=?,region_cd=?"
					+ " WHERE product_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(++cnt, product.getTitle());
			pstmt.setLong(++cnt, product.getCategory_num());
			pstmt.setInt(++cnt, product.getPrice());
			pstmt.setString(++cnt, product.getContent());
			pstmt.setInt(++cnt, product.getState());
			pstmt.setString(++cnt, product.getIp());	
			pstmt.setString(++cnt, product.getThumbnail_img());
			pstmt.setDouble(++cnt, product.getX_loc());
			pstmt.setDouble(++cnt, product.getY_loc());
			pstmt.setString(++cnt, product.getRegion_cd());
			pstmt.setLong(++cnt, product.getProduct_num());

			//SQL문 실행
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateProduct

	//이미지 추가 메서드
	public void insertProductImages(long product_num, List<ProductImgVO> productImgList) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO product_img (product_num, filename) VALUES (?,?)";
			pstmt = conn.prepareStatement(sql);

			//?에 데이터 바인딩
			int batchCount = 0;	
			for (ProductImgVO productImg : productImgList) {
				pstmt.setLong(1, product_num);
				pstmt.setString(2, productImg.getFilename());
				pstmt.addBatch();
				//계속 추가하면 outOfMemory 발생, 1000개 단위로 executeBatch()
				batchCount++;
				if (batchCount % 1000 == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch(); //누적 쿼리 비우기
				}
			} //foreach
			if (batchCount % 1000 != 0) {
				pstmt.executeBatch();
				pstmt.clearBatch(); //누적 쿼리 비우기
			} //if

		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //insertProductImages

	//이미지 삭제 메서드
	public void deleteProductImage(long product_num, String filename) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;    

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM product_img WHERE product_num=? AND filename=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.setString(2, filename);
			pstmt.executeUpdate();
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //deleteProductImage

	//관리자/사용자 - 상품 삭제
	public void deleteProduct(long product_num) throws Exception{		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		PreparedStatement pstmt6 = null;
		PreparedStatement pstmt7 = null;
		PreparedStatement pstmt8 = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);

			//자식글 삭제
			sql = "DELETE FROM product_img WHERE product_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.executeUpdate();

			sql = "DELETE FROM product_view WHERE product_num=?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setLong(1, product_num);
			pstmt2.executeUpdate();

			sql = "DELETE FROM product_fav WHERE product_num=?";
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.setLong(1, product_num);
			pstmt3.executeUpdate();

			sql = "DELETE FROM product_reply WHERE product_num=?";
			pstmt4 = conn.prepareStatement(sql);
			pstmt4.setLong(1, product_num);
			pstmt4.executeUpdate();

			sql = "DELETE FROM product_chat WHERE chatroom_num IN (SELECT chatroom_num FROM product_chatroom WHERE product_num=?)";
			pstmt5 = conn.prepareStatement(sql);
			pstmt5.setLong(1, product_num);
			pstmt5.executeUpdate();

			sql = "DELETE FROM product_chatroom WHERE product_num=?";
			pstmt6 = conn.prepareStatement(sql);
			pstmt6.setLong(1, product_num);
			pstmt6.executeUpdate();

			sql = "DELETE FROM review WHERE product_num=?";
			pstmt7 = conn.prepareStatement(sql);
			pstmt7.setLong(1, product_num);
			pstmt7.executeUpdate();

			//부모글 삭제
			sql = "DELETE FROM product WHERE product_num=?";
			pstmt8 = conn.prepareStatement(sql);
			pstmt8.setLong(1, product_num);
			pstmt8.executeUpdate();

			//SQL문이 모두 성공하면 커밋
			conn.commit();
		} catch (Exception e) {
			//SQL문이 하나라도 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt8, null);
			DBUtil.executeClose(null, pstmt7, null);
			DBUtil.executeClose(null, pstmt6, null);
			DBUtil.executeClose(null, pstmt5, null);
			DBUtil.executeClose(null, pstmt4, null);
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //deleteProduct

	//*관리자/사용자 공통 사용 범위 재확인요망
	//관리자/사용자 - 전체 상품 개수/검색 상품 개수
	public int getProductCount(String keyfield, String keyword) throws Exception{
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
				if (keyfield.equals("1")) sub_sql += "WHERE title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "WHERE category_name	 LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
			}//if

			//SQL문 작성
			sql = "SELECT COUNT(*) FROM product p "
					+ "JOIN users_detail u USING(user_num) "
					+ "JOIN category c USING (category_num) "
					+ sub_sql;
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
	} //getProductCount

	//관리자/사용자 - 전체 상품 목록/검색 상품 목록
	public List<ProductVO> getListProduct(int start, int end, String keyfield, String keyword) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list = null;		
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "WHERE category_name	 LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
			}//if

			//SQL문 작성 -> 재확인요망
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM product p JOIN users_detail u USING(user_num) "
					+ "JOIN category c USING (category_num) "
					+ sub_sql + " ORDER BY NVL(p.up_date, p.reg_date) DESC, p.product_num DESC)a) "
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
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				//cf: 상품제목, 카테고리, 닉네임 검색
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setCategory_name(rs.getString("category_name"));  
				product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				String upDate = rs.getString("up_date");
				if (upDate != null) {
					product.setUp_date(DurationFromNow.getTimeDiffLabel(upDate));
				}
				product.setState(rs.getInt("state")); //판매상태(0판매중, 1예약중, 2판매완료)
				product.setNickname(rs.getString("nickname"));
				product.setPrice(rs.getInt("price"));	
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setX_loc(rs.getDouble("x_loc"));
				product.setY_loc(rs.getDouble("y_loc"));
				product.setRegion_cd(rs.getString("region_cd"));

				list.add(product);
			} // while

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	} //getListProduct

	//관리자/사용자 - 상품 상세
	public ProductVO getProduct(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO product = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			//SQL문 작성
			//users_detail에서 nickname 가져옴 & 
			//category에서 category_name 및 category_status 가져옴 &
			//product_img에서 filename 정보 가져옴. (4개 테이블 조인)
			sql = "SELECT p.*, u.*, c.*, pi.filenames FROM product p "
					+ "JOIN users_detail u ON p.user_num = u.user_num "
					+ "JOIN category c ON p.category_num = c.category_num "
					+ "LEFT JOIN (SELECT product_num, LISTAGG(filename, ',') "
					+ "WITHIN GROUP (ORDER BY filename) AS filenames "
					+ "FROM product_img GROUP BY product_num) pi ON p.product_num = pi.product_num "
					+ "WHERE p.product_num = ?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, product_num);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("title"));
				product.setContent(rs.getString("content"));
				product.setState(rs.getInt("state"));
				product.setHit(rs.getInt("hit"));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				String upDate = rs.getString("up_date");
				if (upDate != null) {
					product.setUp_date(DurationFromNow.getTimeDiffLabel(upDate));
				}
				product.setModi_date(rs.getString("modi_date"));
				product.setUser_num(rs.getLong("user_num"));
				//users_detail에서 nickname 가져옴
				product.setNickname(rs.getString("nickname"));
				product.setIp(rs.getString("ip"));
				product.setPrice(rs.getInt("price"));
				product.setUp_count(rs.getInt("up_count"));
				product.setCategory_num(rs.getLong("category_num"));
				//category에서 category_name & category_status 가져옴
				product.setCategory_name(rs.getString("category_name"));
				product.setCategory_status(rs.getInt("category_status"));
				product.setX_loc(rs.getDouble("x_loc"));
				product.setY_loc(rs.getDouble("y_loc"));
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setRegion_cd(rs.getString("region_cd"));

				product.setProductImgList(getProductImages(product_num));

			} // if
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);			
		}
		return product;
	} //getProduct

	//관리자/사용자 이미지 상세
	public List<ProductImgVO> getProductImages(long product_num) throws Exception {
		List<ProductImgVO> productImgList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT filename FROM product_img WHERE product_num = ? ORDER BY filename";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ProductImgVO productImg = new ProductImgVO();
				productImg.setFilename(rs.getString("filename"));
				productImgList.add(productImg);
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return productImgList;
	} //getProductImages

	//댓글 등록
	public void insertReplyProduct(ProductReplyVO vo) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO product_reply (reply_num,reply_content,reply_ip,user_num,product_num) "
					+ "VALUES (product_reply_seq.nextval,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(++cnt, vo.getReply_content());
			pstmt.setString(++cnt, vo.getReply_ip());
			pstmt.setLong(++cnt, vo.getUser_num());
			pstmt.setLong(++cnt, vo.getProduct_num());
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //insertReplyProduct

	//댓글 개수
	public int getReplyProductCount(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT COUNT(*) FROM product_reply WHERE product_num=?";

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, product_num);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) { //집합함수여서 무조건 1건이므로 if 사용
				count = rs.getInt(1);
			} // if
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	} //getReplyProductCount

	//댓글 목록
	public List<ProductReplyVO> getListReplyProduct(int start,int end,long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductReplyVO> list = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM (SELECT a.*, rownum rnum "
					+ "FROM (SELECT * FROM product_reply JOIN users_detail USING(user_num) "
					+ "WHERE product_num=? ORDER BY reply_num DESC)a) WHERE rnum >= ? AND rnum <=?";

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, product_num);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductReplyVO>();
			while (rs.next()) {
				ProductReplyVO reply = new ProductReplyVO();
				reply.setReply_num(rs.getLong("reply_num"));
				//날짜 -> 1분전, 1시간전, 1일전 형식의 문자열로 변환				
				reply.setReply_date(DurationFromNow.getTimeDiffLabel(rs.getString("reply_date")));
				if (rs.getString("reply_modidate")!=null) {
					reply.setReply_modidate(DurationFromNow.getTimeDiffLabel(rs.getString("reply_modidate")));
				} // if
				reply.setReply_content(StringUtil.useNoHtml(rs.getString("reply_content")));
				reply.setProduct_num(rs.getLong("product_num"));
				reply.setUser_num(rs.getLong("user_num"));
				reply.setNickname(rs.getString("nickname"));

				list.add(reply);				
			} // while

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	} //getListReplyProduct	

	//댓글 상세 (댓글 수정, 삭제시 작성자 회원번호 체크 용도로 사용)
	public ProductReplyVO getReplyProduct(long reply_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductReplyVO reply = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM product_reply WHERE reply_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, reply_num);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				reply = new ProductReplyVO();
				reply.setReply_num(rs.getLong("reply_num"));
				reply.setUser_num(rs.getLong("user_num"));
			} // if

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return reply;
	} //getReplyProduct

	//댓글 수정
	public void updateReplyProduct(ProductReplyVO reply) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE product_reply SET reply_content=?,reply_modidate=SYSDATE,reply_ip=? WHERE reply_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(++cnt, reply.getReply_content());
			pstmt.setString(++cnt, reply.getReply_ip());
			pstmt.setLong(++cnt, reply.getReply_num());
			//SQL문 실행		
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateReplyProduct

	//댓글 삭제
	public void deleteReplyProduct(long reply_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당		
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "DELETE FROM product_reply WHERE reply_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, reply_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //deleteReplyProduct

	//사용자 - 내가 쓴 상품 댓글 개수
	public int getMyProductReplyCount(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;

		try {
			conn = DBUtil.getConnection();

			sql = "SELECT COUNT(*) FROM product_reply WHERE user_num=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
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
	} //getMyProductReplyCount

	//사용자 - 내가 쓴 상품 댓글 목록	
	public List<ProductReplyVO> getMyProductReplyList(int start,int end,long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductReplyVO> list = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();

			sql = "SELECT * FROM (SELECT a.*,rownum rnum FROM "
					+ "(SELECT r.*, p.thumbnail_img, p.title, p.price, p.reg_date, p.state, ud.nickname FROM product_reply r JOIN product p ON r.product_num=p.product_num "
					+ "JOIN users_detail ud ON p.user_num=ud.user_num WHERE r.user_num=? "
					+ "ORDER BY reply_num DESC)a) WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(++cnt, user_num);
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductReplyVO>();
			while (rs.next()) {
				ProductReplyVO reply = new ProductReplyVO();
				reply.setUser_num(user_num);
				reply.setProduct_num(rs.getLong("product_num"));
				reply.setReply_num(rs.getLong("reply_num"));
				reply.setReply_content(StringUtil.useNoHtml(rs.getString("reply_content")));

				ProductVO productVO = new ProductVO();
				productVO.setThumbnail_img(rs.getString("thumbnail_img"));
				productVO.setTitle(rs.getString("title"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setNickname(rs.getString("nickname"));
				productVO.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				productVO.setState(rs.getInt("state"));
				reply.setProductVO(productVO);

				list.add(reply);
			} //while
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;		
	} //getMyProductReplyList

	//관리자 - 전체 댓글 개수/검색 댓글 개수
	public int getAdminReplyProductCount(String keyfield, String keyword) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE product_num=?";
				else if (keyfield.equals("2")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
			}		
			sql = "SELECT COUNT(*) FROM product_reply "
					+ "JOIN users_detail USING(user_num) "
					+ "JOIN product USING(product_num) " + sub_sql;
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
	} //getAdminReplyProductCount

	//관리자 - 전체 댓글 목록/검색 댓글 목록
	public List<ProductReplyVO> getAdminListReplyProduct(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductReplyVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE product_num=?";
				else if (keyfield.equals("2")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
			}

			sql = "SELECT * FROM (SELECT a.*,rownum rnum FROM "
					+ "(SELECT * FROM product_reply JOIN users_detail USING(user_num) "
					+ sub_sql + " ORDER BY reply_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs = pstmt.executeQuery();

			list = new ArrayList<ProductReplyVO>();
			while (rs.next()) {
				ProductReplyVO reply = new ProductReplyVO();
				reply.setProduct_num(rs.getLong("product_num"));
				reply.setReply_num(rs.getLong("reply_num"));
				reply.setReply_content(StringUtil.useNoHtml(rs.getString("reply_content")));
				reply.setUser_num(rs.getLong("user_num"));
				reply.setNickname(rs.getString("nickname"));

				list.add(reply);
			} // while

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	} //getAdminListReplyProduct

	//관리자 - 댓글 상세
	public ProductReplyVO getReplyProductDetailByAdmin(long reply_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductReplyVO reply = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM product_reply JOIN users_detail USING(user_num) "
					+ "JOIN product USING(product_num) "
					+ "WHERE reply_num=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, reply_num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				reply = new ProductReplyVO();
				reply.setProduct_num(rs.getLong("product_num"));
				reply.setTitle(rs.getString("title"));
				reply.setReply_num(rs.getLong("reply_num"));
				reply.setNickname(rs.getString("nickname"));
				reply.setReply_date(rs.getString("reply_date"));
				String modiDate = rs.getString("reply_modidate");
				if (modiDate != null) {
					reply.setReply_modidate(rs.getString("reply_modidate"));
				}
				reply.setReply_ip(rs.getString("reply_ip"));
				reply.setReply_content(rs.getString("reply_content"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return reply;
	} //getReplyProductDetailByAdmin

	//관리자 - 댓글 수정
	public void updateReplyProductByAdmin(ProductReplyVO reply) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product_reply SET reply_content=? WHERE reply_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reply.getReply_content());
			pstmt.setLong(2, reply.getReply_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateReplyProductByAdmin

	//관리자 - 댓글 삭제
	public void deleteReplyProductByAdmin(long reply_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM product_reply WHERE reply_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, reply_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //deleteReplyProductByAdmin

	//찜 개수
	public int selectFavCount(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT COUNT(*) FROM product_fav WHERE product_num=?";

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, product_num);
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
	} //selectFavCount

	//회원번호와 상품번호를 이용한 찜 정보
	//(회원이 상품정보를 호출했을 때 찜 선택 여부 표시)
	public ProductFavVO selectFav(ProductFavVO favVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductFavVO fav = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM product_fav WHERE product_num=? AND user_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, favVO.getProduct_num());
			pstmt.setLong(2, favVO.getUser_num());

			//SQL문 실행
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fav = new ProductFavVO();
				fav.setProduct_num(rs.getLong("product_num"));
				fav.setUser_num(rs.getLong("user_num"));
				fav.setAlarm_flag(rs.getInt("alarm_flag"));
				
			} // if
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return fav;
	} //selectFav

	//찜 등록
	public void insertFav(ProductFavVO favVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "INSERT INTO product_fav (product_num,user_num) VALUES (?,?)";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, favVO.getProduct_num());
			pstmt.setLong(2, favVO.getUser_num());
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //insertFav	

	//찜 삭제
	public void deleteFav(ProductFavVO favVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "DELETE FROM product_fav WHERE product_num=? AND user_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, favVO.getProduct_num());
			pstmt.setLong(2, favVO.getUser_num());
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //deleteFav

	//내가 찜한 상품 개수(검색 개수 포함)
	public int getMyProductFavCount(String keyfield, String keyword, long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			if(keyword != null && !keyword.equals("")) {
				if(keyfield.equals("1")) sub_sql += "AND p.title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND p.content LIKE '%' || ? || '%'";
			}

			sql = "SELECT COUNT(*) FROM product_fav pf JOIN product p ON pf.product_num=p.product_num "
					+ "JOIN users_detail ud ON p.user_num=ud.user_num WHERE pf.user_num=? "
					+ sub_sql;

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, user_num);
			if(keyword != null && !keyword.equals("")) {
				pstmt.setString(2, keyword);
			}
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
	} //getMyProductFavCount

	//내가 선택한 찜 목록
	public List<ProductFavVO> getMyProductFavList(String keyfield, String keyword, int start, int end, long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductFavVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;	

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			if(keyword != null && !keyword.equals("")) {
				if(keyfield.equals("1")) sub_sql += "AND p.title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND p.content LIKE '%' || ? || '%'";
			}

			//(주의)product_fav의 회원번호(찜 클릭한 회원번호)로 검색되어야 하기 때문에
			//f.user_num으로 표기해야 함.
			sql = "SELECT * FROM (SELECT a.*, rownum rnum "
					+ "FROM (SELECT * FROM product p JOIN users_detail u "
					+ "USING(user_num) JOIN product_fav f USING(product_num) "
					+ "WHERE f.user_num=? " + sub_sql 
					+ " ORDER BY product_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(++cnt, user_num);
			if(keyword != null && !keyword.equals("")) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductFavVO>();
			while (rs.next()) {
				ProductFavVO fav = new ProductFavVO();
				fav.setProduct_num(rs.getLong("product_num"));
				fav.setFav_date(DurationFromNow.getTimeDiffLabel(rs.getString("fav_date")));
				fav.setAlarm_flag(rs.getInt("alarm_flag"));

				ProductVO product = new ProductVO();
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				//HTML 태그를 허용하지 않음 
				product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				product.setPrice(rs.getInt("price"));
				product.setNickname(rs.getString("nickname"));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				product.setState(rs.getInt("state"));
				fav.setProductVO(product);

				list.add(fav);				
			} // while
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;		
	} //getMyProductFavList

	//내가 선택한 찜 목록
	public List<ProductVO> getListProductFav(int start, int end, long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//(주의)product_fav의 회원번호(좋아요 클릭한 회원번호)로 검색되어야 하기 때문에
			//f.user_num으로 표기해야 함.
			sql = "SELECT * FROM (SELECT a.*, rownum rnum "
					+ "FROM (SELECT * FROM product p JOIN users_detail u "
					+ "USING(user_num) JOIN product_fav f USING(product_num) "
					+ "WHERE f.user_num = ? ORDER BY product_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";			
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, user_num);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				//HTML 태그를 허용하지 않음 
				product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				product.setNickname(rs.getString("nickname"));

				list.add(product);

			} // while
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;		
	} //getListBoardFav

	public long getSellerNumByProduct(long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		long seller_num = -1;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT user_num FROM product WHERE product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				seller_num = rs.getLong("user_num");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return seller_num;
	}

	// 나의 상품 레코드 확인
	public int getMyProductCount(String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "AND state=0";
				else if (keyfield.equals("2")) sub_sql += "AND state=1";
				else if (keyfield.equals("3")) sub_sql += "AND state=2";
			}
			sql = "SELECT COUNT(*) FROM product WHERE user_num=? "+sub_sql;
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(2, keyword);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 나의 상품 목록
	public List<ProductVO> getMyListProduct(int start,int end,String keyfield,String keyword,long user_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyfield != null && !"".equals(keyfield)) {
				if(keyfield.equals("1")) sub_sql += " AND state=0"; // 판매중
				else if(keyfield.equals("2")) sub_sql += " AND state=1"; // 예약중
				else if(keyfield.equals("3")) sub_sql += " AND state=2"; // 판매완료
			}
			sql = "SELECT * FROM (SELECT a.*, ROWNUM rnum FROM "
					+ "(SELECT * FROM product WHERE user_num=? " + sub_sql
					+ "ORDER BY GREATEST(reg_date, NVL(modi_date, reg_date), NVL(up_date, reg_date)) DESC) a) "
					+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(++cnt, user_num);
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("title"));
				product.setContent(rs.getString("content"));
				product.setState(rs.getInt("state"));
				product.setHit(rs.getInt("hit"));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				String modiDate = rs.getString("modi_date");
				if (modiDate != null) {
					product.setModi_date(DurationFromNow.getTimeDiffLabel(modiDate));
				}
				product.setUser_num(rs.getLong("user_num"));
				product.setIp(rs.getString("ip"));
				product.setPrice(rs.getInt("price"));
				product.setUp_count(rs.getInt("up_count"));
				String upDate = rs.getString("up_date");
				if (upDate != null) {
					product.setUp_date(DurationFromNow.getTimeDiffLabel(upDate));
				}
				product.setCategory_num(rs.getLong("category_num"));
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setHide_status(rs.getInt("hide_status"));
				product.setX_loc(rs.getDouble("x_loc"));
				product.setY_loc(rs.getDouble("y_loc"));
				product.setRegion_cd(rs.getString("region_cd"));

				list.add(product);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 숨기기 상태 확인
	public int getHideStatus(long product_num, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int status = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT hide_status FROM product WHERE product_num = ? AND user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.setLong(2, user_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				status = rs.getInt(1);
			}
		}catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return status;
	}

	// 숨기기 등록
	public void hideProduct(long product_num, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product SET hide_status = 1 WHERE product_num = ? AND user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.setLong(2, user_num);
			pstmt.executeUpdate();
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 숨기기 해제
	public void unhideProduct(long product_num, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product SET hide_status = 0 WHERE product_num = ? AND user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.setLong(2, user_num);
			pstmt.executeUpdate();
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 끌올 카운트 증가
	public void updateUpCount(long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product SET up_count = up_count + 1,up_date=SYSDATE WHERE product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.executeUpdate();
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 끌올 횟수 조회
	public int getUpCount(long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int upCount = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT up_count FROM product WHERE product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				upCount = rs.getInt(1);
			}
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return upCount;
	}

	// 사용자 목록 - 숨기기X만
	public List<ProductVO> getListProductUnHide(int start, int end, String keyfield, String keyword) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list = null;		
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "WHERE title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "WHERE category_name	 LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
			}//if

			//TODO SQL문 작성 -> 재확인요망
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM product p JOIN users_detail u USING(user_num) "
					+ "JOIN category c USING (category_num) "
					+ sub_sql + " ORDER BY product_num DESC)a) "
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
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				//cf: 상품제목, 카테고리, 닉네임 검색
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setCategory_name(rs.getString("category_name"));  
				product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				product.setReg_date(rs.getString("reg_date"));
				product.setState(rs.getInt("state")); //판매상태(0판매중, 1예약중, 2판매완료)
				product.setNickname(rs.getString("nickname"));
				product.setPrice(rs.getInt("price"));

				list.add(product);
			} // while

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 판매글 상태를 판매완료로 업데이트
	public void updateToSoldState(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product SET state=2 WHERE product_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);

			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateToSoldState	

	// 판매글 상태를 예약중으로 업데이트
	public void updateToReservedState(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product SET state=1 WHERE product_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);

			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateToReservedState	


	// 판매글 상태를 거래가능으로 업데이트
	public void updateToSaleState(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product SET state=0 WHERE product_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);

			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateToSaleState	

	// 최근본 상품 저장
	public void saveProductView(long user_num, long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			// 기존에 이미 본 기록이 있으면 삭제
			sql = "DELETE FROM product_view WHERE user_num = ? AND product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setLong(2, product_num);
			pstmt.executeUpdate();

			// 새로 삽입
			sql = "INSERT INTO product_view (view_num, user_num, product_num, view_date) "
					+ "VALUES (product_view_seq.NEXTVAL, ?, ?, SYSDATE)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setLong(1, user_num);
			pstmt2.setLong(2, product_num);
			pstmt2.executeUpdate();

			conn.commit();
		}catch(Exception e) {
			conn.rollback();
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 최근에 본 상품 레코드 수
	public int getProductViewCountByUser(String keyfield,String keyword, long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !keyword.equals("")) {
				if(keyfield.equals("1")) sub_sql += "AND p.title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND p.content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM product_view pv JOIN users_detail ud ON pv.user_num=ud.user_num "
					+ "JOIN product p ON pv.product_num=p.product_num WHERE pv.user_num=? " + sub_sql;
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			if(keyword != null && !keyword.equals("")) {
				pstmt.setString(2, keyword);
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 최근본 상품 목록
	public List<ProductVO> getListProductViewByUser(int start,int end, String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !keyword.equals("")) {
				if(keyfield.equals("1")) sub_sql += "AND p.title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND p.content LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*,rownum rnum FROM "
					+ "(SELECT p.*,ud.nickname,pv.view_date FROM product_view pv JOIN product p ON pv.product_num=p.product_num "
					+ "JOIN users_detail ud ON p.user_num=ud.user_num WHERE pv.user_num = ? "
					+ sub_sql +" ORDER BY pv.view_date DESC)a) WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(++cnt, user_num);
			if(keyword != null && !keyword.equals("")) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("title"));
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setPrice(rs.getInt("price"));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				product.setNickname(rs.getString("nickname"));
				product.setState(rs.getInt("state"));

				list.add(product); 
			}
		} catch(Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 최근본 상품 삭제
	public void deleteView(long user_num, long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM product_view WHERE user_num = ? AND product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setLong(2, product_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 나의 구매 내역 레코드 확인
	public int getBuyMyProductCount(String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "AND state=0";
				else if (keyfield.equals("2")) sub_sql += "AND state=1";
				else if (keyfield.equals("3")) sub_sql += "AND state=2";
			}
			sql = "SELECT COUNT(*) FROM product WHERE user_num=? "+sub_sql;
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(2, keyword);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 관리자 - 지역코드로 상품 지역분류
	public int getProductCountByRegion(String region_cd, String keyfield, String keyword) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "AND title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "AND category_name LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "AND nickname LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM product p "
					+ "JOIN users_detail u USING(user_num) "
					+ "JOIN category c USING (category_num) "
					+ "WHERE p.region_cd=? "
					+ sub_sql;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, region_cd);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 관리자 - 지역 코드로 상품 레코드 조회
	public List<ProductVO> getListProductByRegion(int start, int end, String region_cd, String keyfield, String keyword) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += "AND title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += "AND category_name LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += "AND nickname LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM product p "
					+ "JOIN users_detail u USING(user_num) "
					+ "JOIN category c USING (category_num) "
					+ "WHERE p.region_cd=? "
					+ sub_sql
					+ " ORDER BY NVL(p.up_date, p.reg_date) DESC, p.product_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, region_cd);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setCategory_name(rs.getString("category_name"));  
				product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				String upDate = rs.getString("up_date");
				if (upDate != null) {
					product.setUp_date(DurationFromNow.getTimeDiffLabel(upDate));
				}
				product.setState(rs.getInt("state"));
				product.setNickname(rs.getString("nickname"));
				product.setPrice(rs.getInt("price"));	
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setX_loc(rs.getDouble("x_loc"));
				product.setY_loc(rs.getDouble("y_loc"));
				product.setRegion_cd(rs.getString("region_cd"));

				list.add(product);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 사용자 - 지역코드로 상품 지역분류(지역 + 차단 회원 + 상품숨김 + 카테고리숨김 제외)
	public int getProductCountByRegionBlockHide(String region_cd, String keyfield, String keyword, Long blocked_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		String region_sql = "";
		String block_sql = "";
		int count = 0;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += " AND p.title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += " AND c.category_name LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += " AND u.nickname LIKE '%' || ? || '%'";
			}
			if (region_cd != null && !"ALL".equalsIgnoreCase(region_cd)) {
				region_sql += " AND p.region_cd = ?";
			}
			if(blocked_num != null) {
				block_sql = " AND NOT EXISTS (SELECT 1 FROM block b WHERE b.blocker_num = ? AND b.blocked_num = p.user_num)";
			}

			sql = "SELECT COUNT(*) FROM product p "
					+ "JOIN users_detail u ON u.user_num=p.user_num "
					+ "JOIN category c ON p.category_num=c.category_num "
					+ "WHERE p.hide_status=0 AND c.category_status=1" + sub_sql + region_sql + block_sql;
			pstmt = conn.prepareStatement(sql);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			if (region_cd != null && !"ALL".equalsIgnoreCase(region_cd)) {
				pstmt.setString(++cnt, region_cd);
			}
			if(blocked_num != null) {
				pstmt.setLong(++cnt, blocked_num);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 사용자 - 지역 코드로 상품 목록 조회(지역 + 차단 회원 + 상품숨김 + 카테고리숨김 제외)
	public List<ProductVO> getListProductByRegionBlockHide(int start, int end, String region_cd, String keyfield, String keyword, Long blocked_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductVO> list = null;
		String sql = null;
		String sub_sql = "";
		String region_sql = "";
		String block_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += " AND p.title LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += " AND c.category_name LIKE '%' || ? || '%'";
				else if (keyfield.equals("3")) sub_sql += " AND u.nickname LIKE '%' || ? || '%'";
			}
			if (region_cd != null && !"ALL".equalsIgnoreCase(region_cd)) {
				region_sql += " AND p.region_cd = ?";
			}
			if(blocked_num != null) {
				block_sql = " AND NOT EXISTS (SELECT 1 FROM block b WHERE b.blocker_num = ? AND b.blocked_num = p.user_num)";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM product p "
					+ "JOIN users_detail u ON u.user_num=p.user_num "
					+ "JOIN category c ON p.category_num=c.category_num "
					+ "WHERE p.hide_status=0 AND c.category_status=1" + sub_sql + region_sql + block_sql
					+ " ORDER BY NVL(p.up_date, p.reg_date) DESC, p.product_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			if (region_cd != null && !"ALL".equalsIgnoreCase(region_cd)) {
				pstmt.setString(++cnt, region_cd);
			}
			if(blocked_num != null) {
				pstmt.setLong(++cnt, blocked_num);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs = pstmt.executeQuery();
			list = new ArrayList<ProductVO>();
			while (rs.next()) {
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setCategory_name(rs.getString("category_name"));  
				product.setTitle(StringUtil.useNoHtml(rs.getString("title")));
				product.setReg_date(DurationFromNow.getTimeDiffLabel(rs.getString("reg_date")));
				String upDate = rs.getString("up_date");
				if (upDate != null) {
					product.setUp_date(DurationFromNow.getTimeDiffLabel(upDate));
				}
				product.setState(rs.getInt("state"));
				product.setNickname(rs.getString("nickname"));
				product.setPrice(rs.getInt("price"));	
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setX_loc(rs.getDouble("x_loc"));
				product.setY_loc(rs.getDouble("y_loc"));
				product.setRegion_cd(rs.getString("region_cd"));

				list.add(product);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	//중고상품 조회수 증가
	public void updateProductCount(long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE product SET hit=hit+1 WHERE product_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, product_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateProductCount
	
	//해당 판매글을 찜한 유저들 조회
	public List<Long> getUsersNumByProductFav(long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<Long> list = new ArrayList<>();

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT user_num FROM product_fav WHERE product_num = ? AND alarm_flag = 0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getLong("user_num"));
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}


} //class

