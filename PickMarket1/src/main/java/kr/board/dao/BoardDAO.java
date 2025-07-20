package kr.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.board.vo.BoardFavVO;
import kr.board.vo.BoardReplyVO;
import kr.board.vo.BoardVO;
import kr.user.vo.UserVO;
import kr.util.DBUtil;
import kr.util.DurationFromNow;
import kr.util.StringUtil;

public class BoardDAO {
	//싱글턴 패턴
	private static BoardDAO instance = new BoardDAO();
	public static BoardDAO getInstance() {
		return instance;
	}
	private BoardDAO() {}

	//글등록
	public void insertBoard(BoardVO board)throws Exception{

		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="INSERT INTO board (board_num,btitle,bcontent,bfilename,bip,user_num,region_cd) VALUES (board_seq.nextval,?,?,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt =conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(++cnt, board.getBtitle());
			pstmt.setString(++cnt, board.getBcontent());
			pstmt.setString(++cnt, board.getBfilename());
			pstmt.setString(++cnt, board.getBip());
			pstmt.setLong(++cnt, board.getUser_num());
			pstmt.setString(++cnt, board.getRegion_cd());
			//SQL문 실행
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//총레코드수/검색 레코드수           
	public int getBoardCount(String keyfield,String keyword) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql=null;
		String sub_sql="";
		int count=0;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn = DBUtil.getConnection();

			if(keyword !=null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE btitle LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE users_detail.nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE bcontent LIKE '%' || ? || '%'";
				else if(keyfield.equals("4")) sub_sql += "WHERE board_num = ?";
			}
			//SQL문 작성
			sql="SELECT COUNT(*) FROM board JOIN users_detail USING(user_num) " + sub_sql;

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			if(keyword !=null && !"".equals(keyword)) {
				pstmt.setString(1, keyword);				
			}
			//SQL문 실행
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
	// 관리자 - 목록/검색 목록
	public List<BoardVO> getListBoard(int start,int end,String keyfield,String keyword)throws Exception{

		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> list=null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();

			if(keyword !=null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE btitle LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE users_detail.nickname LIKE '%' || ? || '%'";
			}
			//SQL문 작성
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM (SELECT * FROM board " +
					"JOIN users_detail USING(user_num) " + sub_sql + " ORDER BY board_num DESC) a) " +
					"WHERE rnum >=? AND rnum <=?";
			System.out.print(sql);
			pstmt = conn.prepareStatement(sql);
			if(keyword !=null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);

			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			//SQL문 실행
			rs= pstmt.executeQuery();
			list=new ArrayList<BoardVO>();
			while(rs.next()) {
				BoardVO board = new BoardVO();
				board.setBoard_num(rs.getLong("board_num"));
				board.setBtitle(StringUtil.useNoHtml(rs.getString("btitle")));
				board.setBhit(rs.getInt("bhit"));
				board.setBreg_date(rs.getString("breg_date"));
				board.setBcontent(rs.getString("bcontent"));
				board.setNickname(rs.getString("nickname"));//작성자의 닉네임
				board.setUser_num(rs.getLong("user_num"));
				board.setBmodi_date(rs.getString("bmodi_date"));
				//board.setRegion_cd(rs.getString("region_cd"));

				list.add(board);
			}

		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}


	// 글목록
	public List<BoardVO> getListBoardd(int start, int end, String keyfield, String keyword) throws Exception {
		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();

			// WHERE 절 구성
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += " WHERE b.btitle LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += " WHERE u.nickname LIKE '%' || ? || '%'";
				}
			}

			// SQL 전체문 구성
			sql = "SELECT * FROM ( " +
					"    SELECT a.*, rownum rnum FROM ( " +
					"        SELECT b.board_num, b.btitle, b.bhit, b.breg_date, b.bmodi_date, b.bcontent, " +
					"               u.nickname, NVL(f.cnt, 0) AS like_count " +
					"        FROM board b " +
					"        JOIN users_detail u ON b.user_num = u.user_num " +
					"        LEFT JOIN ( " +
					"            SELECT board_num, COUNT(*) AS cnt FROM board_fav GROUP BY board_num " +
					"        ) f ON b.board_num = f.board_num " +
					sub_sql + " " +
					"        ORDER BY b.board_num DESC " +
					"    ) a " +
					") WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);

			// 바인딩 파라미터 설정
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			// 결과 처리
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVO board = new BoardVO();
				board.setBoard_num(rs.getLong("board_num"));
				board.setBtitle(rs.getString("btitle"));
				board.setBhit(rs.getInt("bhit"));
				board.setBreg_date(DurationFromNow.getTimeDiffLabel(rs.getString("breg_date")));
				String modiDate = rs.getString("bmodi_date");
				if (modiDate != null) {
					board.setBmodi_date(DurationFromNow.getTimeDiffLabel(modiDate));
				}
				board.setBcontent(rs.getString("bcontent"));
				board.setNickname(rs.getString("nickname"));
				board.setLike_count(rs.getInt("like_count"));
				list.add(board);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return list;
	}






