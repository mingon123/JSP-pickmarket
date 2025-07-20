package kr.info.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.info.vo.NoticeVO;
import kr.util.DBUtil;

public class NoticeDAO {
	private static NoticeDAO instance = new NoticeDAO();
	public static NoticeDAO getInstance() {
		return instance;
	}
	private NoticeDAO() {}

	//관리자 (사용자 공통 사용함)
	//전체 내용 개수,검색 내용 개수
	public int getNoticeCountByAdmin(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE noti_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE noti_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM notice " + sub_sql;

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

	//목록,검색 목록 (사용자 공통 사용함)
	public List<NoticeVO> getNoticeListByAdmin(int start,int end, String keyfield,String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<NoticeVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE noti_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE noti_content LIKE '%' || ? || '%'";
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM notice " + sub_sql 
					+ " ORDER BY noti_date DESC )a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<NoticeVO>();
			while(rs.next()) {
				NoticeVO notice = new NoticeVO();
				notice.setNoti_num(rs.getLong("noti_num"));
				notice.setNoti_title(rs.getString("noti_title"));
				notice.setNoti_content(rs.getString("noti_content"));
				notice.setNoti_date(rs.getDate("noti_date"));
				notice.setNoti_view(rs.getInt("noti_view"));

				list.add(notice);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	//공지사항 수정
	public void updateNoticeByAdmin(NoticeVO notice) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			sql = "UPDATE notice SET noti_title=?, noti_content=? WHERE noti_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, notice.getNoti_title());
			pstmt.setString(++cnt, notice.getNoti_content());
			pstmt.setLong(++cnt, notice.getNoti_num());
			pstmt.executeUpdate();

			conn.commit(); // 성공 시 커밋
		} catch (Exception e) {
			conn.rollback(); // 실패 시 롤백
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//공지사항 등록
	public void insertNoticeByAdmin(NoticeVO notice) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();

			sql = "INSERT INTO notice (noti_num,noti_title,noti_content) "
					+ "VALUES (notice_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice.getNoti_title());
			pstmt.setString(2, notice.getNoti_content());
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	//공지사항 상세 (사용자 공통 사용함)
	public NoticeVO getNoticeDetailByAdmin(long noti_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		NoticeVO notice = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM notice WHERE noti_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, noti_num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				notice = new NoticeVO();
				notice.setNoti_num(rs.getLong("noti_num"));
				notice.setNoti_title(rs.getString("noti_title"));
				notice.setNoti_content(rs.getString("noti_content"));
				notice.setNoti_view(rs.getLong("noti_view"));
				notice.setNoti_date(rs.getDate("noti_date"));
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return notice;
	}

	// 공지사항 삭제
	public void deleteNoticeByAdmin(Long noti_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM notice WHERE noti_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, noti_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	//공지사항 조회수 증가
	public void updateNoticeCount(long noti_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE notice SET noti_view=noti_view+1 WHERE noti_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, noti_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	} //updateNoticeCount
}
