package kr.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.location.vo.LocationVO;
import kr.user.vo.KeywordVO;
import kr.user.vo.UserVO;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class UserDAO {
	//싱글턴 패턴
	private static UserDAO instance = new UserDAO();

	public static UserDAO getInstance() {
		return instance;
	}
	private UserDAO() {}

	//회원가입
	public void insertMember(UserVO user) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		String sql = null;
		long num = 0; //시퀀스 번호 저장
		int cnt = 0;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);

			//회원번호(user_num) 생성
			sql = "SELECT users_seq.nextval FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				num = rs.getLong(1);
			}

			sql = "INSERT INTO users (USER_NUM,ID) VALUES (?,?)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setLong(1, num);//회원번호
			pstmt2.setString(2, user.getId());
			pstmt2.executeUpdate();

			sql = "INSERT INTO USERS_DETAIL (USER_NUM,NAME,NICKNAME,"
					+ "PASSWD,PHONE,region_cd) VALUES ("
					+ "?,?,?,?,?,?)";
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.setLong(++cnt, num);//회원번호
			pstmt3.setString(++cnt, user.getName());
			pstmt3.setString(++cnt, user.getNickname()+num); //이름+시퀀스번호
			pstmt3.setString(++cnt, user.getPasswd());
			pstmt3.setString(++cnt, user.getPhone());
			pstmt3.setString(++cnt, user.getRegion_cd());
			pstmt3.executeUpdate();

			//SQL 실행시 모두 성공하면 commit
			conn.commit();
		}catch(Exception e) {
			//SQL문이 하나라도 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}

	//Email(=ID) 중복 체크
	public boolean checkUniqueInfo(String id) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM USERS LEFT OUTER JOIN "
					+ "USERS_DETAIL USING(user_num) WHERE id=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, id);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true; //중복
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false; //Email(=ID) 미중복
	}

	//닉네임 중복 체크
	public boolean checkUniqueNickname(String nickname) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = -1;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM USERS_DETAIL WHERE nickname = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nickname);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
				return count > 0;
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return false; //미중복
	}

	//id -> 닉네임 얻기
	public String findNicknameByUserId(String id) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String nickname = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT nickname FROM USERS INNER JOIN "
					+ "USERS_DETAIL USING(user_num) WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				nickname = rs.getString(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return nickname;
	}
	//id -> 비밀번호 얻기
	public String findPasswdByUserId(String id) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String passwd = null;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT PASSWD FROM USERS INNER JOIN "
					+ "USERS_DETAIL USING(user_num) WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				passwd = rs.getString(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return passwd;
	}
	//id -> user_num 얻기
	public long findUserNumByUserId(String id) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		long user_num = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT user_num FROM USERS WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user_num = rs.getLong(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return user_num;
	}
	//user_num -> id 얻기
	public String findUserIdByUserNum(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String id = "";
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT id FROM users WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);

			rs = pstmt.executeQuery();
			if(rs.next()) {
				id = rs.getString(1);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return id;
	}
	//닉네임 업데이트
	public void updateNickname(long user_num, String nickname)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE users_detail SET nickname=? WHERE user_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, nickname);
			pstmt.setLong(2, user_num);
			//SQL문 실행
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//프로필 사진 수정
	public void updateMyPhoto(String photo, long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			//커넥션풀로부터 커넥션을 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "UPDATE users_detail SET photo=? WHERE user_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, photo);
			pstmt.setLong(2, user_num);
			//SQL문 실행
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}		
	}

	//로그인 처리
	public UserVO checkMember(String id)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO user = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT * FROM users JOIN users_detail "
					+ "USING(user_num) WHERE id=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setString(1, id);
			//SQL문 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new UserVO();
				user.setUser_num(rs.getLong("user_num"));
				user.setId(rs.getString("id"));
				user.setAuth(rs.getInt("auth"));
				user.setNickname(rs.getString("nickname"));
				user.setPasswd(rs.getString("passwd"));
				user.setPhoto(rs.getString("photo"));
				user.setRegion_cd(rs.getString("region_cd"));
				user.setScore(rs.getLong("score"));
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return user;
	}

	// 회원 상세정보
	public UserVO getUser(long user_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO user = null;
		LocationVO location = null;
		String sql = null;

		try {
			//커넥션풀로부터 커넥션을 할당
			conn = DBUtil.getConnection();
			//SQL문 작성
			sql = "SELECT u.*, ud.*, l.region_nm FROM users u "
					+ "JOIN users_detail ud ON u.user_num=ud.user_num "
					+ "LEFT OUTER JOIN location l ON ud.region_cd=l.region_cd "
					+ "WHERE u.user_num=?";
			//PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt.setLong(1, user_num);
			//SQL문을 실행
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new UserVO();
				user.setUser_num(rs.getLong("user_num"));
				user.setId(rs.getString("id"));
				user.setAuth(rs.getInt("auth"));
				user.setReport_count(rs.getInt("report_count"));
				user.setName(rs.getString("name"));
				user.setNickname(rs.getString("nickname"));
				user.setPasswd(rs.getString("passwd"));
				user.setPhone(rs.getString("phone"));
				user.setPhoto(rs.getString("photo"));
				user.setReg_date(rs.getDate("reg_date"));
				user.setModi_date(rs.getDate("modi_date"));
				user.setScore(rs.getLong("score"));
				user.setRegion_cd(rs.getString("region_cd"));

				location = new LocationVO();
				location.setRegion_cd(rs.getString("region_cd"));
				location.setRegion_nm(rs.getString("region_nm"));

				user.setLocationVO(location); // 유저 객체에 지역정보 저장
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return user;
	}

	// 회원정보수정
	public void updateUser(UserVO user) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users_detail SET name=?,nickname=?,phone=?,region_cd=?,modi_date=SYSDATE WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, user.getName());
			pstmt.setString(++cnt, user.getNickname());
			pstmt.setString(++cnt, user.getPhone());
			pstmt.setString(++cnt, user.getRegion_cd());
			pstmt.setLong(++cnt, user.getUser_num());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 비밀번호수정
	public void updatePassword(String passwd,long user_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql =  null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users_detail SET passwd=? WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, passwd);
			pstmt.setLong(2, user_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 회원탈퇴
	public void deleteUser(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;

		try {
			// 커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			// auto commit 해제
			conn.setAutoCommit(false);

			// auth 값 변경
			sql = "UPDATE users SET auth=0 WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();

			// users_detail의 레코드 삭제
			sql = "DELETE FROM users_detail WHERE user_num=?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setLong(1, user_num);
			pstmt2.executeUpdate();

			// 모든 SQL문의 실행이 성공하면 커밋
			conn.commit();
		}catch(Exception e) {
			// SQL문이 하나라도 실패하면 롤백
			conn.rollback();			
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 회원번호 -> 닉네임
	public String getUserNickname(Long user_num) throws Exception {
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

	// 키워드 개수 가져오기 
	public int getKeywordCount(Long user_num) throws Exception {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null; 

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM keyword WHERE user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
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
	// 키워드 중복 체크 (true : 중복, false : 중복 아님)
	public boolean isKeywordExists(Long user_num, String k_word) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null; 
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(*) FROM keyword WHERE user_num = ? AND k_word = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setString(2, k_word);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}
	// 키워드(알림) 등록
	public void insertKeyword(KeywordVO keywordVO) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO keyword (k_num,user_num,k_word) VALUES (keyword_seq.nextval,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, keywordVO.getUser_num());
			pstmt.setString(2, keywordVO.getK_word());
			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 키워드(알림) 삭제
	public void deleteKeyword(KeywordVO keywordVO)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "DELETE FROM keyword WHERE user_num=? AND k_word=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, keywordVO.getUser_num());
			pstmt.setString(2, keywordVO.getK_word());
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	// 내가 등록한 키워드 목록
	public List<KeywordVO> getListKeyword(long user_num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<KeywordVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM (SELECT k.* FROM keyword k JOIN users u ON k.user_num = u.user_num "
					+ "WHERE k.user_num = ? ORDER BY k.k_num DESC) WHERE ROWNUM <= 3";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();
			list = new ArrayList<KeywordVO>();
			while(rs.next()) {
				KeywordVO keyword = new KeywordVO();
				keyword.setK_num(rs.getLong("k_num"));
				// HTML 태그를 허용하지 않음
				keyword.setK_word(StringUtil.useNoHtml(rs.getString("k_word")));

				list.add(keyword);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	//키워드랑 해당 유저번호 전부 리스트에 반환
	public List<Map<String, String>> getAllKeywordsWithUsers() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<Map<String, String>> list = new ArrayList<>();

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT user_num, k_word FROM keyword";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				map.put("user_num", rs.getString("user_num"));
				map.put("k_word", rs.getString("k_word"));
				list.add(map);
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}


	//관리자
	//전체 내용 개수,검색 내용 개수
	public int getMemberCountByAdmin(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE id LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE phone LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM users LEFT OUTER JOIN "
					+ "users_detail USING(user_num) " + sub_sql;

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
	public List<UserVO> getListMemberByAdmin(int start,int end, String keyfield,String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE id LIKE '%' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE nickname LIKE '%' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE phone LIKE '%' || ? || '%'";
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM users LEFT OUTER JOIN users_detail "
					+ "USING(user_num) " + sub_sql 
					+ " ORDER BY reg_date DESC NULLS LAST)a) "
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<UserVO>();
			while(rs.next()) {
				UserVO user = new UserVO();
				user.setUser_num(rs.getLong("user_num"));
				user.setId(rs.getString("id"));
				user.setNickname(rs.getString("nickname"));
				user.setAuth(rs.getInt("auth"));
				user.setReport_count(rs.getInt("report_count"));

				list.add(user);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}

	//회원 상세정보 - 관리자
	public UserVO getDetailMemberByAdmin(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVO user = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM users JOIN users_detail "
					+ " USING(user_num) WHERE user_num=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				user = new UserVO();
				user.setUser_num(rs.getLong("user_num"));
				user.setId(rs.getString("id"));
				user.setAuth(rs.getInt("auth"));
				user.setReport_count(rs.getInt("report_count"));
				user.setName(rs.getString("name"));
				user.setNickname(rs.getString("nickname"));
				user.setPasswd(rs.getString("passwd"));

				String phone = rs.getString("phone");
				String formatted = phone.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
				user.setPhone(formatted);

				user.setPhoto(rs.getString("photo"));
				user.setReg_date(rs.getDate("reg_date"));
				user.setModi_date(rs.getDate("modi_date"));
				user.setScore(rs.getLong("score"));
				user.setRegion_cd(rs.getString("region_cd"));
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return user;
	}

	//회원 수정 - 관리자
	public void updateUserByAdmin(UserVO user) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			sql = "UPDATE users SET auth=?, report_count=? WHERE user_num=?";
			pstmt1 = conn.prepareStatement(sql);
			pstmt1.setInt(++cnt, user.getAuth());
			pstmt1.setInt(++cnt, user.getReport_count());
			pstmt1.setLong(++cnt, user.getUser_num());
			pstmt1.executeUpdate();

			cnt = 0;
			sql = "UPDATE users_detail SET name=?, nickname=?, passwd=?, phone=?, photo=?, region_cd=?, score=?, "
					+ "modi_date=SYSDATE WHERE user_num=?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(++cnt, user.getName());
			pstmt2.setString(++cnt, user.getNickname());
			pstmt2.setString(++cnt, user.getPasswd());
			pstmt2.setString(++cnt, user.getPhone());
			pstmt2.setString(++cnt, user.getPhoto());
			pstmt2.setString(++cnt, user.getRegion_cd());
			pstmt2.setLong(++cnt, user.getScore());
			pstmt2.setLong(++cnt, user.getUser_num());
			pstmt2.executeUpdate();

			conn.commit(); // 성공 시 커밋

		} catch (Exception e) {
			conn.rollback(); // 실패 시 롤백
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt1, conn);
		}
	}

	//회원등록 - 관리자
	public void insertUserByAdmin(UserVO user) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		String sql = null;
		long num = 0; //시퀀스 번호 저장
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			//오토 커밋 해제
			conn.setAutoCommit(false);

			//회원번호(user_num) 생성
			sql = "SELECT users_seq.nextval FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				num = rs.getLong(1);
			}

			sql = "INSERT INTO users (user_num,id,auth) VALUES (?,?,?)";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setLong(1, num);
			pstmt2.setString(2, user.getId());
			pstmt2.setInt(3, user.getAuth());
			pstmt2.executeUpdate();

			String photo = user.getPhoto();
			if (photo != null && !photo.isEmpty()) {
				sql = "INSERT INTO users_detail (user_num,name,nickname,passwd,"
						+ "phone,region_cd,photo) VALUES (?,?,?,?,?,?,?)";
				pstmt3 = conn.prepareStatement(sql);
				pstmt3.setLong(++cnt, num);
				pstmt3.setString(++cnt, user.getName());
				pstmt3.setString(++cnt, user.getNickname());
				pstmt3.setString(++cnt, user.getPasswd());
				pstmt3.setString(++cnt, user.getPhone());
				pstmt3.setString(++cnt, user.getRegion_cd());
				pstmt3.setString(++cnt, photo);
			} else {
				sql = "INSERT INTO users_detail (user_num,name,nickname,passwd,"
						+ "phone,region_cd) VALUES (?,?,?,?,?,?)";
				pstmt3 = conn.prepareStatement(sql);
				pstmt3.setLong(++cnt, num);
				pstmt3.setString(++cnt, user.getName());
				pstmt3.setString(++cnt, user.getNickname());
				pstmt3.setString(++cnt, user.getPasswd());
				pstmt3.setString(++cnt, user.getPhone());
				pstmt3.setString(++cnt, user.getRegion_cd());
			}

			pstmt3.executeUpdate();

			//SQL 실행시 모두 성공하면 commit
			conn.commit();
		}catch(Exception e) {
			//SQL문이 하나라도 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt, conn);
		}
	}

	// 회원별 region_cd -> region_nm
	public String getRegionNmByUser(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String regionNm = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT l.region_nm FROM users_detail ud "
					+ "JOIN location l ON ud.region_cd = l.region_cd "
					+ "WHERE ud.user_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				regionNm = rs.getString("region_nm");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return regionNm;
	}

	// 신고 카운트 가져옴
	public int checkReportCount(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT report_count FROM users WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
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

	// 신고하면 신고카운터 1증가
	public void insertReportCount(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users SET report_count = report_count + 1 WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 신고 삭제 시 신고카운터 1감소
	public void deleteReportCount(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users SET report_count = report_count - 1 WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 신고 횟수 3회 이상이면 회원 정지 처리(user_auth=1)
	public void deleteUserByReport(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users SET auth=1 WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 신고 횟수 3회 이하로 변경하면 회원 정지 취소(user_auth=2)
	public void revertDeleteUserByReport(long user_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users SET auth=2 WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 온도 수정
	public void modifyTemperatureUser(long user_num, int score) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE users_detail SET score = score + ? WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, score);
			pstmt.setLong(2, user_num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	public int getUserTemperature(long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int temp = 0;

		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT score FROM users_detail WHERE user_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				temp = rs.getInt("score");
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return temp;
	}
	
}