	//로그인한 회원 닉네임, 동네
	public UserVO getUserByNum(int user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO user = null;

		String sql = "SELECT user_num, nickname, region_ch FROM users WHERE user_num = ?";

		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserVO();
				user.setUser_num(rs.getInt("user_num"));
				user.setNickname(rs.getString("nickname"));
				user.setRegion_cd(rs.getString("region_ch"));
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return user;
	}



	//글 상세
	public BoardVO getBoard(long board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		BoardVO board = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn =DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM board JOIN users USING(user_num) LEFT OUTER JOIN users_detail USING(user_num) WHERE board_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, board_num);
			//SQL문 실행
			rs=pstmt.executeQuery();
			if(rs.next()) {
				board=new BoardVO();
				board.setBoard_num(rs.getInt("board_num")); 
				board.setBhit(rs.getInt("bhit"));
				board.setBreg_date(DurationFromNow.getTimeDiffLabel(rs.getString("breg_date")));
				String modiDate = rs.getString("bmodi_date");
				if (modiDate != null) {
					board.setBmodi_date(DurationFromNow.getTimeDiffLabel(modiDate));
				}
				board.setBtitle(rs.getString("btitle"));
				board.setBcontent(rs.getString("bcontent"));
				board.setBfilename(rs.getString("bfilename"));
				board.setUser_num(rs.getInt("user_num"));
				board.setNickname(rs.getString("nickname"));
				board.setPhoto(rs.getString("photo"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return board;
	}
	//조회수 증가
	public void updateReadCount(long board_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt= null;
		String sql=null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="UPDATE board SET bhit=bhit+1 WHERE board_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, board_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}
	//파일 삭제
	public void deleteFile(long board_num)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		String sql= null;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="UPDATE board SET bfilename='' WHERE board_num=?";
			//PreparedStatement 객체 생성
			pstmt=conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, board_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//글 수정
	public void updateBoard(BoardVO board)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		String sql= null;
		int cnt=0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="UPDATE board SET btitle=?,bcontent=?,bmodi_date=SYSDATE,bfilename=?,bip=? WHERE board_num=?";
			//PreparedStatement 객체 생성
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(++cnt, board.getBtitle());
			pstmt.setString(++cnt, board.getBcontent());
			pstmt.setString(++cnt, board.getBfilename());
			pstmt.setString(++cnt, board.getBip());
			pstmt.setLong(++cnt, board.getBoard_num());
			//SQL문 실행
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}
	//글 삭제
	public void deleteBoard(long board_num)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmt2=null;
		PreparedStatement pstmt3=null;
		String sql=null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//오토커밋 해제
			conn.setAutoCommit(false);
			//좋아요 삭제
			sql="DELETE FROM board_fav WHERE board_num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, board_num);
			pstmt.executeUpdate();

			//댓글 삭제
			sql="DELETE FROM board_reply WHERE board_num=?";
			pstmt2=conn.prepareStatement(sql);
			pstmt2.setLong(1, board_num);
			pstmt2.executeUpdate();

			//부모글 삭제
			sql="DELETE FROM board WHERE board_num=?";
			pstmt3=conn.prepareStatement(sql);
			pstmt3.setLong(1, board_num);
			pstmt3.executeUpdate();
			//예외 발생 없이 정상적으로 모든 SQL문 실행
			conn.commit();
		} catch (Exception e) {
			//예외발생
			conn.rollback();
			throw new Exception(e);			
		}finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//좋아요 개수
	public int selectFavCount(long board_num) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		String sql =null;
		int count = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="SELECT COUNT(*) FROM board_fav WHERE board_num=?";
			//PreparedStiatemnet 객체 생성
			pstmt=conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, board_num);
			//SQL문 실행
			rs=pstmt.executeQuery();
			if(rs.next()) {
				count=rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;

	}

