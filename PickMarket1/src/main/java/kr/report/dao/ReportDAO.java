package kr.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.report.vo.ReportVO;
import kr.user.vo.UserVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class ReportDAO {
	private static ReportDAO instance = new ReportDAO();
	public static ReportDAO getInstance() {
		return instance;
	}
	private ReportDAO() {}

	// 신고 등록
	public void insertReport(ReportVO report) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO report (report_num,report_title,report_content,report_img,suspect_num,reporter_num) VALUES (report_seq.nextval,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, report.getReport_title());
			pstmt.setString(++cnt, report.getReport_content());
			pstmt.setString(++cnt, report.getReport_img());
			pstmt.setLong(++cnt, report.getSuspect_num());
			pstmt.setLong(++cnt, report.getReporter_num());
			pstmt.executeUpdate();			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 총레코드수/검색 레코드수/전체 신고 수
	public int getReportCount(String keyfield,String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !keyword.equals("")) {
				if(keyfield.equals("1")) sub_sql += "WHERE report_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE report_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM report r JOIN users u ON r.reporter_num=u.user_num " + sub_sql;

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

	// 사용자 본인의 신고 개수만 카운트
	public int getReportCountByUser(long reporter_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM report WHERE reporter_num=?";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, reporter_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	// 회원번호 -> 닉네임
	public String getUserNickname(long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String nickname = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT nickname FROM users_detail WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				nickname = rs.getString("nickname");
			}
		}catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return nickname;
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

	// 신고 목록
	public List<ReportVO> getListReport(int start, int end, String keyfield, String keyword, long reporter_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ReportVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "AND ud.nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND r.report_title LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ("
					+ "SELECT r.*,ud.nickname FROM report r JOIN users_detail ud ON ud.user_num=r.suspect_num "
					+ "WHERE reporter_num=? " + sub_sql +" ORDER BY r.report_num DESC)a) "
					+ "WHERE rnum >=? AND rnum <=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(++cnt, reporter_num);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setLong(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<ReportVO>();
			while(rs.next()) {
				ReportVO report = new ReportVO();
				report.setReport_num(rs.getInt("report_num"));
				report.setReporter_num(rs.getLong("reporter_num"));
				report.setSuspect_num(rs.getLong("suspect_num"));
				report.setReport_content(rs.getString("report_content"));
				report.setReport_img(rs.getString("report_img"));
				report.setReport_title(StringUtil.useNoHtml(rs.getString("report_title")));
				report.setReport_date(rs.getDate("report_date"));
				report.setNickname(rs.getString("nickname"));

				list.add(report);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 신고 상세 조회
	public ReportVO getReport(long report_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ReportVO report = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT r.*,ud.nickname FROM report r JOIN users u ON r.reporter_num=u.user_num "
					+ "LEFT OUTER JOIN users_detail ud ON u.user_num=ud.user_num "
					+ "WHERE report_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, report_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				report = new ReportVO();
				report.setReport_num(rs.getInt("report_num"));
				report.setReporter_num(rs.getLong("reporter_num"));
				report.setSuspect_num(rs.getLong("suspect_num"));
				report.setReport_content(rs.getString("report_content"));
				report.setReport_img(rs.getString("report_img"));
				report.setReport_title(rs.getString("report_title"));
				report.setReport_date(rs.getDate("report_date"));
				report.setNickname(rs.getString("nickname"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return report;
	}

	// 이미지 삭제
	public void deleteImage(long report_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE report SET report_img='' WHERE report_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, report_num);
			pstmt.executeUpdate();
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 신고 수정
	public void updateReport(ReportVO report) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(report.getReport_img()!=null && !report.getReport_img().equals("")) {
				sub_sql += ",report_img=?";
			}
			sql = "UPDATE report SET report_title=?,report_content=?"+sub_sql+" WHERE report_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, report.getReport_title());
			pstmt.setString(++cnt, report.getReport_content());
			if(report.getReport_img()!=null && !report.getReport_img().equals("")) {
				pstmt.setString(++cnt, report.getReport_img());
			}
			pstmt.setLong(++cnt, report.getReport_num());
			pstmt.executeUpdate();			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 신고 삭제
	public void deleteReport(long report_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM report WHERE report_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, report_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//관리자
	//전체 내용 개수,검색 내용 개수
	public int getReportCountByAdmin(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) 
					sub_sql += "WHERE r.report_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) 
					sub_sql += "WHERE ud1.nickname LIKE '%' || ? || '%' "
							+ "OR ud2.nickname LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) "
					+ "FROM report r "
					+ "JOIN users_detail ud1 ON r.reporter_num = ud1.user_num "
					+ "JOIN users_detail ud2 ON r.suspect_num = ud2.user_num " + sub_sql;

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					pstmt.setString(1, keyword);
				} else if (keyfield.equals("2")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
				}
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
	public List<ReportVO> getListByAdmin(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ReportVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) 
					sub_sql += "WHERE r.report_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) 
					sub_sql += "WHERE ud1.nickname LIKE '%' || ? || '%' "
							+ "OR ud2.nickname LIKE '%' || ? || '%'";
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ("
					+ "SELECT r.report_num, r.report_title, r.reporter_num, r.suspect_num, "
					+ "r.report_date, ud1.nickname AS reporter_nickname, ud2.nickname AS suspect_nickname "
					+ "FROM report r "
					+ "JOIN users_detail ud1 ON r.reporter_num = ud1.user_num "
					+ "JOIN users_detail ud2 ON r.suspect_num = ud2.user_num "
					+ sub_sql
					+ " ORDER BY r.report_date DESC) a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					pstmt.setString(++cnt, keyword);
				} else if(keyfield.equals("2")) {
					pstmt.setString(++cnt, keyword); // ud1.nickname
					pstmt.setString(++cnt, keyword); // ud2.nickname
				}
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<ReportVO>();
			while(rs.next()) {
				ReportVO report = new ReportVO();
				report.setReport_num(rs.getLong("report_num"));
				report.setReport_title(rs.getString("report_title"));
				report.setReporter_num(rs.getLong("reporter_num"));
				report.setSuspect_num(rs.getLong("suspect_num"));
				report.setReporter_nickname(rs.getString("reporter_nickname"));
				report.setNickname(rs.getString("suspect_nickname")); 

				list.add(report);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}
	//신고상세정보 가져오기 - 관리자
	public ReportVO getDetailReportByAdmin(long report_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ReportVO report = null;
		String sql = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT r.report_num, r.report_title, r.report_content, r.report_img, "
					+ "r.report_date, r.reporter_num, r.suspect_num, " 
					+ "ud1.nickname AS reporter_nickname, ud2.nickname AS suspect_nickname " 
					+ "FROM report r " 
					+ "JOIN users_detail ud1 ON r.reporter_num = ud1.user_num " 
					+ "JOIN users_detail ud2 ON r.suspect_num = ud2.user_num " 
					+ "WHERE r.report_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, report_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				report = new ReportVO();
				report.setReport_num(rs.getLong("report_num"));
				report.setReport_title(rs.getString("report_title"));
				report.setReport_content(rs.getString("report_content"));
				report.setReport_img(rs.getString("report_img"));
				report.setReport_date(rs.getDate("report_date"));
				report.setReporter_num(rs.getLong("reporter_num"));
				report.setSuspect_num(rs.getLong("suspect_num"));
				report.setReporter_nickname(rs.getString("reporter_nickname"));
				report.setNickname(rs.getString("suspect_nickname"));
			}

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return report;
	}

	// 신고 삭제 - 관리자
	public void deleteReportByAdmin(long report_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			// auto commit 해제
			conn.setAutoCommit(false);

			sql = "DELETE FROM report WHERE report_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, report_num);
			pstmt.executeUpdate();
			// 모든 SQL문의 실행이 성공하면 커밋
			conn.commit();
		}catch(Exception e) {
			// SQL문이 하나라도 실패하면 롤백
			conn.rollback();			
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

}
