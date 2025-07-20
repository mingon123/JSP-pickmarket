package kr.searchword.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.searchword.vo.SearchWordVO;
import kr.util.DBUtil;

public class SearchWordDAO {

	private static SearchWordDAO instance = new SearchWordDAO();

	public static SearchWordDAO getInstance() {
		return instance;
	}
	private SearchWordDAO() {}

	//최근 검색어 개수확인
	public int getSearchWordCount(long user_num, int keyfield) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		int count = -1;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM SEARCH_WORD WHERE user_num = ? AND keyfield = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setInt(2, keyfield);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return count;
	}

	//최근 검색어 유무확인 - true : 있음, false : 없음
	public boolean checkHaveSearchWord(long user_num, String word, int keyfield) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM search_word WHERE user_num = ? AND s_word = ? AND keyfield = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setString(2, word);
			pstmt.setInt(3, keyfield);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return count==0? false : true;
	}

	//최근 검색어 저장
	public void insertSearchWord(long user_num, String word, int keyfield) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO search_word (s_num, user_num, s_word, keyfield, s_date) "
					+ "VALUES (search_word_seq.nextval, ?, ?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setString(2, word);
			pstmt.setInt(3, keyfield);
			pstmt.executeUpdate();

		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//같은 검색어를 또 검색할 경우 최신 시간으로 업데이트
	public void updateSearchWord(long user_num, String word, int keyfield) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE search_word SET s_date = SYSDATE WHERE user_num = ? AND s_word = ? AND keyfield = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setString(2, word);
			pstmt.setInt(3, keyfield);
			pstmt.executeUpdate();

		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//최근 검색어 중 가장 오래전 데이터 찾기
	public long getOldSearchWord(long user_num, int keyfield) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		long s_num = -1;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT s_num FROM ("
					+ " SELECT s_num FROM search_word"
					+ " WHERE user_num = ? AND keyfield = ? "
					+ " ORDER BY s_date ASC, s_num ASC"
					+ ") WHERE ROWNUM = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setInt(2, keyfield);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				s_num = rs.getLong(1);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return s_num;
	}

	// 최근 검색어 정보 리스트
	public List<SearchWordVO> getListSearchWord (long user_num, int keyfield) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<SearchWordVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM search_word WHERE user_num = ? AND keyfield = ? ORDER BY s_date DESC, s_num DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setInt(2, keyfield);
			rs = pstmt.executeQuery();

			list = new ArrayList<SearchWordVO>();
			while(rs.next()) {
				SearchWordVO searchword = new SearchWordVO();
				searchword.setS_num(rs.getLong("s_num"));
				searchword.setUser_num(rs.getLong("user_num"));
				searchword.setS_word(rs.getString("s_word"));
				searchword.setS_date(rs.getDate("s_date"));

				list.add(searchword);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	//최근 검색어 삭제
	public void deleteSearchWord(long s_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM search_word WHERE s_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, s_num);
			pstmt.executeUpdate();

		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	

}