	//회원번호와 게시물 번호를 이용한 좋아요 정보 제공(회원이 게시물을 호출했을 때 좋아요 선택여부를 표시)
	public BoardFavVO selectFav(BoardFavVO favVO) throws Exception{

		Connection conn= null;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		BoardFavVO fav= null;
		String sql= null;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="SELECT * FROM board_fav WHERE board_num=? AND user_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, favVO.getBoard_num());
			pstmt.setLong(2, favVO.getUser_num());
			//SQL문 실행
			rs=pstmt.executeQuery();
			if(rs.next()) {
				fav=new BoardFavVO();
				fav.setBoard_num(rs.getLong("board_num"));
				fav.setUser_num(rs.getLong("user_num"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return fav;
	}

	//좋아요 등록
	public void insertFav(BoardFavVO favVO)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn= DBUtil.getConnection();
			//SQL문 작성
			sql="INSERT INTO board_fav (board_num,user_num) VALUES (?,?)";
			//PreparedStatement 객체 생성
			pstmt= conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, favVO.getBoard_num());
			pstmt.setLong(2, favVO.getUser_num());
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//좋아요 삭제
	public void deleteFav(BoardFavVO favVO)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="DELETE FROM board_fav WHERE board_num=? AND user_num=?";
			//PreparedStatement 객체 생성
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, favVO.getBoard_num());
			pstmt.setLong(2, favVO.getUser_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	
	// 커뮤니티 좋아요 레코드 확인
	public int selectFavCountByUser(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		String sql =null;
		int count = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql="SELECT COUNT(*) FROM board_fav WHERE user_num=?";
			//PreparedStiatemnet 객체 생성
			pstmt=conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, user_num);
			//SQL문 실행
			rs=pstmt.executeQuery();
			if(rs.next()) {
				count=rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	
	//내가 선택한 좋아요 목록
	public List<BoardFavVO> getListBoardFav(int start, int end, long user_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardFavVO> list = null;
		String sql = null;
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			sql = "SELECT * FROM ( "
					+ "SELECT a.*, rownum rnum FROM ( "
					+ "SELECT b.board_num, b.btitle, b.bhit, b.breg_date, b.bmodi_date, b.bcontent, b.bfilename, "
					+ "u.nickname, NVL(f.cnt, 0) AS like_count "
					+ "FROM board_fav bf "
					+ "JOIN board b ON bf.board_num = b.board_num "
					+ "JOIN users_detail u ON b.user_num = u.user_num "
					+ "LEFT JOIN ( "
					+ "  SELECT board_num, COUNT(*) AS cnt FROM board_fav GROUP BY board_num "
					+ ") f ON b.board_num = f.board_num "
					+ "WHERE bf.user_num = ? "
					+ "ORDER BY b.board_num DESC "
					+ ") a) "
					+ "WHERE rnum >= ? AND rnum <= ?";
			//PreparedSatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(++cnt, user_num);
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
			//SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<BoardFavVO>();
			while(rs.next()) {
				BoardFavVO bf = new BoardFavVO();
				long board_num = rs.getLong("board_num");
				bf.setBoard_num(board_num);
				bf.setUser_num(user_num);
				
				BoardVO board = new BoardVO();
				board.setBoard_num(board_num);
				board.setBtitle(StringUtil.useNoHtml(rs.getString("btitle")));
				board.setBhit(rs.getInt("bhit"));
				board.setBreg_date(DurationFromNow.getTimeDiffLabel(rs.getString("breg_date")));
				String modiDate = rs.getString("bmodi_date");
				if (modiDate != null) {
					board.setBmodi_date(DurationFromNow.getTimeDiffLabel(modiDate));
				}
				board.setBcontent(rs.getString("bcontent"));
				board.setNickname(rs.getString("nickname"));
				board.setLike_count(rs.getInt("like_count"));
				board.setBfilename(rs.getString("bfilename"));
				int reply_count = getCountBoardReply(board_num);
				board.setReply_count(reply_count);

				bf.setBoardVO(board);
				
				list.add(bf);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}


	//댓글 등록
	public void insertReplyBoard(BoardReplyVO vo) throws Exception{
		Connection conn=null;
		PreparedStatement pstmt= null;
		String sql = null;
		int cnt = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn= DBUtil.getConnection();
			//SQL문 작성
			sql="INSERT INTO board_reply (breply_num,breply_content,breply_ip,user_num,board_num) VALUES (board_reply_seq.nextval,?,?,?,?)";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(++cnt, vo.getBreply_content());
			pstmt.setString(++cnt, vo.getBreply_ip());
			pstmt.setLong(++cnt, vo.getUser_num());
			pstmt.setLong(++cnt, vo.getBoard_num());
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//댓글 개수
	public int getReplyBoardCount(long board_num)throws Exception{

		Connection conn =null;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		String sql= null;
		int count = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn= DBUtil.getConnection();
			//SQL 문 작성
			sql="SELECT COUNT(*) FROM board_reply WHERE board_num=?";
			//PreparedStatement객체 생성
			pstmt=conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, board_num);

			//SQL문 실행
			rs= pstmt.executeQuery();
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
	//댓글 목록
	public List<BoardReplyVO> getListReplyBoardd(int start, int end, long board_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardReplyVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();

			sql = "SELECT * FROM (" +
					"  SELECT a.*, rownum rnum FROM (" +
					"    SELECT r.*, d.nickname FROM board_reply r " +
					"    JOIN users_detail d ON r.user_num = d.user_num " +
					"    WHERE r.board_num = ? " +
					"    ORDER BY r.breply_num DESC" +
					"  ) a WHERE rownum <= ?" +
					") WHERE rnum >= ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, board_num);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);

			rs = pstmt.executeQuery();
			list = new ArrayList<BoardReplyVO>();
			while (rs.next()) {
				BoardReplyVO reply = new BoardReplyVO();
				reply.setBreply_num(rs.getLong("breply_num"));
				reply.setBreply_date(rs.getDate("breply_date"));
				//if (rs.getDate("breply_modidate") != null) {
				reply.setBreply_modidate(rs.getDate("breply_modidate"));
				//}
				reply.setBreply_content(rs.getString("breply_content"));
				reply.setBoard_num(rs.getLong("board_num"));
				reply.setUser_num(rs.getLong("user_num"));
				reply.setNickname(rs.getString("nickname"));

				list.add(reply);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 관리자 - 댓글 목록
	public List<BoardReplyVO> getListReplyBoard(int start, int end, String keyfield, String keyword) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardReplyVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			// 키워드가 있을 경우 서브 쿼리 조건 추가
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("4")) {sub_sql += "WHERE board.board_num = ?";
				}else if (keyfield.equals("2")) {sub_sql += "WHERE users_detail.nickname LIKE '%' || ? || '%'";
				}
			}

			// SQL문 작성
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM (SELECT board_reply.*, users_detail.nickname " +
					"FROM board_reply " +
					"JOIN users_detail ON board_reply.user_num = users_detail.user_num " +
					"JOIN board ON board_reply.board_num = board.board_num " + sub_sql +
					" ORDER BY breply_num DESC) a) WHERE rnum >= ? AND rnum <= ?";


			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("4")) {
					// board_num을 long으로 설정
					pstmt.setLong(++cnt, Long.parseLong(keyword));
				} else if (keyfield.equals("2")) {
					// nickname을 LIKE 조건으로 설정
					pstmt.setString(++cnt, keyword);
				}
			}

			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();
			list = new ArrayList<>();

			// 결과를 리스트에 추가
			while (rs.next()) {
				BoardReplyVO reply = new BoardReplyVO();
				reply.setBreply_num(rs.getLong("breply_num"));
				reply.setBreply_date(rs.getDate("breply_date"));
				reply.setBreply_modidate(rs.getDate("breply_modidate"));
				reply.setBreply_content(StringUtil.useBrNoHtml(rs.getString("breply_content")));
				reply.setBoard_num(rs.getLong("board_num"));
				reply.setUser_num(rs.getLong("user_num"));
				reply.setBreply_ip(rs.getString("breply_ip"));
				reply.setNickname(rs.getString("nickname"));

				list.add(reply);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			// 자원 해제
			DBUtil.executeClose(rs, pstmt, conn);
		}

		return list;
	}

	//관리자 - 댓글 상세(댓글 수정,삭제시 작성자 회원번호 체크 용도로 사용)
	public BoardReplyVO getReplyBoard(long breply_num) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		BoardReplyVO reply=null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn=DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT board_reply.*, users_detail.nickname,board.btitle " +
					"FROM board_reply " +
					"JOIN users_detail ON board_reply.user_num = users_detail.user_num "
				+ "JOIN board ON board_reply.board_num = board.board_num " +
					"WHERE board_reply.breply_num = ?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이서 바인딩
			pstmt.setLong(1, breply_num);
			//SQL문 실행
			rs=pstmt.executeQuery();
			if(rs.next()) {
				reply = new BoardReplyVO();
				reply.setBreply_ip(rs.getNString("breply_ip"));
				reply.setBreply_date(rs.getDate("breply_date"));
				reply.setBreply_content(rs.getString("breply_content"));
				reply.setBreply_modidate(rs.getDate("breply_modidate"));
				reply.setBreply_num(rs.getLong("breply_num"));
				reply.setUser_num(rs.getLong("user_num"));
				reply.setBoard_num(rs.getLong("board_num"));
				reply.setNickname(rs.getString("nickname"));
				
				BoardVO board = new BoardVO();
				board.setBtitle(rs.getString("btitle"));
				reply.setBoardVO(board);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return reply;
	}

	//관리자 - 댓글 수정
	public void updateReplyBoard(BoardReplyVO breply_num) throws Exception{

		Connection conn=null;
		PreparedStatement pstmt = null;
		String sql = null;
		String sub_sql="";
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql="UPDATE board_reply SET breply_content=?, breply_modidate=SYSDATE,breply_ip=?" + sub_sql + "WHERE breply_num=?";

			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(++cnt, breply_num.getBreply_content());
			pstmt.setString(++cnt, breply_num.getBreply_ip());
			pstmt.setLong(++cnt, breply_num.getBreply_num());
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//댓글 삭제
	public void deleteReplyBoard(long breply_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn= DBUtil.getConnection();
			//sql문 작성
			sql = "DELETE FROM board_reply WHERE breply_num=?";
			//PreparedStatement 객체 생성
			pstmt= conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, breply_num);
			//SQL문 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 내가 쓴 게시글 총레코드수(검색 레코드수)
	public int getBoardCountByUser(String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += " AND btitle LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += " AND bcontent LIKE '%' || ? || '%'";
				}
			}

			// SQL문 작성 (채팅 관련 제거)
			sql = "SELECT COUNT(*) FROM board WHERE user_num=?" + sub_sql;

			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// ?에 데이터 바인딩
			pstmt.setLong(1, user_num);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(2, keyword);
			}

			// SQL문 실행
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


	//내가 쓴 게시글 글목록(검색글 목록)
	public List<BoardVO> getListBoardByUser(int start, int end,	String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += " AND btitle LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += " AND bcontent LIKE '%' || ? || '%'";
				}
			}
			// SQL문 작성 (채팅 관련 제거)
			sql = "SELECT * FROM ( "
					+ "SELECT a.*, rownum rnum FROM ( "
					+ "SELECT b.board_num, b.btitle, b.bhit, b.breg_date, b.bmodi_date, b.bcontent, b.bfilename, "
					+ "u.nickname, NVL(f.cnt, 0) AS like_count "
					+ "FROM board b "
					+ "JOIN users_detail u ON b.user_num = u.user_num "
					+ "LEFT JOIN ( "
					+ "  SELECT board_num, COUNT(*) AS cnt FROM board_fav GROUP BY board_num "
					+ ") f ON b.board_num = f.board_num "
					+ "WHERE b.user_num = ? "
					+ sub_sql
					+ " ORDER BY b.board_num DESC "
					+ ") a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);

			// 바인딩 순서 맞춰서 처리
			pstmt.setLong(++cnt, user_num);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			// SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<BoardVO>();
			while (rs.next()) {
				BoardVO board = new BoardVO();
				long board_num = rs.getLong("board_num");
				board.setBoard_num(board_num);
				board.setBtitle(StringUtil.useNoHtml(rs.getString("btitle")));
				board.setBhit(rs.getInt("bhit"));
				board.setBreg_date(DurationFromNow.getTimeDiffLabel(rs.getString("breg_date")));
				String modiDate = rs.getString("bmodi_date");
				if (modiDate != null) {
					board.setBmodi_date(DurationFromNow.getTimeDiffLabel(modiDate));
				}
				board.setBcontent(rs.getString("bcontent"));
				board.setNickname(rs.getString("nickname"));
				board.setLike_count(rs.getInt("like_count"));
				board.setBfilename(rs.getString("bfilename"));
				int reply_count = getCountBoardReply(board_num);
				board.setReply_count(reply_count);

				// 채팅 관련 필드 제거됨
				list.add(board);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 내가 쓴 게시글 댓글 총레코드수(검색 레코드수)
	public int getBoardReplyCountByUser(String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += " AND breply_content LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += " AND breply_content LIKE '%' || ? || '%'";
				}
			}

			// SQL문 작성 (채팅 관련 제거)
			sql = "SELECT COUNT(*) FROM board_reply WHERE user_num=?" + sub_sql;

			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// ?에 데이터 바인딩
			pstmt.setLong(1, user_num);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(2, keyword);
			}

