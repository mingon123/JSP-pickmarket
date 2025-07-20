package kr.chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import kr.chat.vo.ProductChatRoomVO;
import kr.chat.vo.ProductChatVO;
import kr.product.vo.ProductVO;
import kr.report.vo.ReportVO;
import kr.user.vo.UserVO;
import kr.util.DBUtil;

public class ProductChatRoomDAO {
	//싱글턴 패턴
	private static ProductChatRoomDAO instance = new ProductChatRoomDAO();
	public static ProductChatRoomDAO getInstance() {
		return instance;
	}
	private ProductChatRoomDAO() {}

	//채팅방 생성 여부 체크 & 정보 반환
	public ProductChatRoomVO checkChatRoom(long product_num, long buyer_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductChatRoomVO vo = null;
		String sql = null;

		try {
			conn =DBUtil.getConnection();
			sql = "SELECT c.chatroom_num, c.product_num, c.seller_num, c.buyer_num, c.room_date, "
					+ "		c.deal_datetime, c.deal_x_loc, c.deal_y_loc, "
					+ "       seller.nickname AS seller_nickname, "
					+ "       seller.photo AS seller_photo, "
					+ "       buyer.nickname AS buyer_nickname, "
					+ "       buyer.photo AS buyer_photo, "
					+ "       p.title AS product_title, "
					+ "       p.thumbnail_img AS product_thumbnail,"
					+ "		p.state AS product_state  "
					+ "FROM product_chatroom c "
					+ "JOIN product p ON c.product_num = p.product_num "
					+ "JOIN users_detail seller ON c.seller_num = seller.user_num "
					+ "JOIN users_detail buyer ON c.buyer_num = buyer.user_num "
					+ "WHERE c.product_num = ? AND c.buyer_num = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			pstmt.setLong(2, buyer_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				vo = new ProductChatRoomVO();
				vo.setChatroom_num(rs.getLong("chatroom_num"));
				vo.setProduct_num(rs.getLong("product_num"));
				vo.setSeller_num(rs.getLong("seller_num"));
				vo.setBuyer_num(rs.getLong("buyer_num"));
				vo.setDeal_datetime(rs.getTimestamp("deal_datetime"));
				vo.setDeal_x_loc(rs.getDouble("deal_x_loc"));
				vo.setDeal_y_loc(rs.getDouble("deal_y_loc"));

				// 상품 정보 매핑
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("product_title"));
				product.setThumbnail_img(rs.getString("product_thumbnail"));
				product.setState(rs.getInt("product_state"));
				vo.setProduct(product);

				// 판매자 정보 매핑
				UserVO seller = new UserVO();
				seller.setNickname(rs.getString("seller_nickname"));
				seller.setPhoto(rs.getString("seller_photo"));
				vo.setSeller(seller);

				// 구매자 정보 매핑
				UserVO buyer = new UserVO();
				buyer.setNickname(rs.getString("buyer_nickname"));
				buyer.setPhoto(rs.getString("buyer_photo"));
				vo.setBuyer(buyer);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return vo;
	}


