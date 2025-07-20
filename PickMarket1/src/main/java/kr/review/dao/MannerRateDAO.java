package kr.review.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.review.vo.MannerRateVO;
import kr.user.dao.UserDAO;
import kr.util.DBUtil;

public class MannerRateDAO {
	private static MannerRateDAO instance = new MannerRateDAO();
	public static MannerRateDAO getInstance() {
		return instance;
	}
	private MannerRateDAO() {}
	
	// 평가 기록 확인
	public int getMannerCount(long rater_num, long rated_num) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
		int count = 0;
	    
	    try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM manner_rate WHERE rater_num=? AND rated_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, rater_num);
			pstmt.setLong(2, rated_num);
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
	
	// 수정한 부분 주석으로 표시함
	public int getMannerCountByRatedNum(long rated_num) throws Exception {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    int count = 0;

	    try {
	        conn = DBUtil.getConnection();
	        sql = "SELECT COUNT(*) FROM manner_rate WHERE rated_num=?"; 
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, rated_num);
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

	
	// 매너 평가 등록(첫 등록)
	public void insertMannerRate(long rater_num, long rated_num, int[] selectedOps) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO manner_rate (manner_num,rater_num,rated_num, " 
				+ "op1,op2,op3,op4,op5,op6,op7,op8,op9,op10) " 
				+ "VALUES (manner_rate_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, rater_num);
	        pstmt.setLong(2, rated_num);
	        
	        // op1~op10 전부 0으로 초기화
	        int[] ops = new int[10];
	        int tempScore = 0; 
	        for (int i : selectedOps) {
	            if (1 <= i && i <= 10) {
	            	ops[i - 1] = 1;
	            	if (i <= 5) tempScore += 1; // 좋은 항목
	            	else tempScore -= 1; // 나쁜 항목
	            }
	        }

	        for (int i = 0; i < 10; i++) {
	            pstmt.setInt(3 + i, ops[i]);
	        }
	        pstmt.executeUpdate();
	        
	        UserDAO userDAO = UserDAO.getInstance();
	        userDAO.modifyTemperatureUser(rated_num, tempScore);
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 매너 평가 수정
	public void updateManner(long rater_num, long rated_num, int[] selectedOps) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		String sql = null;
				
	    try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
	        sql = "SELECT op1,op2,op3,op4,op5,op6,op7,op8,op9,op10 FROM manner_rate WHERE rater_num=? AND rated_num=?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setLong(1, rater_num);
	        pstmt.setLong(2, rated_num);
	        rs = pstmt.executeQuery();
			
	        int oldTempScore = 0;
	        if (rs.next()) {
	            for (int i = 1; i <= 10; i++) {
	                int op = rs.getInt("op" + i);
	                if (op == 1) {
	                    if (i <= 5) oldTempScore += 1;
	                    else oldTempScore -= 1;
	                }
	            }
	        }
	        
	        UserDAO userDAO = UserDAO.getInstance();
	        userDAO.modifyTemperatureUser(rated_num, -oldTempScore);
	        
	        sql = "DELETE FROM manner_rate WHERE rater_num=? AND rated_num=?";
	        pstmt2 = conn.prepareStatement(sql);
	        pstmt2.setLong(1, rater_num);
	        pstmt2.setLong(2, rated_num);
	        pstmt2.executeUpdate();
	        
	        sql = "INSERT INTO manner_rate (manner_num,rater_num,rated_num, " 
				+ "op1,op2,op3,op4,op5,op6,op7,op8,op9,op10) " 
				+ "VALUES (manner_rate_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt3 = conn.prepareStatement(sql);
		    pstmt3.setLong(1, rater_num);
		    pstmt3.setLong(2, rated_num);
		    
		    int[] ops = new int[10];
		    int tempScore = 0;
	        for (int i : selectedOps) {
	            if (1 <= i && i <= 10) {
	            	ops[i - 1] = 1;
	            	if (i <= 5) tempScore += 1; // 좋은 항목
	            	else tempScore -= 1; // 나쁜 항목
	            }
	        }

		    for (int i = 0; i < 10; i++) {
		        pstmt3.setInt(3 + i, ops[i]);
		    }
			pstmt3.executeUpdate();
			
	        userDAO.modifyTemperatureUser(rated_num, tempScore);
			
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	
	// 내가 받은 평가 목록
	public List<MannerRateVO> getListManner(int start,int end,long rated_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<MannerRateVO> list = null;
		String sql = null;
		int cnt = 0;
		
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
	        sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ( "
	            + "SELECT manner_op, SUM(count) AS total_count FROM ( "
	            + "SELECT 'op1' AS manner_op, op1 AS count FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op2', op2 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op3', op3 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op4', op4 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op5', op5 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op6', op6 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op7', op7 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op8', op8 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op9', op9 FROM manner_rate WHERE rated_num = ? UNION ALL "
	            + "SELECT 'op10', op10 FROM manner_rate WHERE rated_num = ?) "
	            + "GROUP BY manner_op ORDER BY total_count DESC)a) "
	            + "WHERE rnum >= ? AND rnum <= ?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
	        for (int i = 1; i <= 10; i++) {
	            pstmt.setLong(++cnt, rated_num);
	        }
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<MannerRateVO>();
			while(rs.next()) {
				MannerRateVO manner = new MannerRateVO();
				manner.setMannerOp(rs.getString("manner_op"));
				manner.setCount(rs.getInt("total_count"));            
				
				list.add(manner);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}
	
}
