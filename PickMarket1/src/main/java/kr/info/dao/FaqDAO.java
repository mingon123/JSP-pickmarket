package kr.info.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.info.vo.FaqVO;
import kr.info.vo.NoticeVO;
import kr.util.DBUtil;

public class FaqDAO {
	private static FaqDAO instance = new FaqDAO();
	public static FaqDAO getInstance() {
		return instance;
	}
	private FaqDAO() {}

	//관리자
	//전체 내용 개수,검색 내용 개수
	public int getFAQCountByAdmin(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE faq_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE faq_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM faq " + sub_sql;

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);
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

	//목록,검색 목록
	public List<FaqVO> getFAQListByAdmin(int start,int end, String keyfield,String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FaqVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE faq_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE faq_content LIKE '%' || ? || '%'";
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM faq " + sub_sql 
					+ " ORDER BY faq_date DESC )a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<FaqVO>();
			while(rs.next()) {
				FaqVO faq = new FaqVO();
				faq.setFaq_num(rs.getLong("faq_num"));
				faq.setFaq_title(rs.getString("faq_title"));
				faq.setFaq_content(rs.getString("faq_content"));
				faq.setFaq_date(rs.getDate("faq_date"));
				faq.setFaq_modidate(rs.getDate("faq_modidate"));

				list.add(faq);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	//수정
	public void updateFAQByAdmin(FaqVO faq) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			sql = "UPDATE faq SET faq_title=?, faq_content=?, faq_modidate=SYSDATE WHERE faq_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, faq.getFaq_title());
			pstmt.setString(++cnt, faq.getFaq_content());
			pstmt.setLong(++cnt, faq.getFaq_num());
			pstmt.executeUpdate();

			conn.commit(); // 성공 시 커밋
		} catch (Exception e) {
			conn.rollback(); // 실패 시 롤백
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//등록
	public void insertFAQByAdmin(FaqVO faq) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();

			sql = "INSERT INTO faq (faq_num,faq_title,faq_content) "
					+ "VALUES (faq_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, faq.getFaq_title());
			pstmt.setString(2, faq.getFaq_content());
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	//상세
	public FaqVO getFAQDetailByAdmin(long faq_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FaqVO faq = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM faq WHERE faq_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, faq_num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				faq = new FaqVO();
				faq.setFaq_num(rs.getLong("faq_num"));
				faq.setFaq_title(rs.getString("faq_title"));
				faq.setFaq_content(rs.getString("faq_content"));
				faq.setFaq_date(rs.getDate("faq_date"));
				faq.setFaq_modidate(rs.getDate("faq_modidate"));
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return faq;
	}

	//삭제
	public void deleteFAQByAdmin(Long faq_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM faq WHERE faq_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, faq_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}
}