	//채팅방 정보 반환
	public ProductChatRoomVO selectChatRoom(long chatroom_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductChatRoomVO vo = null;
		String sql = null;
		try {
			conn =DBUtil.getConnection();
			sql = "SELECT c.chatroom_num, c.product_num, c.seller_num, c.buyer_num, c.room_date, "
					+ "	    c.deal_datetime, c.deal_x_loc, c.deal_y_loc, "
					+ "       seller.nickname AS seller_nickname, "
					+ "       seller.photo AS seller_photo, "
					+ "       buyer.nickname AS buyer_nickname, "
					+ "       buyer.photo AS buyer_photo, "
					+ "       p.title AS product_title, "
					+ "       p.thumbnail_img AS product_thumbnail,"
					+ "		p.state AS product_state  "
					+ "FROM product_chatroom c "
					+ "JOIN product p ON c.product_num = p.product_num "
					+ "JOIN users_detail seller ON c.seller_num = seller.user_num "
					+ "JOIN users_detail buyer ON c.buyer_num = buyer.user_num "
					+ "WHERE c.chatroom_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chatroom_num);

			rs=pstmt.executeQuery();
			if(rs.next()) {
				vo = new ProductChatRoomVO();
				vo.setChatroom_num(rs.getLong("chatroom_num"));
				vo.setProduct_num(rs.getLong("product_num"));
				vo.setSeller_num(rs.getLong("seller_num"));
				vo.setBuyer_num(rs.getLong("buyer_num"));
				vo.setRoom_date(rs.getDate("room_date"));
				vo.setDeal_datetime(rs.getTimestamp("deal_datetime"));
				vo.setDeal_x_loc(rs.getDouble("deal_x_loc"));
				vo.setDeal_y_loc(rs.getDouble("deal_y_loc"));

				// 상품 정보 매핑
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("product_title"));
				product.setThumbnail_img(rs.getString("product_thumbnail"));
				product.setState(rs.getInt("product_state"));
				vo.setProduct(product);

				// 판매자 정보 매핑
				UserVO seller = new UserVO();
				seller.setNickname(rs.getString("seller_nickname"));
				seller.setPhoto(rs.getString("seller_photo"));
				vo.setSeller(seller);

				// 구매자 정보 매핑
				UserVO buyer = new UserVO();
				buyer.setNickname(rs.getString("buyer_nickname"));
				buyer.setPhoto(rs.getString("buyer_photo"));
				vo.setBuyer(buyer);
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return vo;
	}


	//채팅방 생성
	public void insertChatRoom(ProductChatRoomVO vo) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO product_chatroom "
					+ "(chatroom_num, product_num, seller_num, buyer_num) "
					+ "VALUES (PRODUCT_CHATROOM_SEQ.nextval,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getProduct_num());
			pstmt.setLong(2, vo.getSeller_num());
			pstmt.setLong(3, vo.getBuyer_num());

			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}


