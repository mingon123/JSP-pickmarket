package kr.block.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.block.vo.BlockVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class BlockDAO {
	//싱글턴 패턴
	private static BlockDAO instance = new BlockDAO();

	public static BlockDAO getInstance() {
		return instance;
	}
	private BlockDAO() {}

	// 총레코드수/검색 레코드수
	public int getBlockCount(String keyfield,String keyword, long blocker_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "AND ud.nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "AND b.block_content LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "AND id LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM block b JOIN users_detail ud ON b.blocked_num = ud.user_num "
					+ "WHERE blocker_num=? " + sub_sql;
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, blocker_num);
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
	
	// 차단 등록
	public void insertBlock(BlockVO block) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO block (blocker_num, blocked_num, block_content) VALUES (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, block.getBlocker_num());
			pstmt.setLong(2, block.getBlocked_num());
			pstmt.setString(3, block.getBlock_content());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 차단 목록
	public List<BlockVO> getListBlock(int start,int end,String keyfield,String keyword, long blocker_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BlockVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE ud.nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE b.block_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM(SELECT a.*,rownum rnum FROM ( "
					+ "SELECT b.*, ud.nickname,ud.photo FROM block b JOIN users_detail ud ON b.blocked_num = ud.user_num "
					+ sub_sql + " AND b.blocker_num=? ORDER BY b.block_date DESC)a) WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setLong(++cnt, blocker_num);
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			rs = pstmt.executeQuery();
			list = new ArrayList<BlockVO>();
			while (rs.next()) {
				BlockVO block = new BlockVO();
				block.setRnum(rs.getInt("rnum"));
				block.setBlocked_num(rs.getLong("blocked_num"));
				block.setBlock_content(StringUtil.useBrNoHtml(rs.getString("block_content")));
				block.setBlock_date(rs.getDate("block_date"));
				block.setNickname(StringUtil.useNoHtml(rs.getString("nickname")));
				block.setPhoto(rs.getString("photo"));

				list.add(block);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	// 차단 해제
	public void deleteBlockUser(long blocker_num, long blocked_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM block WHERE blocker_num = ? AND blocked_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, blocker_num);
			pstmt.setLong(2, blocked_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 차단 상태 확인
	public boolean isBlocked(long blocker_num,long blocked_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM block WHERE blocker_num=? AND blocked_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, blocker_num);
			pstmt.setLong(2, blocked_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return flag;
	}
	
	//관리자
	// 차단 전체 내용 개수,검색 내용 개수
	public int getBlockCountByAdmin(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += "WHERE b.block_content LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += "WHERE ud1.nickname LIKE '%' || ? || '%' "
							+ "OR ud2.nickname LIKE '%' || ? || '%'";
				}
			}
			sql = "SELECT COUNT(*) "
					+ "FROM block b "
					+ "JOIN users_detail ud1 ON b.blocker_num = ud1.user_num "
					+ "JOIN users_detail ud2 ON b.blocked_num = ud2.user_num " + sub_sql;

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

	//차단 내역 목록,검색 목록
	public List<BlockVO> getListByAdmin(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BlockVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += "WHERE b.block_content LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += "WHERE ud1.nickname LIKE '%' || ? || '%' "
							+ "OR ud2.nickname LIKE '%' || ? || '%'";
				}
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ("
					+ "SELECT b.blocker_num, b.blocked_num, b.block_content, b.block_date, "
					+ "ud1.nickname AS blocker_nickname, ud2.nickname AS nickname "
					+ "FROM block b "
					+ "JOIN users_detail ud1 ON b.blocker_num = ud1.user_num "
					+ "JOIN users_detail ud2 ON b.blocked_num = ud2.user_num "
					+ sub_sql
					+ " ORDER BY b.block_date DESC"
					+ ") a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					pstmt.setString(++cnt, keyword);
				} else if (keyfield.equals("2")) {
					pstmt.setString(++cnt, keyword);
					pstmt.setString(++cnt, keyword);
				}
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<BlockVO>();
			while(rs.next()) {
				BlockVO block = new BlockVO();
				block.setBlocker_num(rs.getLong("blocker_num"));
				block.setBlocked_num(rs.getLong("blocked_num"));
				block.setNickname(rs.getString("nickname"));
				block.setBlocker_nickname(rs.getString("blocker_nickname"));
				block.setBlock_content(rs.getString("block_content"));
				block.setBlock_date(rs.getDate("block_date"));

				list.add(block);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	//차단상세정보 가져오기 - 관리자
	public BlockVO getDetailBlockByAdmin(long blocker_num, long blocked_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		BlockVO block = null;
		String sql = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT b.blocker_num, b.blocked_num, b.block_content, b.block_date, " 
					+ "ud1.nickname AS blocker_nickname, ud2.nickname AS nickname " 
					+ "FROM block b " 
					+ "JOIN users_detail ud1 ON b.blocker_num = ud1.user_num " 
					+ "JOIN users_detail ud2 ON b.blocked_num = ud2.user_num " 
					+ "WHERE b.blocker_num = ? AND b.blocked_num = ?"; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, blocker_num);
			pstmt.setLong(2, blocked_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				block = new BlockVO();
				block.setBlocker_num(rs.getLong("blocker_num"));
				block.setBlocked_num(rs.getLong("blocked_num"));
				block.setNickname(rs.getString("nickname"));
				block.setBlocker_nickname(rs.getString("blocker_nickname"));
				block.setBlock_content(rs.getString("block_content"));
				block.setBlock_date(rs.getDate("block_date"));
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return block;
	}

	// 차단 삭제 - 관리자
	public void deleteBlockByAdmin(long blocker_num, long blocked_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			// auto commit 해제
			conn.setAutoCommit(false);

			sql = "DELETE FROM block WHERE blocker_num=? AND blocked_num=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, blocker_num);
			pstmt.setLong(2, blocked_num);
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
	
	// 내가 차단한 상대 확인
	public List<Long> getBlockedUsers(long blocker_num) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<Long> list = null;
	    String sql = null;
	    
	    try {
	        conn = DBUtil.getConnection();
	        sql = "SELECT blocked_num FROM block WHERE blocker_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, blocker_num);
	        rs = pstmt.executeQuery();
	        list = new ArrayList<Long>();
	        while (rs.next()) {
	        	list.add(rs.getLong("blocked_num"));
	        }
	    } finally {
	        DBUtil.executeClose(rs, pstmt, conn);
	    }
	    return list;
	}

}