			// SQL문 실행
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

	//내가 쓴 댓글 목록(검색글 목록)
	public List<BoardReplyVO> getListBoardreplyByUser(int start, int end, String keyfield, String keyword, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardReplyVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();

			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
					sub_sql += " AND breply_content LIKE '%' || ? || '%'";
				} else if (keyfield.equals("2")) {
					sub_sql += " AND breply_content LIKE '%' || ? || '%'";
				}
			}

			// SQL문 작성 (board_reply와 board 테이블을 JOIN하여 btitle을 가져옴)
			sql = "SELECT br.*, b.btitle,ud.nickname FROM (SELECT a.*, rownum rnum FROM ("
					+ "SELECT * FROM board_reply WHERE user_num=?"
					+ sub_sql
					+ " ORDER BY board_num DESC"
					+ ") a) br "
					+ "JOIN board b ON br.board_num = b.board_num "
					+ "JOIN users_detail ud ON b.user_num = ud.user_num "
					+ "WHERE rnum >= ? AND rnum <= ?";

			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// ?에 데이터 바인딩
			pstmt.setLong(++cnt, user_num);
			if (keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			// SQL문 실행
			rs = pstmt.executeQuery();
			list = new ArrayList<BoardReplyVO>();
			while (rs.next()) {
				BoardReplyVO board = new BoardReplyVO();
				board.setBoard_num(rs.getLong("board_num"));
				board.setBreply_content(rs.getString("breply_content"));
				board.setBreply_date(rs.getDate("breply_date"));
				board.setBtitle(rs.getString("btitle"));
				board.setNickname(rs.getString("nickname"));

				list.add(board);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 지역별 커뮤니티 글 레코드 + 차단
	public int getBoardCountByRegionBlock(String region_cd, String keyfield,String keyword, Long blocked_num) throws Exception{
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
			//커넥션풀로부터 커넥션을 할당
			conn = DBUtil.getConnection();

			if(keyword !=null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += " AND b.btitle LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += " AND u.nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += " AND b.bcontent LIKE '%' || ? || '%'";
			}
			if (region_cd != null && !"ALL".equalsIgnoreCase(region_cd)) {
				region_sql += " AND b.region_cd = ?";
			}
			if(blocked_num != null) {
				block_sql = " AND NOT EXISTS (SELECT 1 FROM block bl WHERE bl.blocker_num = ? AND bl.blocked_num = u.user_num)";
			}
			//SQL문 작성
			sql = "SELECT COUNT(*) FROM board b "
					+ "JOIN users_detail u ON b.user_num=u.user_num "
					+ "WHERE 1=1" + sub_sql + region_sql + block_sql;

			//PreparedStatement 객체 생성
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
			//SQL문 실행
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
	// 사용자 - 목록/검색 목록
	public List<BoardVO> getListBoardByRegionBlock(int start, int end, String region_cd, String keyfield, String keyword, Long blocked_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> list = null;
		String sql = null;
		String sub_sql = "";
		String region_sql = "";
		String block_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();

			// WHERE 절 구성
			if (keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) sub_sql += " AND b.btitle LIKE '%' || ? || '%'";
				else if (keyfield.equals("2")) sub_sql += " AND u.nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += " AND b.bcontent LIKE '%' || ? || '%'";
			}
			if (region_cd != null && !"ALL".equalsIgnoreCase(region_cd)) {
				region_sql += " AND b.region_cd = ?";
			}
			if(blocked_num != null) {
				block_sql = " AND NOT EXISTS (SELECT 1 FROM block bl WHERE bl.blocker_num = ? AND bl.blocked_num = u.user_num)";
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM ( "
					+ "SELECT b.board_num, b.btitle, b.bhit, b.breg_date, b.bmodi_date, b.bcontent,b.region_cd, b.bfilename, "
					+ "u.nickname, NVL(f.cnt, 0) AS like_count "
					+ "FROM board b "
					+ "JOIN users_detail u ON b.user_num = u.user_num "
					+ "LEFT JOIN ( "
					+ "SELECT board_num, COUNT(*) AS cnt "
					+ "FROM board_fav GROUP BY board_num) f ON b.board_num = f.board_num "
					+ "WHERE 1=1"+ sub_sql + region_sql + block_sql
					+ " ORDER BY b.board_num DESC)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);

			// 바인딩 파라미터 설정
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

			// 결과 처리
			rs = pstmt.executeQuery();
			list = new ArrayList<BoardVO>();
			while (rs.next()) {
				BoardVO board = new BoardVO();
				long board_num = rs.getLong("board_num");
				
				board.setBoard_num(board_num);
				board.setBtitle(rs.getString("btitle"));
				board.setBhit(rs.getInt("bhit"));
				board.setBreg_date(DurationFromNow.getTimeDiffLabel(rs.getString("breg_date")));
				String modiDate = rs.getString("bmodi_date");
				if (modiDate != null) {
					board.setBmodi_date(DurationFromNow.getTimeDiffLabel(modiDate));
				}
				board.setBcontent(rs.getString("bcontent"));
				board.setNickname(rs.getString("nickname"));
				board.setLike_count(rs.getInt("like_count"));
				board.setRegion_cd(rs.getString("region_cd"));
				board.setBfilename(rs.getString("bfilename"));
				
				int reply_count = getCountBoardReply(board_num);
				board.setReply_count(reply_count);

				list.add(board);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	//해당 커뮤니티글에 달린 댓글 개수
	public int getCountBoardReply(long board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		String sql =null;
		int count = 0;
		try {
			conn=DBUtil.getConnection();
			sql="SELECT COUNT(*) FROM board_reply WHERE board_num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, board_num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				count=rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}


}