	//로그인한 유저의 채팅방 목록(+ 채팅방 정보까지)
	public List<ProductChatRoomVO> getChatRoomListWithDetails(long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductChatRoomVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT r.chatroom_num, r.product_num, r.seller_num, r.buyer_num, "
					+ "r.room_date, r.deal_datetime, r.deal_x_loc, r.deal_y_loc, "
					+ "p.title AS product_title, p.thumbnail_img, p.state AS product_state,  "
					+ "seller.nickname AS seller_nickname, seller.photo AS seller_photo, "
					+ "buyer.nickname AS buyer_nickname, buyer.photo AS buyer_photo, "
					+ "c.message AS last_message, c.chat_date AS last_message_time, c.deleted AS last_deleted, c.filename AS last_filename, "
					+ "("
					+ "  SELECT COUNT(*) "
					+ "  FROM product_chat pc "
					+ "  WHERE pc.chatroom_num = r.chatroom_num "
					+ "  AND pc.user_num != ? "
					+ "  AND pc.read_check = 1 "
					+ ") AS unread_count "
					+ "FROM product_chatroom r "
					+ "JOIN product p ON r.product_num = p.product_num "
					+ "JOIN users_detail seller ON r.seller_num = seller.user_num "
					+ "JOIN users_detail buyer ON r.buyer_num = buyer.user_num "
					+ "INNER JOIN ( "
					+ "    SELECT c1.chatroom_num, c1.message, c1.chat_date, c1.deleted, c1.filename "
					+ "    FROM product_chat c1 "
					+ "    INNER JOIN ( "
					+ "        SELECT chatroom_num, MAX(chat_date) AS max_date "
					+ "        FROM product_chat "
					+ "        GROUP BY chatroom_num "
					+ "    ) c2 ON c1.chatroom_num = c2.chatroom_num AND c1.chat_date = c2.max_date "
					+ ") c ON r.chatroom_num = c.chatroom_num "
					+ "WHERE r.seller_num = ? OR r.buyer_num = ? "
					+ "ORDER BY c.chat_date DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user_num);
			pstmt.setLong(2, user_num);
			pstmt.setLong(3, user_num);

			rs = pstmt.executeQuery();
			list = new ArrayList<>();

			while (rs.next()) {
				ProductChatRoomVO room = new ProductChatRoomVO();
				// 채팅방 기본 정보
				room.setChatroom_num(rs.getLong("chatroom_num"));
				room.setProduct_num(rs.getLong("product_num"));
				room.setSeller_num(rs.getLong("seller_num"));
				room.setBuyer_num(rs.getLong("buyer_num"));
				room.setRoom_date(rs.getDate("room_date"));
				room.setDeal_datetime(rs.getTimestamp("deal_datetime"));
				room.setDeal_x_loc(rs.getDouble("deal_x_loc"));
				room.setDeal_y_loc(rs.getDouble("deal_y_loc"));
				room.setUnreadCount(rs.getInt("unread_count"));

				// 상품 정보
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("product_title"));
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setState(rs.getInt("product_state"));
				room.setProduct(product);

				// 판매자 정보
				UserVO seller = new UserVO();
				seller.setNickname(rs.getString("seller_nickname"));
				seller.setPhoto(rs.getString("seller_photo"));
				room.setSeller(seller);

				// 구매자 정보
				UserVO buyer = new UserVO();
				buyer.setNickname(rs.getString("buyer_nickname"));
				buyer.setPhoto(rs.getString("buyer_photo"));
				room.setBuyer(buyer);

				// 마지막 채팅
				ProductChatVO lastChat = new ProductChatVO();
				String message = rs.getString("last_message");
				String filename = rs.getString("last_filename");  
				int deleted = rs.getInt("last_deleted");  

				String preview;
				if (deleted == 0 && message == null) {
					preview = "삭제된 메시지입니다";
				}else if(deleted == 0 && message.equals("[request_cancel]")){
					preview = "송금요청이 취소되었습니다.";
				}else if(deleted == 0 && message.equals("[pay_cancel]")){
					preview = "송금이 취소되었습니다.";
				}else if(message != null && deleted == 1 && message.startsWith("[pay:")){
					preview = "송금 메세지입니다.";
				}else if(message != null && deleted == 1 && message.startsWith("[request:")){
					preview = "송금요청 메세지입니다.";
				}else if(deleted == 9){
					preview = "관리자에 의해 삭제된 메세지입니다";
				}else {
					boolean hasPhoto = filename != null && !filename.trim().isEmpty();
					boolean hasMessage = message != null && !message.trim().isEmpty();
					if (hasPhoto && hasMessage) {
						preview = "[사진] " + message;
					} else if (hasPhoto) {
						preview = "[사진]";
					} else if (hasMessage) {
						preview = message;
					} else {
						preview = "네트워크 오류 발생"; // 메시지도 사진도 없는 이상한 상황
					}
					// 30자 제한 처리
					if (preview.length() > 30) {
						preview = preview.substring(0, 30) + "...";
					}
				}

				lastChat.setMessage(preview);
				lastChat.setChat_date(rs.getString("last_message_time"));
				lastChat.setDeleted(deleted);
				lastChat.setFilename(filename);
				room.setLastChat(lastChat);

				list.add(room);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	// 특정 상품의 판매자가 보는 채팅방 목록
	public List<ProductChatRoomVO> getSellerChatRoomsByProduct(long product_num, long seller_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductChatRoomVO> list = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql = "SELECT r.chatroom_num, r.product_num, r.seller_num, r.buyer_num, "
					+ "r.room_date, r.deal_datetime, r.deal_x_loc, r.deal_y_loc, "
					+ "p.title AS product_title, p.thumbnail_img, p.state AS product_state, "
					+ "seller.nickname AS seller_nickname, seller.photo AS seller_photo, "
					+ "buyer.nickname AS buyer_nickname, buyer.photo AS buyer_photo, "
					+ "c.message AS last_message, c.chat_date AS last_message_time, c.deleted AS last_deleted, c.filename AS last_filename, "
					+ "("
					+ "  SELECT COUNT(*) "
					+ "  FROM product_chat pc "
					+ "  WHERE pc.chatroom_num = r.chatroom_num "
					+ "  AND pc.user_num != ? "
					+ "  AND pc.read_check = 1 "
					+ ") AS unread_count "
					+ "FROM product_chatroom r "
					+ "JOIN product p ON r.product_num = p.product_num "
					+ "JOIN users_detail seller ON r.seller_num = seller.user_num "
					+ "JOIN users_detail buyer ON r.buyer_num = buyer.user_num "
					+ "INNER JOIN ( "
					+ "    SELECT c1.chatroom_num, c1.message, c1.chat_date, c1.deleted, c1.filename "
					+ "    FROM product_chat c1 "
					+ "    INNER JOIN ( "
					+ "        SELECT chatroom_num, MAX(chat_date) AS max_date "
					+ "        FROM product_chat "
					+ "        GROUP BY chatroom_num "
					+ "    ) c2 ON c1.chatroom_num = c2.chatroom_num AND c1.chat_date = c2.max_date "
					+ ") c ON r.chatroom_num = c.chatroom_num "
					+ "WHERE r.product_num = ? AND r.seller_num = ? "
					+ "ORDER BY c.chat_date DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, seller_num); 
			pstmt.setLong(2, product_num);
			pstmt.setLong(3, seller_num);

			rs = pstmt.executeQuery();
			list = new ArrayList<>();

			while (rs.next()) {
				ProductChatRoomVO room = new ProductChatRoomVO();
				room.setChatroom_num(rs.getLong("chatroom_num"));
				room.setProduct_num(rs.getLong("product_num"));
				room.setSeller_num(rs.getLong("seller_num"));
				room.setBuyer_num(rs.getLong("buyer_num"));
				room.setRoom_date(rs.getDate("room_date"));
				room.setDeal_datetime(rs.getTimestamp("deal_datetime"));
				room.setDeal_x_loc(rs.getDouble("deal_x_loc"));
				room.setDeal_y_loc(rs.getDouble("deal_y_loc"));
				room.setUnreadCount(rs.getInt("unread_count"));

				// 상품 정보
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getLong("product_num"));
				product.setTitle(rs.getString("product_title"));
				product.setThumbnail_img(rs.getString("thumbnail_img"));
				product.setState(rs.getInt("product_state"));
				room.setProduct(product);

				// 판매자 정보
				UserVO seller = new UserVO();
				seller.setNickname(rs.getString("seller_nickname"));
				seller.setPhoto(rs.getString("seller_photo"));
				room.setSeller(seller);

				// 구매자 정보
				UserVO buyer = new UserVO();
				buyer.setNickname(rs.getString("buyer_nickname"));
				buyer.setPhoto(rs.getString("buyer_photo"));
				room.setBuyer(buyer);

				// 마지막 채팅
				ProductChatVO lastChat = new ProductChatVO();
				String message = rs.getString("last_message");
				String filename = rs.getString("last_filename");
				int deleted = rs.getInt("last_deleted");

				String preview;
				if (deleted == 0) {
					preview = "삭제된 메시지입니다";
				} else {
					boolean hasPhoto = filename != null && !filename.trim().isEmpty();
					boolean hasMessage = message != null && !message.trim().isEmpty();
					if (hasPhoto && hasMessage) {
						preview = "[사진] " + message;
					} else if (hasPhoto) {
						preview = "[사진]";
					} else if (hasMessage) {
						preview = message;
					} else {
						preview = "네트워크 오류 발생";
					}
					if (preview.length() > 30) {
						preview = preview.substring(0, 30) + "...";
					}
				}

				lastChat.setMessage(preview);
				lastChat.setChat_date(rs.getString("last_message_time"));
				lastChat.setDeleted(deleted);
				lastChat.setFilename(filename);
				room.setLastChat(lastChat);

				list.add(room);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	//약속 시간/장소 설정 업데이트
	public void updateDealInfo(long chatroom_num, Timestamp dealTimestamp, double deal_x_loc, double deal_y_loc) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;

		try {
			conn = DBUtil.getConnection();
			sql =  "UPDATE product_chatroom SET deal_datetime = ?,"
					+ " deal_x_loc=?, deal_y_loc=? WHERE chatroom_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, dealTimestamp);
			pstmt.setDouble(2, deal_x_loc);
			pstmt.setDouble(3, deal_y_loc);
			pstmt.setLong(4, chatroom_num);
			pstmt.executeUpdate();

		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	//약속 시간/장소 삭제
	public void clearDealInfo(long chatroom_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE product_chatroom SET deal_datetime = NULL, "
					+ "deal_x_loc = NULL, deal_y_loc = NULL WHERE chatroom_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chatroom_num);
			pstmt.executeUpdate();
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}

	}

	// 채팅방에서 내가 아직 안 읽은 메시지 수 조회
	public int countUnreadMessages(long chatroom_num, long user_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT COUNT(*) FROM product_chat "
					+ "	   WHERE chatroom_num = ? AND user_num != ? AND read_check = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, chatroom_num);
			pstmt.setLong(2, user_num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}

	//상품 별 채팅방
	public List<ProductChatRoomVO> getChatRoomsByProduct(long product_num) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<ProductChatRoomVO> list = new ArrayList<>();

		try {
			conn = DBUtil.getConnection();
			sql = 	"SELECT r.*, b.nickname AS buyer_nickname, b.photo AS buyer_photo, "
					+ "c.message AS last_message, c.chat_date AS last_message_time, "
					+ "("
					+ "   SELECT COUNT(*) "
					+ "   FROM product_chat pc " 
					+ "   WHERE pc.chatroom_num = r.chatroom_num "
					+ "   AND pc.user_num != ? "
					+ "   AND pc.read_check = 1"
					+ " ) AS unread_count"
					+ "FROM product_chatroom r "
					+ "JOIN users_detail b ON r.buyer_num = b.user_num "
					+ "LEFT JOIN ( "
					+ "  SELECT chatroom_num, message, chat_date "
					+ "  FROM product_chat "
					+ "  WHERE (chatroom_num, chat_date) IN ( "
					+ "    SELECT chatroom_num, MAX(chat_date) "
					+ "    FROM product_chat "
					+ "    GROUP BY chatroom_num "
					+ "  ) "
					+ ") c ON r.chatroom_num = c.chatroom_num "
					+ "WHERE r.product_num = ? AND r.seller_num = ? AND c.chat_date IS NOT NULL " 
					+ "ORDER BY c.chat_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, product_num);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				ProductChatRoomVO room = new ProductChatRoomVO();
				room.setChatroom_num(rs.getLong("chatroom_num"));
				room.setProduct_num(rs.getLong("product_num"));
				room.setBuyer_num(rs.getLong("buyer_num"));
				room.setRoom_date(rs.getDate("room_date"));
				room.setUnreadCount(rs.getInt("unread_count"));

				UserVO buyer = new UserVO();
				buyer.setNickname(rs.getString("buyer_nickname"));
				buyer.setPhoto(rs.getString("buyer_photo"));
				room.setBuyer(buyer);

				ProductChatVO last = new ProductChatVO();
				last.setMessage(rs.getString("last_message") == null ? "[사진]" : rs.getString("last_message"));
				last.setChat_date(rs.getString("last_message_time"));
				room.setLastChat(last);

				list.add(room);
			}
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}

	//관리자
	//전체 내용 개수,검색 내용 개수
	public int getChatRoomCountByAdmin(String keyfield, String keyword) throws Exception{
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
					sub_sql += "WHERE seller.nickname LIKE '%' || ? || '%' OR buyer.nickname LIKE '%' || ? || '%'";
			}
			sql = "SELECT COUNT(*) FROM product_chatroom pc "
					+ "JOIN users_detail seller ON pc.seller_num = seller.user_num "
					+ "JOIN users_detail buyer ON pc.buyer_num = buyer.user_num "
					+ sub_sql;

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				if (keyfield.equals("1")) {
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
	public List<ProductChatRoomVO> getChatRoomListByAdmin(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ProductChatRoomVO> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;

		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) 
					sub_sql += "WHERE seller.nickname LIKE '%' || ? || '%' OR buyer.nickname LIKE '%' || ? || '%'";
			}

			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM (" 
					+ "SELECT pc.chatroom_num, pc.product_num, pc.seller_num, pc.buyer_num, pc.room_date, " 
					+ "seller.nickname AS seller_nickname, buyer.nickname AS buyer_nickname " 
					+ "FROM product_chatroom pc " 
					+ "JOIN users_detail seller ON pc.seller_num = seller.user_num " 
					+ "JOIN users_detail buyer ON pc.buyer_num = buyer.user_num " 
					+ sub_sql 
					+ " ORDER BY pc.room_date DESC " 
					+ ") a) " 
					+ "WHERE rnum >= ? AND rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) {
					pstmt.setString(++cnt, keyword);
					pstmt.setString(++cnt, keyword);
				}
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);

			rs = pstmt.executeQuery();

			list = new ArrayList<ProductChatRoomVO>();
			while(rs.next()) {
				ProductChatRoomVO chatroom = new ProductChatRoomVO();
				chatroom.setChatroom_num(rs.getLong("chatroom_num"));
				chatroom.setProduct_num(rs.getLong("product_num"));
				chatroom.setSeller_num(rs.getLong("seller_num"));
				chatroom.setBuyer_num(rs.getLong("buyer_num"));
				chatroom.setRoom_date(rs.getDate("room_date")); 
				
				UserVO seller = new UserVO();
	            seller.setNickname(rs.getString("seller_nickname"));
	            chatroom.setSeller(seller);

	            UserVO buyer = new UserVO();
	            buyer.setNickname(rs.getString("buyer_nickname"));
	            chatroom.setBuyer(buyer);

				list.add(chatroom);
			}			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}		
		return list;
	}



}
