package kr.notification.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.notification.vo.NotificationVO;
import kr.util.DBUtil;

public class NotificationDAO {

	private static NotificationDAO instance = new NotificationDAO();

	public static NotificationDAO getInstance() {
		return instance;
	}
	private NotificationDAO() {}

	//알림 등록
	public void insertNotification(long user_num, String message, String type, long product_num, Long opponent_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();

			sql = "INSERT INTO notification (notifi_num, user_num, message, type, product_num, opponent_num) "
					+ "VALUES (notification_seq.nextval, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setString(2, message);
			pstmt.setString(3, type);
			pstmt.setLong(4, product_num);
			if (opponent_num != null) {
				pstmt.setLong(5, opponent_num);
			} else {
				pstmt.setNull(5, java.sql.Types.NUMERIC);
			}
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	//해당 사용자의 알림 전체 조회
	public List<NotificationVO> getNotificationsByUser(long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<NotificationVO> list = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM notification WHERE user_num = ? "
					+ "ORDER BY created_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();

			list = new ArrayList<NotificationVO>();
			while (rs.next()) {
				NotificationVO vo = new NotificationVO();
				vo.setNotifi_num(rs.getLong("notifi_num"));
				vo.setUser_num(rs.getInt("user_num"));
				vo.setMessage(rs.getString("message"));
				vo.setIs_read(rs.getInt("is_read"));
				vo.setCreated_date(rs.getDate("created_date"));
				vo.setType(rs.getString("type"));
				vo.setProduct_num(rs.getLong("product_num"));
				vo.setOpponent_num(rs.getLong("opponent_num"));

				list.add(vo);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return list;
	}


	//해당 사용자의 알림 타입별 조회
	public List<NotificationVO> getTypeNotificationsByUser(long user_num, String type) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<NotificationVO> list = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM notification WHERE user_num = ? AND type = ? "
					+ "ORDER BY created_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setString(2, type);
			rs = pstmt.executeQuery();

			list = new ArrayList<NotificationVO>();
			while (rs.next()) {
				NotificationVO vo = new NotificationVO();
				vo.setNotifi_num(rs.getLong("notifi_num"));
				vo.setUser_num(rs.getInt("user_num"));
				vo.setMessage(rs.getString("message"));
				vo.setIs_read(rs.getInt("is_read"));
				vo.setCreated_date(rs.getDate("created_date"));
				vo.setType(rs.getString("type"));
				vo.setProduct_num(rs.getLong("product_num"));

				list.add(vo);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return list;
	}

	//알림 읽음 처리
	public void updateReadNotification(long notifi_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();

			sql = "UPDATE notification SET is_read = 1 WHERE notifi_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, notifi_num);
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//알림 읽음 처리
	public void updateReadAllNotification(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE notification SET is_read = 1 WHERE user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 타입별 안 읽은 알림 개수
	public int countUnreadNotificationByType(long user_num, String type) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		String sub_sql = "";
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			if (!type.equals("all")) {
				sub_sql += " AND type = ?";
			}
			sql = "SELECT COUNT(*) FROM notification WHERE user_num = ? AND is_read = 0" + sub_sql;

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);

			if (!type.equals("all")) {
				pstmt.setString(2, type);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return count;
	}

	//해당 판매글 알림 끄기
	public void updateOffFavAlarm(long user_num, long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();

			sql = "UPDATE product_fav SET alarm_flag = 1 WHERE user_num = ? AND product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setLong(2, product_num);
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//해당 판매글 알림 키기
	public void updateOnFavAlarm(long user_num, long product_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();

			sql = "UPDATE product_fav SET alarm_flag = 0 WHERE user_num = ? AND product_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setLong(2, product_num);
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//해당 알림 삭제
	public void deleteNotification(long notifi_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM notification WHERE notifi_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, notifi_num);
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//해당 유저 알림 전체 삭제
	public void deleteAllNotification(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM notification WHERE user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//해당 사용자의 최근알림 일정 갯수 조회
	public List<NotificationVO> getRecentNotifications(long user_num, int count) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		List<NotificationVO> list = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM ("
				+ "	SELECT * FROM notification "
				+ " WHERE user_num = ? "
				+ " ORDER BY created_date DESC "
				+ ") sub "
				+ "WHERE ROWNUM <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setInt(2, count);
			rs = pstmt.executeQuery();

			list = new ArrayList<NotificationVO>();
			while (rs.next()) {
				NotificationVO vo = new NotificationVO();
				vo.setNotifi_num(rs.getLong("notifi_num"));
				vo.setUser_num(rs.getInt("user_num"));
				vo.setMessage(rs.getString("message"));
				vo.setIs_read(rs.getInt("is_read"));
				vo.setCreated_date(rs.getDate("created_date"));
				vo.setType(rs.getString("type"));
				vo.setProduct_num(rs.getLong("product_num"));
				vo.setOpponent_num(rs.getLong("opponent_num"));

				list.add(vo);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
		return list;
	}

}
