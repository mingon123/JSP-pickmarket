package kr.chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.chat.vo.ProductChatVO;
import kr.util.DBUtil;

public class ProductChatDAO {
	//싱글턴 패턴
	private static ProductChatDAO instance = new ProductChatDAO();
	public static ProductChatDAO getInstance() {
		return instance;
	}
	private ProductChatDAO() {}

	//채팅 등록
	public void insertChat(ProductChatVO chat) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO product_chat (chat_num, chatroom_num, user_num, message, filename) "
					+ "VALUES (PRODUCT_CHAT_SEQ.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,chat.getChatroom_num());
			pstmt.setLong(2,chat.getUser_num());
			pstmt.setString(3,chat.getMessage());
			pstmt.setString(4,chat.getFilename());

			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}


	//채팅 - 채팅메시지 읽기(목록)
	public List<ProductChatVO> getChatDetail(long user_num, long chatroom_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductChatVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			//오토 커밋해제
			conn.setAutoCommit(false);
			sql = "SELECT pc.chat_num, pc.chatroom_num, pc.user_num, pc.message, pc.filename, pc.chat_date, pc.read_check, pc.deleted, u.nickname, pc.processed "
					+ "FROM product_chat pc "
					+ "JOIN users_detail u ON pc.user_num = u.user_num "
					+ "WHERE pc.chatroom_num=? "
					+ "ORDER BY pc.chat_date ASC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chatroom_num);

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductChatVO>();

			while(rs.next()) {
				ProductChatVO chat = new ProductChatVO();
				chat.setChat_num(rs.getLong("chat_num"));
				chat.setChatroom_num(rs.getLong("chatroom_num"));
				chat.setUser_num(rs.getLong("user_num"));
				chat.setMessage(rs.getString("message"));
				chat.setFilename(rs.getString("filename"));
				chat.setChat_date(rs.getString("chat_date"));
				chat.setRead_check(rs.getInt("read_check"));
				chat.setDeleted(rs.getInt("deleted"));
				chat.setNickname(rs.getString("nickname"));
				chat.setProcessed(rs.getInt("processed"));

				list.add(chat);
			}
			//SQL문이 모두 성공 시 커밋
			conn.commit();

		}catch(Exception e) {
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	//chat_num으로 내용 가져오기
	public ProductChatVO getChatByNum(long chat_num) throws Exception {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		ProductChatVO chat = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT * FROM product_chat WHERE chat_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chat_num);

			rs = pstmt.executeQuery();
			if(rs.next()) {
				chat = new ProductChatVO();
				chat.setChat_num(rs.getLong("chat_num"));
				chat.setChatroom_num(rs.getLong("chatroom_num"));
				chat.setUser_num(rs.getLong("user_num"));
				chat.setMessage(rs.getString("message"));
				chat.setFilename(rs.getString("filename"));
				chat.setChat_date(rs.getString("chat_date"));
				chat.setRead_check(rs.getInt("read_check"));
				chat.setDeleted(rs.getInt("deleted"));
				chat.setProcessed(rs.getInt("processed"));
			}

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return chat;
	}

	//삭제 시 내용 업데이트
	public void deleteMessageChat(ProductChatVO chat) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			if (chat.getMessage() != null && chat.getMessage().startsWith("[pay:")) {
				// 송금 메시지인 경우엔 송금 취소로 대체
				sql = "UPDATE product_chat SET message='[pay_cancel]', filename=NULL, deleted=0 WHERE chat_num=?";
			} else if(chat.getMessage() != null && chat.getMessage().startsWith("[request:")){
				sql = "UPDATE product_chat SET message='[request_cancel]', filename=NULL, deleted=0 WHERE chat_num=?";
			}else {
				// 일반 메시지는 null 처리
				sql = "UPDATE product_chat SET message=NULL, filename=NULL, deleted=0 WHERE chat_num=?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chat.getChat_num());

			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	// 읽음 처리
	public void updateReadStatus(long user_num, long chatroom_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product_chat SET read_check=0 "
					+ "WHERE chatroom_num = ? AND user_num != ? AND read_check = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chatroom_num);
			pstmt.setLong(2, user_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	// 버튼 사용시
	public void updateProcessedStatus(long chat_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product_chat SET processed = 1 WHERE chat_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chat_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//관리자
	//삭제시 추가 함수
	public void adminDeleteChat(long chat_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product_chat SET deleted=9 WHERE chat_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chat_num);

			pstmt.executeUpdate();

		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}

	//관리자전용 채팅메시지 읽기(목록)
	public List<ProductChatVO> getChatDetailByAdmin(long chatroom_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductChatVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			//오토 커밋해제
			conn.setAutoCommit(false);
			sql = "SELECT pc.chat_num, pc.chatroom_num, pc.user_num, pc.message, " +
					"pc.filename, pc.chat_date, pc.read_check, pc.deleted, pc.processed, " +
					"u.nickname " +
					"FROM product_chat pc " +
					"JOIN users_detail u ON pc.user_num = u.user_num " +
					"WHERE pc.chatroom_num = ? " +
					"ORDER BY pc.chat_date ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chatroom_num);

			rs = pstmt.executeQuery();
			list = new ArrayList<ProductChatVO>();

			while(rs.next()) {
				ProductChatVO chat = new ProductChatVO();
				chat.setChat_num(rs.getLong("chat_num"));
				chat.setChatroom_num(rs.getLong("chatroom_num"));
				chat.setUser_num(rs.getLong("user_num"));
				chat.setMessage(rs.getString("message"));
				chat.setFilename(rs.getString("filename"));
				chat.setChat_date(rs.getString("chat_date"));
				chat.setRead_check(rs.getInt("read_check"));
				chat.setDeleted(rs.getInt("deleted"));
				chat.setNickname(rs.getString("nickname"));
				chat.setProcessed(rs.getInt("processed"));

				list.add(chat);
			}
			//SQL문이 모두 성공 시 커밋
			conn.commit();

		}catch(Exception e) {
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}


}
