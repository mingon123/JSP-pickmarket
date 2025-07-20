package kr.info.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.info.vo.OperationalPolicyVO;
import kr.util.DBUtil;

public class OperationalPolicyDAO {
	private static OperationalPolicyDAO instance = new OperationalPolicyDAO();
	public static OperationalPolicyDAO getInstance() {
		return instance;
	}
	private OperationalPolicyDAO() {}

	//관리자
	//전체 내용 개수,검색 내용 개수
	public int getOPCountByAdmin(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE pol_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE pol_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM OPERATIONAL_POLICY " + sub_sql;

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
	public List<OperationalPolicyVO> getOPListByAdmin(int start,int end, String keyfield,String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OperationalPolicyVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE pol_title LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE pol_content LIKE '%' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM OPERATIONAL_POLICY " + sub_sql 
					+ " ORDER BY pol_date DESC )a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<OperationalPolicyVO>();
			while(rs.next()) {
				OperationalPolicyVO op = new OperationalPolicyVO();
				op.setPol_num(rs.getLong("pol_num"));
				op.setPol_title(rs.getString("pol_title"));
				op.setPol_content(rs.getString("pol_content"));
				op.setPol_date(rs.getDate("pol_date"));
				op.setPol_modi_date(rs.getDate("pol_modi_date"));

				list.add(op);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	//수정
	public void updateOPByAdmin(OperationalPolicyVO op) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			sql = "UPDATE OPERATIONAL_POLICY SET "
					+ "pol_title=?, pol_content=?, pol_modi_date=SYSDATE WHERE pol_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, op.getPol_title());
			pstmt.setString(++cnt, op.getPol_content());
			pstmt.setLong(++cnt, op.getPol_num());
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
	public void insertOPByAdmin(OperationalPolicyVO op) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO OPERATIONAL_POLICY (pol_num,pol_title,pol_content) "
					+ "VALUES (OPERATIONAL_POLICY_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, op.getPol_title());
			pstmt.setString(2, op.getPol_content());
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	//상세
	public OperationalPolicyVO getOPDetailByAdmin(long pol_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OperationalPolicyVO op = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM OPERATIONAL_POLICY WHERE pol_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pol_num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				op = new OperationalPolicyVO();
				op.setPol_num(rs.getLong("pol_num"));
				op.setPol_title(rs.getString("pol_title"));
				op.setPol_content(rs.getString("pol_content"));
				op.setPol_date(rs.getDate("pol_date"));
				op.setPol_modi_date(rs.getDate("pol_modi_date"));
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return op;
	}

	//삭제
	public void deleteOPByAdmin(long pol_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM OPERATIONAL_POLICY WHERE pol_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pol_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}
}
