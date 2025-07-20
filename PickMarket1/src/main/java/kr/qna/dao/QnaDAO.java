package kr.qna.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.info.vo.FaqVO;
import kr.qna.vo.QnaVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class QnaDAO {
	private static QnaDAO instance = new QnaDAO();
	public static QnaDAO getInstance() {
		return instance;
	}
	private QnaDAO() {}

	// Q&A 등록
	public void insertQna(QnaVO qna) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO qna (qna_num,qna_title,qna_content,user_num) VALUES (qna_seq.nextval,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, qna.getQna_title());
			pstmt.setString(++cnt, qna.getQna_content());
			pstmt.setLong(++cnt, qna.getUser_num());
			pstmt.executeUpdate();			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 사용자 본인의 Q&A 개수만 카운트
	public int getQnaCountByUser(long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM qna WHERE user_num=?";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// Q&A 목록 - 사용자 본인
	public List<QnaVO> getListQna(int start, int end, long user_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaVO> list = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();			
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM (SELECT * FROM qna "
					+ "JOIN users_detail USING(user_num) WHERE user_num=? ORDER BY qna_num DESC) a) "
					+ "WHERE rnum >=? AND rnum <=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(++cnt, user_num);
			pstmt.setInt(++cnt, start);
			pstmt.setLong(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<QnaVO>();
			while(rs.next()) {
				QnaVO qna = new QnaVO();
				qna.setUser_num(rs.getLong("user_num"));
				qna.setQna_num(rs.getLong("qna_num"));
				qna.setQna_title(StringUtil.useNoHtml(rs.getString("qna_title")));
				qna.setQ_date(rs.getDate("q_date"));
				qna.setA_date(rs.getDate("a_date"));

				list.add(qna);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// Q&A 상세정보 - 사용자 본인
	public QnaVO getQna(long qna_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		QnaVO qna = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM qna JOIN users USING(user_num) "
					+ "LEFT OUTER JOIN users_detail USING(user_num) "
					+ "WHERE qna_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, qna_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				qna = new QnaVO();
				qna.setUser_num(rs.getLong("user_num"));
				qna.setQna_title(rs.getString("qna_title"));
				qna.setQna_content(rs.getString("qna_content"));
				qna.setQna_re(rs.getString("qna_re"));
				qna.setQ_date(rs.getDate("q_date"));
				qna.setA_date(rs.getDate("a_date"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return qna;
	}

	// Q&A 수정 - 사용자 본인
	public void updateQna(QnaVO qna) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE qna SET qna_title=?,qna_content=? WHERE qna_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, qna.getQna_title());
			pstmt.setString(++cnt, qna.getQna_content());
			pstmt.setLong(++cnt, qna.getQna_num());
			pstmt.executeUpdate();			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// Q&A 삭제
	public void deleteQna(long qna_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM qna WHERE qna_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, qna_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	
	//관리자
	// 총레코드수/검색 레코드수
	public int getQnaCountByAdmin(String keyfield,String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !keyword.equals("")) {
				if(keyfield.equals("1")) sub_sql += "WHERE qna_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE qna_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) "
				    + "FROM qna q "
				    + "JOIN users_detail ud ON q.user_num = ud.user_num "
				    + sub_sql;

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !keyword.equals("")) {
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
	
	
	// 답변 작성/수정
	public void insertReplyQnaByAdmin(QnaVO qna)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String title = null;

		try {
			conn = DBUtil.getConnection();
			if(qna.getA_date() == null) {
				title = "RE: " + qna.getQna_title();	
			}else {
				title = qna.getQna_title();
			}
			sql = "UPDATE qna SET qna_title=?,qna_re=?,a_date=SYSDATE WHERE qna_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, qna.getQna_re());
			pstmt.setLong(3, qna.getQna_num());
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 답변 삭제
	public void deleteReplyQnaByAdmin(long qna_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		ResultSet rs = null;
		String db_title = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT qna_title FROM qna WHERE qna_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, qna_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				db_title = rs.getString(1);
			}
			
			String cleanTitle = db_title.replaceFirst("(?i)^re:\\s*", "");
			sql = "UPDATE qna SET qna_re=null,a_date=null,qna_title=? WHERE qna_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cleanTitle);
			pstmt.setLong(2, qna_num);
			pstmt.executeUpdate();
			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null,pstmt2,null);
			DBUtil.executeClose(rs,pstmt,conn);
		}
	}

	
	//목록,검색 목록
	public List<QnaVO> getQnaListByAdmin(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE qna_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE qna_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ("
				    + "SELECT q.qna_num, q.qna_title, q.qna_content, q.qna_re, q.q_date, q.a_date, "
				    + "q.user_num, ud.nickname "
				    + "FROM qna q "
				    + "JOIN users_detail ud ON q.user_num = ud.user_num "
				    + sub_sql
				    + " ORDER BY q.q_date DESC) a) "
				    + "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<QnaVO>();
			while(rs.next()) {
				QnaVO qna = new QnaVO();
				qna.setQna_num(rs.getLong("qna_num"));
				qna.setQna_title(rs.getString("qna_title"));
				qna.setQna_content(rs.getString("qna_content"));
				qna.setQna_re(rs.getString("qna_re"));
				qna.setQ_date(rs.getDate("q_date"));
				qna.setA_date(rs.getDate("a_date"));
				qna.setUser_num(rs.getLong("user_num"));
				qna.setNickname(rs.getString("nickname"));

				list.add(qna);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	
	//상세
	public QnaVO getQnaDetailByAdmin(long qna_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		QnaVO qna = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT q.*, ud.nickname "
					+ "FROM qna q "
					+ "JOIN users_detail ud ON q.user_num = ud.user_num "
					+ "WHERE q.qna_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, qna_num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				qna = new QnaVO();
				qna.setQna_num(rs.getLong("qna_num"));
				qna.setQna_title(rs.getString("qna_title"));
				qna.setQna_content(rs.getString("qna_content"));
				qna.setQna_re(rs.getString("qna_re"));
				qna.setQ_date(rs.getDate("q_date"));
				qna.setA_date(rs.getDate("a_date"));
				qna.setUser_num(rs.getLong("user_num"));
				qna.setNickname(rs.getString("nickname"));
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return qna;
	}


}
